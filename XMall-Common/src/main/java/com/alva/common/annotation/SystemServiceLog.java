package com.alva.common.annotation;

import java.lang.annotation.*;

/**
 * <一句话描述>,
 * 系统级别Service层自定义注解,拦截Service层
 *
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemServiceLog {
    String description() default "";
}
