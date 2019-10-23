package com.robert.schedule.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 任务类注入spring注解<br>
 * <pre>
 *  添加这个注解主要原因是为了更好扫描方法上的@Task注解
 * </pre>
 *
 * @author ldy
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Job {

}
