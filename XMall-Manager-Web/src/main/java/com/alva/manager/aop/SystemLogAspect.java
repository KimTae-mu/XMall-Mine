package com.alva.manager.aop;

import com.alva.common.annotation.SystemControllerLog;
import com.alva.common.annotation.SystemServiceLog;
import com.alva.common.utils.IPInfoUtil;
import com.alva.common.utils.ObjectUtil;
import com.alva.common.utils.ThreadPoolUtil;
import com.alva.manager.pojo.TbLog;
import com.alva.manager.service.SystemService;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
@Aspect
@Component
public class SystemLogAspect {

    private Logger log = LoggerFactory.getLogger(SystemLogAspect.class);

    private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<>("ThreadLocal beginTime");

    @Autowired
    private SystemService systemService;

    @Autowired(required = false)
    private HttpServletRequest request;

    /**
     * Controller层切点,注解方式
     */
    @Pointcut("@annotation(com.alva.common.annotation.SystemControllerLog)")
    public void controllerAspect() {
        log.info("=========contorllerAspect==========");
    }

    /**
     * Service层切点,注解方式
     */
    @Pointcut("@annotation(com.alva.common.annotation.SystemServiceLog)")
    public void serviceAspect() {
        log.info("==========serviceAspect==========");
    }

    /**
     * 前置通知 (在方法执行之前返回) 用于拦截Controller层记录用户的操作的开始时间
     *
     * @param joinPoint 切点
     * @throws InterruptedException
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException {
        //线程绑定变量 (该数据只有当前请求的线程可见)
        Date beginTime = new Date();
        beginTimeThreadLocal.set(beginTime);
    }

    @After("controllerAspect()")
    public void doAfter(JoinPoint joinPoint) {
        try {
            String username = SecurityUtils.getSubject().getPrincipal().toString();

            if (username != null) {
                TbLog tbLog = new TbLog();

                //日志标题
                tbLog.setName();
                //日志类型
                tbLog.setType(1);
                //日志请求url
                tbLog.setUrl(request.getRequestURI());
                //请求方式
                tbLog.setRequestType(request.getMethod());
                //请求参数
                Map<String,String[]> logParams = request.getParameterMap();
                tbLog.setMapToParams(logParams);
                IPInfoUtil.getInfo(request, ObjectUtil.mapToStringAll(logParams));
                //请求用户
                tbLog.setUser(username);
                //请求IP
                tbLog.setIp(IPInfoUtil.getIpAddr(request));
                //IP地址
                tbLog.setIpInfo(IPInfoUtil.getIpCity(IPInfoUtil.getIpAddr(request)));
                //请求开始时间
                Date logStartTime = beginTimeThreadLocal.get();

                long beginTime = beginTimeThreadLocal.get().getTime();
                long endTime = System.currentTimeMillis();

                //请求耗时
                Long logElapsedTime = endTime - beginTime;
                tbLog.setTime(Math.toIntExact(logElapsedTime));
                tbLog.setCreateDate(logStartTime);

                //调用线程保存至数据库
                ThreadPoolUtil.getPool().execute(new SaveSystemLogThread(tbLog,systemService));
            }
        }catch (Exception e){
            log.error("AOP后置通知异常",e);
        }
    }

    /**
     * 保存日志
     */
    private static class SaveSystemLogThread implements Runnable{
        private TbLog tbLog;
        private SystemService systemService;

        public SaveSystemLogThread(TbLog tbLog,SystemService systemService){
            this.tbLog = tbLog;
            this.systemService = systemService;
        }
        @Override
        public void run() {
            systemService.addLog(tbLog);
        }
    }

    /**
     * 获取注解中对方法的描述信息,用于Controller层注解
     * @param joinPoint 切点
     * @return  方法描述
     * @throws Exception
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception{
        //获取目标类名
        String targetName = joinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取相关参数
        Object[] arguments = joinPoint.getArgs();
        //生成类对象
        Class targetClass = Class.forName(targetName);
        //获取该类中的方法
        Method[] methods = targetClass.getMethods();

        String description = "";

        for (Method method : methods){
            if(!method.getName().equals(methodName)){
                continue;
            }
            Class[] clazzs = method.getParameterTypes();
            if(clazzs.length != arguments.length){
                //比较方法中参数个数与从切点中获取的参数个数是否相同,原因是方法可以重载
                continue;
            }
            description = method.getAnnotation(SystemControllerLog.class).description();
        }
        return description;
    }

    /**
     * 获取注解中对方法的描述信息 用于Service层注解
     * @param joinPoint 切点
     * @return  方法描述
     * @throws Exception
     */
    public static String getServiceMethodDescription(JoinPoint joinPoint) throws Exception{
        //获取目标类名
        String targetName = joinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取相关参数
        Object[] arguments = joinPoint.getArgs();
        //生成类对象
        Class targetClass = Class.forName(targetName);
        //获取该类中的方法
        Method[] methods = targetClass.getMethods();

        String description = "";

        for (Method method : methods){
            if(!method.getName().equals(methodName)){
                continue;
            }
            Class[] clazzs = method.getParameterTypes();
            if(clazzs.length != arguments.length){
                continue;
            }
            description = method.getAnnotation(SystemServiceLog.class).description();
        }
        return description;
    }
}
