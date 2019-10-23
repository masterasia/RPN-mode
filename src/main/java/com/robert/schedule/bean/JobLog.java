package com.robert.schedule.bean;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.weinan.base.common.schedule.Constant;

/**
 * 任务日志
 * 
 *
 */
public class JobLog implements Serializable {

	private static final long serialVersionUID = -1065576949505096310L;

	/**
	 * 执行应用名称
	 */
	private String appName;

	/**
	 * 任务名称
	 */
	private String jobName;

	/**
	 * 任务组
	 */
	private String jobGroup;

	/**
	 * 目标类
	 */
	private String targetClass;

	/**
	 * 目标方法
	 */
	private String targetMethod;

	/**
	 * 参数
	 */
	private String params;

	/**
	 * 任务状态, 0:成功, 1:失败
	 */
	private Integer status;

	/**
	 * 失败信息
	 */
	private String error;

	/**
	 * 耗时(单位：毫秒)
	 */
	private Integer times;

	/**
	 * 创建时间
	 */
	private Long createTime;

	/**
	 * Gets the value of appName
	 *
	 * @return the value of appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * Sets the appName
	 * <p>You can use getAppName() to get the value of appName</p>
	 *
	 * @param appName appName
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * Gets the value of jobName
	 *
	 * @return the value of jobName
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * Sets the jobName
	 * <p>You can use getJobName() to get the value of jobName</p>
	 *
	 * @param jobName jobName
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * Gets the value of jobGroup
	 *
	 * @return the value of jobGroup
	 */
	public String getJobGroup() {
		return jobGroup;
	}

	/**
	 * Sets the jobGroup
	 * <p>You can use getJobGroup() to get the value of jobGroup</p>
	 *
	 * @param jobGroup jobGroup
	 */
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
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
	 * <p>You can use getTargetClass() to get the value of targetClass</p>
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
	 * <p>You can use getTargetMethod() to get the value of targetMethod</p>
	 *
	 * @param targetMethod targetMethod
	 */
	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	/**
	 * Gets the value of params
	 *
	 * @return the value of params
	 */
	public String getParams() {
		return params;
	}

	/**
	 * Sets the params
	 * <p>You can use getParams() to get the value of params</p>
	 *
	 * @param params params
	 */
	public void setParams(String params) {
		this.params = params;
	}

	/**
	 * Gets the value of status 任务状态, 0:成功, 1:失败
	 *
	 * @return the value of status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * Sets the status 任务状态, 0:成功, 1:失败
	 * <p>You can use getStatus() to get the value of status</p>
	 *
	 * @param status status 任务状态, 0:成功, 1:失败
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * Gets the value of error
	 *
	 * @return the value of error
	 */
	public String getError() {
		return error;
	}

	/**
	 * Sets the error
	 * <p>You can use getError() to get the value of error</p>
	 *
	 * @param error error
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Gets the value of times
	 *
	 * @return the value of times
	 */
	public Integer getTimes() {
		return times;
	}

	/**
	 * Sets the times
	 * <p>You can use getTimes() to get the value of times</p>
	 *
	 * @param times times
	 */
	public void setTimes(Integer times) {
		this.times = times;
	}

	/**
	 * Gets the value of createTime
	 *
	 * @return the value of createTime
	 */
	public Long getCreateTime() {
		return createTime;
	}

	/**
	 * Sets the createTime
	 * <p>You can use getCreateTime() to get the value of createTime</p>
	 *
	 * @param createTime createTime
	 */
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	/**
	 * 本次调度任务运行是否成功
	 * @return boolean
	 */
	public boolean success() {
		return this.status == Constant.SUCCESS;
	}
	
	/**
	 * toString 重载
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
