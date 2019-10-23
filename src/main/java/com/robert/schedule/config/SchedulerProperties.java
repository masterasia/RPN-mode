package com.robert.schedule.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.weinan.base.common.util.Const;

/**
 * scheduler配置
 * 
 *
 */
@ConfigurationProperties("scheduler")
public class SchedulerProperties {

	/** 是否启动Scheduler */
	private Boolean enabled;
	/** 是否重新加载初始化Job，默认重新加载 */
	private Boolean overwriteExistingJobs = true;
	/** 应用实例名 集群中每一个实例都必须使用相同的名称 */
	private String instanceName;
	/** 数据库表面前缀 */
	private String tablePrefix;
	/** 是否加入集群 */
	private Boolean isClustered;
	/** 线程数 */
	private Integer threadCount;
	/** 集群校验时间 */
	private Integer clusterCheckinInterval;
	/**
	 * 启动延迟时间 s; 默认60s<br>
	 * 建议至少60s，因为在首次应用启动时会自动创建表，提供延时以避免QRTZ_相关表还未创建的时候就插入相关任务记录
	 */
	private Integer startupDelay = Const.SIXTY;

	/**
	 * Gets the value of enabled
	 *
	 * @return the value of enabled
	 */
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * Sets the enabled
	 * <p>
	 * You can use getEnabled() to get the value of enabled
	 * </p>
	 *
	 * @param enabled
	 *            enabled
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * get the overwriteExistingJobs
	 * <p>
	 * You can use getEnabled() to get the value of enabled
	 * </p>
	 * 
	 * @return overwriteExistingJobs
	 */
	public Boolean getOverwriteExistingJobs() {
		return overwriteExistingJobs;
	}

	/**
	 * set the overwriteExistingJobs
	 * <p>
	 * You can use setOverwriteExistingJobs() to get the value of
	 * overwriteExistingJobs
	 * </p>
	 * 
	 * @param overwriteExistingJobs
	 *            overwriteExistingJobs
	 */
	public void setOverwriteExistingJobs(Boolean overwriteExistingJobs) {
		this.overwriteExistingJobs = overwriteExistingJobs;
	}

	/**
	 * Gets the value of instanceName
	 *
	 * @return the value of instanceName
	 */
	public String getInstanceName() {
		return instanceName;
	}

	/**
	 * Sets the instanceName
	 * <p>
	 * You can use getInstanceName() to get the value of instanceName
	 * </p>
	 *
	 * @param instanceName
	 *            instanceName
	 */
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	/**
	 * Gets the value of tablePrefix
	 *
	 * @return the value of tablePrefix
	 */
	public String getTablePrefix() {
		return tablePrefix;
	}

	/**
	 * Sets the tablePrefix
	 * <p>
	 * You can use getTablePrefix() to get the value of tablePrefix
	 * </p>
	 *
	 * @param tablePrefix
	 *            tablePrefix
	 */
	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}

	/**
	 * Gets the value of isClustered
	 *
	 * @return the value of isClustered
	 */
	public Boolean getIsClustered() {
		return isClustered;
	}

	/**
	 * Sets the isClustered
	 * <p>
	 * You can use setIsClustered() to get the value of isClustered
	 * </p>
	 *
	 * @param isClustered
	 *            isClustered
	 */
	public void setIsClustered(Boolean isClustered) {
		this.isClustered = isClustered;
	}

	/**
	 * Gets the value of threadCount
	 *
	 * @return the value of threadCount
	 */
	public Integer getThreadCount() {
		return threadCount;
	}

	/**
	 * Sets the threadCount
	 * <p>
	 * You can use getThreadCount() to get the value of threadCount
	 * </p>
	 *
	 * @param threadCount
	 *            threadCount
	 */
	public void setThreadCount(Integer threadCount) {
		this.threadCount = threadCount;
	}

	/**
	 * Gets the value of clusterCheckinInterval
	 *
	 * @return the value of clusterCheckinInterval
	 */
	public Integer getClusterCheckinInterval() {
		return clusterCheckinInterval;
	}

	/**
	 * Sets the clusterCheckinInterval
	 * <p>
	 * You can use getClusterCheckinInterval() to get the value of
	 * clusterCheckinInterval
	 * </p>
	 *
	 * @param clusterCheckinInterval
	 *            clusterCheckinInterval
	 */
	public void setClusterCheckinInterval(Integer clusterCheckinInterval) {
		this.clusterCheckinInterval = clusterCheckinInterval;
	}

	/**
	 * Gets the value of startupDelay
	 *
	 * @return the value of startupDelay
	 */
	public Integer getStartupDelay() {
		return startupDelay;
	}

	/**
	 * Sets the startupDelay
	 * <p>
	 * You can use getStartupDelay() to get the value of startupDelay
	 * </p>
	 *
	 * @param startupDelay
	 *            startupDelay
	 */
	public void setStartupDelay(Integer startupDelay) {
		this.startupDelay = startupDelay;
	}
}
