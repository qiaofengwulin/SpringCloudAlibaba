package com.qh.annotation;

import java.lang.annotation.*;

/**
 * 校验接口参数的自定义注解
 */
@Target({ElementType.METHOD}) //注解可以写在方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckParams {
}
