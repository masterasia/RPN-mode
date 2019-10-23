package com.robert.schedule.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 任务注解
 * 
 * @author ldy
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Task {

	/**
	 * 默认字符串
	 */
	String DEFAULT = "DEFAULT";

	/**
	 * 名称; 默认DEFAULT：类名_方法名
	 * 
	 * @return 名称
	 */
	String name() default DEFAULT;

	/**
	 * 组; 默认 DEFAULT
	 * 
	 * @return 组
	 */
	String group() default DEFAULT;

	/**
	 * cron表达式
	 * 
	 * @return cron表达式
	 */
	String cron();

	/**
	 * description
	 * 
	 * @return 描述
	 */
	String desc() default "";
}
