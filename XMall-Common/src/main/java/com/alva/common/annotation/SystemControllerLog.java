package com.alva.common.annotation;

import java.lang.annotation.*;

/**
 * <一句话描述>,
 * 系统级别Controller层自定义注解,拦截Controller
 *
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})     //作用于参数或者方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemControllerLog {
    String description() default "";
}
