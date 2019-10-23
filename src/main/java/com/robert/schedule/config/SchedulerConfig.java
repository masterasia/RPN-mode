package com.robert.schedule.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.StringUtils;

import com.weinan.base.common.schedule.service.SchedulerManager;

/**
 * 定时器配置
 * 
 * <pre>
 * 分布式集群定时器，实现功能：
 * 1、相同任务，同时只能一个服务执行；
 * 2、多个任务在多个服务下均衡分配；
 * 3、一个服务断开，在此服务上运行的任务将分派给其他服务执行；
 * 4、通过注解初始化任务入库；
 * 5、任务支持在线增删改查，数据需要落地；
 * 6、任务执行需要日志记录，并提供查看
 * 
 * 	配置文件中必须配置了
 * 	scheduler.enabled：true
 *  才能获取到实例scheduler和schedulerManager实例
 * </pre>
 * 
 */
@Configuration
@ConditionalOnProperty(prefix = "scheduler", value = "enabled", havingValue = "true")
@EnableConfigurationProperties(SchedulerProperties.class)
public class SchedulerConfig {

	private static final String INSTANCE_NAME = "DistributedScheduler";

	private static ApplicationContext ctx;

	@Autowired
	private SchedulerProperties properties;

	/**
	 * 设置 ApplicationContext 静态方法
	 * 
	 * @param ctx
	 *            ApplicationContext
	 */
	public static void setApplicationContext(ApplicationContext ctx) {
		SchedulerConfig.ctx = ctx;
	}

	/**
	 * 设置 ApplicationContext
	 * 
	 * @param ctx
	 *            ApplicationContext
	 */
	public void setAppContext(ApplicationContext ctx) {
		setApplicationContext(ctx);
	}

	/**
	 * Scheduler 纳入spring管理
	 * 
	 * @param ac
	 *            ApplicationContext
	 * @param ds
	 *            DataSource
	 * @return SchedulerFactoryBean
	 */
	@Bean
	@ConditionalOnMissingBean
	public SchedulerFactoryBean schedulerFactory(ApplicationContext ac, DataSource ds) {

		SchedulerFactoryBean bean = new SchedulerFactoryBean();
		// 用于quartz集群,QuartzScheduler 启动时更新己存在的Job,false不更新
		bean.setOverwriteExistingJobs(false);
		// 定义定时器名 集群一致
		bean.setBeanName(val(properties.getInstanceName(), INSTANCE_NAME));
		// 不自动启动
		bean.setAutoStartup(false);
		// 加载quartz配置
		bean.setQuartzProperties(quartzProperties());
		// 数据源
		bean.setDataSource(ds);
		// spring 上下文
		bean.setApplicationContext(ac);
		setAppContext(ac);
		return bean;
	}

	/**
	 * SchedulerManager 纳入spring管理
	 * 
	 * @param scheduler
	 *            Scheduler
	 * @return SchedulerManager
	 */
	@Bean
	@ConditionalOnMissingBean
	public SchedulerManager schedulerManager(Scheduler scheduler) {
		SchedulerManager manager = new SchedulerManager();
		manager.setScheduler(scheduler);
		manager.setOverwriteExistingJobs(properties.getOverwriteExistingJobs());
		manager.setStartupDelay(properties.getStartupDelay());
		return manager;
	}

	/**
	 * get bean
	 * 
	 * @param beanName
	 *            beanName
	 * @return Object
	 */
	public static Object get(String beanName) {
		try {
			return ctx.getBean(Class.forName(beanName));
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * quartzProperties
	 * 
	 * @return Properties
	 */
	private Properties quartzProperties() {
		final int clusterCheckinInterval = 20000;
		final int threadCount = 10;
		Properties pro = new Properties();
		pro.setProperty("org.quartz.scheduler.instanceName", val(properties.getInstanceName(), INSTANCE_NAME));
		pro.setProperty("org.quartz.scheduler.instanceId", "AUTO");
		pro.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
		pro.setProperty("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
		pro.setProperty("org.quartz.jobStore.tablePrefix", val(properties.getTablePrefix(), "QRTZ_"));
		pro.setProperty("org.quartz.jobStore.isClustered", val(properties.getIsClustered(), true));
		pro.setProperty("org.quartz.jobStore.useProperties", "false");
		pro.setProperty("org.quartz.jobStore.clusterCheckinInterval",
				val(properties.getClusterCheckinInterval(), clusterCheckinInterval));
		pro.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
		pro.setProperty("org.quartz.threadPool.threadCount", val(properties.getThreadCount(), threadCount));
		pro.setProperty("org.quartz.threadPool.threadPriority", "5");
		pro.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
		return pro;
	}

	/**
	 * val
	 * 
	 * @param det
	 *            det
	 * @param src
	 *            src
	 * @return String
	 */
	public String val(Object src, Object det) {
		if (StringUtils.isEmpty(src)) {
			return String.valueOf(det);
		}
		return String.valueOf(src);
	}
}
