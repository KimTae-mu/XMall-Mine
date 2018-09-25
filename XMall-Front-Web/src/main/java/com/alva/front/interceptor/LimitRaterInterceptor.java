package com.alva.front.interceptor;

import com.alva.front.limit.RedisRaterLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    }
}
