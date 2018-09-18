package com.alva.common.annotation;

import java.lang.annotation.*;

/**
 * <一句话描述>,
 * 限流注解
 *
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
@Target(ElementType.METHOD)     //作用在方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    int limit() default 5;
    int timeout() default 1000;
}
