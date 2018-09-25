package com.alva.front.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
@Configuration
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private LimitRaterInterceptor limitRaterInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //注册拦截器
        InterceptorRegistration ir = registry.addInterceptor(limitRaterInterceptor);

        //配置拦截的路径
        ir.addPathPatterns("/**");
    }
}
