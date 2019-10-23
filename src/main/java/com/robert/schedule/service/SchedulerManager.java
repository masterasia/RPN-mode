package com.robert.schedule.service;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.fastjson.JSON;
import com.weinan.base.common.schedule.Constant;
import com.weinan.base.common.schedule.ScheduleJob;
import com.weinan.base.common.schedule.annotation.Job;
import com.weinan.base.common.schedule.annotation.Task;
import com.weinan.base.common.schedule.bean.JobEntity;
import com.weinan.base.common.util.Const;
import com.weinan.base.common.util.Utils;

/**
 * 定时器调度服务
 *
 */
public class SchedulerManager implements ApplicationContextAware {

	private static Logger logger = LoggerFactory.getLogger(SchedulerManager.class);

	private Scheduler scheduler;

	/** 是否重新加载初始化Job */
	private boolean overwriteExistingJobs;

	/** 启动延迟时间 s */
	private int startupDelay;

	/**
	 * 设置应用上下文
	 * 
	 * @param context ApplicationContext
	 * @throws Exception 异常
	 */
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context ApplicationContext
	 */
	private void init(ApplicationContext context) {
		// 初始化加载
		logger.debug("Scheduler init!");
		try {
			// 启动延迟, 建议至少60s，因为在首次应用启动时会自动创建表，提供延时以避免QRTZ_相关表还未创建的时候就插入相关任务记录
			// noinspection AlibabaAvoidManuallyCreateThread
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(startupDelay * Const.ONE_THOUSAND);
					} catch (InterruptedException e1) {
					}
					try {
						// 初始化job
						initJobs(context);
						scheduler.start();
					} catch (SchedulerException e) {
						logger.error("Scheduler start fail!", e);
					}
				}
			});
			thread.setName("Quartz Scheduler [" + scheduler.getSchedulerName() + "]");
			thread.setDaemon(true);
			thread.start();
		} catch (Exception e) {
			logger.error("Scheduler init fail!", e);
		}
	}

	/**
	 * 初始化JOB，扫描类注解@Job根据方法上注解@Task添加
	 * 
	 * @param context ApplicationContext
	 */
	public void initJobs(ApplicationContext context) {
		final String cgLibString = "CGLIB$$";
		// 获取所有@Job注解类
		Map<String, Object> map = context.getBeansWithAnnotation(Job.class);
		map.forEach((k, v) -> {
			Class<? extends Object> clazz = v.getClass();
			if (clazz.getName().contains(cgLibString)) {
				// CGLIB代理
				clazz = clazz.getSuperclass();
			}
			Method[] methods = clazz.getDeclaredMethods();
			for (Method method : methods) {
				// 判断该方法是否有Task注解
				if (method.isAnnotationPresent(Task.class)) {
					Task task = method.getAnnotation(Task.class);
					JobEntity job = new JobEntity();
					// 默认name
					String name = task.name();
					if (Task.DEFAULT.equals(task.name())) {
						name = clazz.getName() + "_" + method.getName();
					}
					Class<?>[] types = method.getParameterTypes();
					job.setName(name);
					job.setGroup(task.group());
					job.setDescription(task.desc());
					if (task.cron().contains("$")) {
						// 从yml配置中获取cron表达式值
						String cronValue = task.cron();
						String convertCron = cronValue.substring(2, cronValue.length() - 1);
						String taskCron = context.getEnvironment().getProperty(convertCron);
						job.setCronExpression(taskCron);
					} else {
						job.setCronExpression(task.cron());
					}
					job.setTargetClass(clazz.getName());
					job.setTargetMethod(method.getName());
					// 参数类型
					if (null != types && types.length > 0) {
						job.setParamsType(types[0]);
					}
					try {
						// 判断是否需要加载到引擎，overwriteExistingJobs：true或者JobEntity不存在
						if (this.overwriteExistingJobs
								|| !scheduler.checkExists(JobKey.jobKey(job.getName(), job.getGroup()))) {
							logger.debug("Schedule job[name:{}, group:{}] is reloaded", job.getName(), job.getGroup());
							// 创建或者更新job
							createOrUpdate(job);
						}
					} catch (SchedulerException e) {
						logger.error("", e);
					} catch (IllegalArgumentException e) {
						logger.error("", e);
					}
				}
			}
		});
	}

	/**
	 * 获取所有任务
	 *
	 * @return List<SchedulerJob>
	 */
	public List<JobEntity> getJobList() {
		try {
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			List<JobEntity> jobList = new ArrayList<JobEntity>();
			for (JobKey jobKey : jobKeys) {
				JobDetail detail = scheduler.getJobDetail(jobKey);
				TriggerKey triggerKey = TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup());
				if (scheduler.checkExists(triggerKey)) {
					String json = detail.getJobDataMap().getString(Constant.JOB_PARAM_KEY);
					JobEntity job = JSON.parseObject(json, JobEntity.class);
					// 状态
					Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
					job.setStatus(triggerState.name());
					jobList.add(job);
				}
			}
			return jobList;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	/**
	 * 获取正在执行任务
	 *
	 * @return List<SchedulerJob>
	 */
	public List<JobEntity> getRunningJobList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 创建或更新 create
	 * 
	 * @param jobEntity JobEntity
	 */
	public void createOrUpdate(JobEntity jobEntity) {
		try {
			if (Utils.StringUtil.isEmpty(jobEntity.getCronExpression())) {
				createOrUpdateWithSimpleSchedule(jobEntity);
				return;
			}
			CronExpression cronExpression = new CronExpression(jobEntity.getCronExpression());
			if (!isValidExpression(cronExpression)) {
				return;
			}
			createOrUpdateWithCronSchedule(jobEntity);
		} catch (ParseException e) {
			logger.error("ParseException: jobEntity.getCronExpression() is error.", e);
			throw new IllegalArgumentException(e);
		} catch (SchedulerException e) {
			logger.error("SchedulerException: ", e);
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 创建或更新简单调度任务
	 * 
	 * @param jobEntity 任务类
	 * @throws SchedulerException 调度异常
	 */
	private void createOrUpdateWithSimpleSchedule(JobEntity jobEntity) throws SchedulerException {
		// 获取TriggerKey
		TriggerKey triggerKey = TriggerKey.triggerKey(jobEntity.getName(), jobEntity.getGroup());
		// 从数据库中查询触发器
		SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
		// 不存在则新建一个触发器
		if (null == trigger) {
			JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class)
					.withIdentity(jobEntity.getName(), jobEntity.getGroup()).storeDurably(true).build();
			// 参数赋值
			jobDetail.getJobDataMap().put(Constant.JOB_PARAM_KEY, JSON.toJSONString(jobEntity));
			// 表达式调度构建器
			SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInMinutes(jobEntity.getInterval()).repeatForever();
			// 按新的cronExpression表达式构建一个新的trigger
			trigger = TriggerBuilder.newTrigger().withIdentity(jobEntity.getName(), jobEntity.getGroup())
					.forJob(jobEntity.getName(), jobEntity.getGroup()).withSchedule(scheduleBuilder)
					.withDescription(jobEntity.getDescription()).build();

			scheduler.addJob(jobDetail, true);
			scheduler.scheduleJob(trigger);
		} else {
			// Trigger已存在，那么更新相应的定时设置
			// 简单调度构建器
			SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInMinutes(jobEntity.getInterval()).repeatForever();

			JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobEntity.getName(), jobEntity.getGroup()));
			jobDetail.getJobDataMap().put(Constant.JOB_PARAM_KEY, JSON.toJSONString(jobEntity));

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder)
					.withDescription(jobEntity.getDescription()).build();
			// 按新的trigger重新设置job执行
			scheduler.addJob(jobDetail, true);
			scheduler.rescheduleJob(triggerKey, trigger);
		}
		// 暂停
		if (Constant.PAUSED.equals(jobEntity.getStatus())) {
			pause(jobEntity.getName(), jobEntity.getGroup());
		}
	}

	/**
	 * 创建或更新Cron表达式调度任务
	 * 
	 * @param jobEntity 任务类
	 * @throws SchedulerException 调度异常
	 */
	private void createOrUpdateWithCronSchedule(JobEntity jobEntity) throws SchedulerException {
		// 获取TriggerKey
		TriggerKey triggerKey = TriggerKey.triggerKey(jobEntity.getName(), jobEntity.getGroup());
		// 从数据库中查询触发器
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		// 不存在则新建一个触发器
		if (null == trigger) {
			JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class)
					.withIdentity(jobEntity.getName(), jobEntity.getGroup()).storeDurably(true).build();
			// 参数赋值
			jobDetail.getJobDataMap().put(Constant.JOB_PARAM_KEY, JSON.toJSONString(jobEntity));
			// 表达式调度构建器
			CronScheduleBuilder cronBuilder = CronScheduleBuilder.cronSchedule(jobEntity.getCronExpression());
			// 按新的cronExpression表达式构建一个新的trigger
			trigger = TriggerBuilder.newTrigger().withIdentity(jobEntity.getName(), jobEntity.getGroup())
					.forJob(jobEntity.getName(), jobEntity.getGroup()).withSchedule(cronBuilder)
					.withDescription(jobEntity.getDescription()).build();
			scheduler.addJob(jobDetail, true);
			scheduler.scheduleJob(trigger);
		} else {
			// Trigger已存在，那么更新相应的定时设置
			// 表达式调度构建器
			CronScheduleBuilder cronBuilder = CronScheduleBuilder.cronSchedule(jobEntity.getCronExpression());
			JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobEntity.getName(), jobEntity.getGroup()));
			jobDetail.getJobDataMap().put(Constant.JOB_PARAM_KEY, JSON.toJSONString(jobEntity));

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronBuilder)
					.withDescription(jobEntity.getDescription()).build();
			// 按新的trigger重新设置job执行
			scheduler.addJob(jobDetail, true);
			scheduler.rescheduleJob(triggerKey, trigger);
		}
		// 暂停
		if (Constant.PAUSED.equals(jobEntity.getStatus())) {
			pause(jobEntity.getName(), jobEntity.getGroup());
		}
	}

	/**
	 * 立刻执行
	 *
	 * @param name  名称
	 * @param group 组
	 */
	public void exec(String name, String group) {
		try {
			scheduler.triggerJob(JobKey.jobKey(name, group));
		} catch (SchedulerException e) {
			logger.error("", e);
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 暂停 pause
	 *
	 * @param name  (name 为null，默认该组所有Job)
	 * @param group 组
	 */
	public void pause(String name, String group) {
		try {
			scheduler.pauseJob(JobKey.jobKey(name, group));
		} catch (SchedulerException e) {
			logger.error("", e);
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 恢复 resume
	 *
	 * @param name  (name 为null，默认该组所有Job)
	 * @param group 组
	 */
	public void resume(String name, String group) {
		try {
			scheduler.resumeJob(JobKey.jobKey(name, group));
		} catch (SchedulerException e) {
			logger.error("", e);
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 删除 remove
	 *
	 * @param name  名称
	 * @param group 组
	 */
	public void remove(String name, String group) {
		// 删除只删除触发器，JobDetail保留，为了控制初始化任务构建
		TriggerKey triggerKey = new TriggerKey(name, group);
		try {
			// 停止触发器
			scheduler.pauseTrigger(triggerKey);
			// 移除触发器
			if (!scheduler.unscheduleJob(triggerKey)) {
				throw new IllegalArgumentException("Remove Task fail!");
			}
		} catch (SchedulerException e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * cron表达式是否有效
	 * 
	 * @param cronExpression 表达式
	 * @return 判断结果
	 */
	private boolean isValidExpression(final CronExpression cronExpression) {

		CronTriggerImpl trigger = new CronTriggerImpl();
		trigger.setCronExpression(cronExpression);

		Date date = trigger.computeFirstFireTime(null);

		return date != null && date.after(new Date());
	}

	/**
	 * set scheduler
	 * 
	 * @param scheduler Scheduler
	 */
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * set overwriteExistingJobs
	 * 
	 * @param overwriteExistingJobs overwriteExistingJobs
	 */
	public void setOverwriteExistingJobs(boolean overwriteExistingJobs) {
		this.overwriteExistingJobs = overwriteExistingJobs;
	}

	/**
	 * set startupDelay
	 * 
	 * @param startupDelay startupDelay
	 */
	public void setStartupDelay(int startupDelay) {
		this.startupDelay = startupDelay;
	}

}
