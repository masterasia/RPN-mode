package com.robert.schedule.bean;

import java.io.Serializable;

/**
 * 任务类
 * 
 *
 */
public class JobEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 组
	 */
	private String group;

	/**
	 * 目标类
	 */
	private String targetClass;

	/**
	 * 目标方法
	 */
	private String targetMethod;

	/**
	 * 方法参数类型
	 */
	private Class<?> paramsType;

	/**
	 * 参数
	 */
	private Object params;

	/**
	 * cron表达式
	 */
	private String cronExpression;

	/**
	 * 简单调度周期，单位：分钟
	 */
	private int interval;

	/**
	 * 任务状态； PAUSED：暂停 ；NORMAL：正常
	 */
	private String status;

	/**
	 * 最近执行时间
	 */
	private Long lastExecTime;
	/**
	 * 最近执行状态 0：成功 1：失败
	 */
	private Integer lastExecStatus;

	/**
	 * 描述\备注
	 */
	private String description;

	/**
	 * Gets the value of name
	 *
	 * @return the value of name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * <p>
	 * You can use getName() to get the value of name
	 * </p>
	 *
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the value of group
	 *
	 * @return the value of group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * Sets the group
	 * <p>
	 * You can use getGroup() to get the value of group
	 * </p>
	 *
	 * @param group group
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * Gets the value of targetClass
	 *
	 * @return the value of targetClass
	 */
	public String getTargetClass() {
		return targetClass;
	}

	/**
	 * Sets the targetClass
	 * <p>
	 * You can use getTargetClass() to get the value of targetClass
	 * </p>
	 *
	 * @param targetClass targetClass
	 */
	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}

	/**
	 * Gets the value of targetMethod
	 *
	 * @return the value of targetMethod
	 */
	public String getTargetMethod() {
		return targetMethod;
	}

	/**
	 * Sets the targetMethod
	 * <p>
	 * You can use getTargetMethod() to get the value of targetMethod
	 * </p>
	 *
	 * @param targetMethod targetMethod
	 */
	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	/**
	 * Gets the value of cronExpression
	 *
	 * @return the value of cronExpression
	 */
	public String getCronExpression() {
		return cronExpression;
	}

	/**
	 * Sets the cronExpression
	 * <p>
	 * You can use getCronExpression() to get the value of cronExpression
	 * </p>
	 *
	 * @param cronExpression cronExpression
	 */
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	/**
	 * Gets the value of status
	 *
	 * @return the value of status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status
	 * <p>
	 * You can use getStatus() to get the value of status
	 * </p>
	 *
	 * @param status status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the value of lastExecTime
	 *
	 * @return the value of lastExecTime
	 */
	public Long getLastExecTime() {
		return lastExecTime;
	}

	/**
	 * Sets the lastExecTime
	 * <p>
	 * You can use getLastExecTime() to get the value of lastExecTime
	 * </p>
	 *
	 * @param lastExecTime lastExecTime
	 */
	public void setLastExecTime(Long lastExecTime) {
		this.lastExecTime = lastExecTime;
	}

	/**
	 * Gets the value of lastExecStatus
	 *
	 * @return the value of lastExecStatus
	 */
	public Integer getLastExecStatus() {
		return lastExecStatus;
	}

	/**
	 * Sets the lastExecStatus
	 * <p>
	 * You can use getLastExecStatus() to get the value of lastExecStatus
	 * </p>
	 *
	 * @param lastExecStatus lastExecStatus
	 */
	public void setLastExecStatus(Integer lastExecStatus) {
		this.lastExecStatus = lastExecStatus;
	}

	/**
	 * Gets the value of description
	 *
	 * @return the value of description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description
	 * <p>
	 * You can use getDescription() to get the value of description
	 * </p>
	 *
	 * @param description description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the value of paramsType
	 *
	 * @return the value of paramsType
	 */
	public Class<?> getParamsType() {
		return paramsType;
	}

	/**
	 * Sets the paramsType
	 * <p>
	 * You can use setParamsType() to get the value of paramsType
	 * </p>
	 *
	 * @param paramsType paramsType
	 */
	public void setParamsType(Class<?> paramsType) {
		this.paramsType = paramsType;
	}

	/**
	 * Gets the value of params
	 *
	 * @return the value of params
	 */
	public Object getParams() {
		return params;
	}

	/**
	 * set the value of params
	 * 
	 * @param params 参数
	 */
	public void setParams(Object params) {
		this.params = params;
	}

	/**
	 * 简单调度周期，单位：分钟
	 * 
	 * @return int
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * 简单调度周期，单位：分钟
	 * 
	 * @param interval 简单调度周期，单位：分钟
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}
}
