package com.alva.front.interceptor;

import com.alva.common.annotation.RateLimiter;
import com.alva.common.constant.CommonConstant;
import com.alva.common.exception.XmallException;
import com.alva.common.utils.IPInfoUtil;
import com.alva.front.limit.RedisRaterLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import cn.hutool.core.util.StrUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * <一句话描述>,
 * 限流拦截器
 *
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
@Component
public class LimitRaterInterceptor extends HandlerInterceptorAdapter {

    @Value("${xmall.rateLimit.enable}")
    private boolean rateLimitEnable;

    @Value("${xmall.rateLimit.limit}")
    private Integer limit;

    @Value("${xmall.rateLimit.timeout}")
    private Integer timeout;

    @Autowired
    private RedisRaterLimiter redisRaterLimiter;

    /**
     * 预处理回调方法,实现处理器的预处理 (如登录检查)
     * 第三个参数为相应的处理器,即controller
     * 返回true,表示继续流程,调用下一个拦截器或者处理器
     * 返回false,表示流程中断,通过response产生响应
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //IP限流 在线Demo所需,一秒限10个请求
        String token1 = redisRaterLimiter.acquireTokenFromBucket("XMALL" + IPInfoUtil.getIpAddr(request), 10, 1000);
        if(StrUtil.isBlank(token1)){
            throw new XmallException("你手速怎么这么快,请点慢一点");
        }

        if(rateLimitEnable){
            String token2 = redisRaterLimiter.acquireTokenFromBucket(CommonConstant.LIMIT_ALL, limit, timeout);
            if(StrUtil.isBlank(token2)){
                throw new XmallException("当前访问人数太多啦,请稍后再试");
            }
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);

        if(rateLimiter != null){
            int limit = rateLimiter.limit();
            int timeout = rateLimiter.timeout();
            String token3 = redisRaterLimiter.acquireTokenFromBucket(method.getName(), limit, timeout);
            if(StrUtil.isBlank(token3)){
                throw new XmallException("当前访问人数太多啦,请稍后再试");
            }
        }
        return true;
    }
}
