package com.robert.schedule;

import java.lang.reflect.Method;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.alibaba.fastjson.JSON;
import com.weinan.base.common.schedule.bean.JobEntity;
import com.weinan.base.common.schedule.bean.JobLog;
import com.weinan.base.common.schedule.config.SchedulerConfig;
import com.weinan.base.common.util.Utils;
import com.weinan.base.configurer.Configurer;

/**
 * 定时任务作业类。
 * 
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@ConditionalOnProperty(prefix = "scheduler", value = "enabled", havingValue = "true")
public class ScheduleJob extends QuartzJobBean {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleJob.class);

	/**
	 * 定时执行
	 * 
	 * @param context
	 *            JobExecutionContext
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		long now = System.currentTimeMillis();
		JobEntity job = null;
		try {
			String jsonParams = context.getJobDetail().getJobDataMap().getString(Constant.JOB_PARAM_KEY);
			job = JSON.parseObject(jsonParams, JobEntity.class);
			job.setLastExecTime(now);
			Object otargetObject = SchedulerConfig.get(job.getTargetClass());
			// 判断方式是否有参数
			if (null == job.getParamsType()) {
				// 无参数
				Method m = otargetObject.getClass().getMethod(job.getTargetMethod(), new Class[] {});
				m.invoke(otargetObject);
			} else {
				// 有参数
				Method m = otargetObject.getClass().getMethod(job.getTargetMethod(),
						new Class[] { job.getParamsType() });
				if (job.getParamsType().equals(JobExecutionContext.class)) {
					// 上下文参数
					m.invoke(otargetObject, new Object[] { context });
				} else {
					// 普通参数
					Object params = job.getParams();
					m.invoke(otargetObject, params);
				}
			}
			job.setLastExecStatus(Constant.SUCCESS);
			// 添加日志
			log(job, null);
		} catch (Exception e) {
			// 添加日志
			if (null != job) {
				job.setLastExecStatus(Constant.FAIL);
				String error = e.getMessage();
				if (Utils.StringUtil.isBlank(error)) {
					error = e.getCause().getMessage();
				}
				log(job, error);
			}
			throw new JobExecutionException(e);
		} finally {
			// 放回上下文
			context.getJobDetail().getJobDataMap().put(Constant.JOB_PARAM_KEY, JSON.toJSONString(job));
		}
	}

	/**
	 * 记录日志
	 * 
	 * @param job
	 *            任务
	 * @param errorMsg
	 *            错误信息
	 */
	private void log(JobEntity job, String errorMsg) {
		JobLog log = new JobLog();

		log.setAppName(Configurer.appName());
		log.setCreateTime(job.getLastExecTime());
		log.setJobGroup(job.getGroup());
		log.setJobName(job.getName());
		// 参数
		log.setParams(JSON.toJSONString(job.getParams()));
		log.setTargetClass(job.getTargetClass());
		log.setTargetMethod(job.getTargetMethod());
		log.setStatus(job.getLastExecStatus());
		log.setError(errorMsg);
		long t = System.currentTimeMillis() - job.getLastExecTime();
		log.setTimes((int) t);

		// TODO 后续完善日志持久化和报警功能
		if (!log.success()) {
//			logger.info(log.toString());
//		} else {
			logger.error(log.toString());
		}
	}

}