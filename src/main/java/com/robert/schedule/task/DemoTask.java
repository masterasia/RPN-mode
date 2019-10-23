package com.robert.schedule.task;


import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Value;


/**
 * Task demo
 * <pre>
 *  定义name和group，方法参数job上下文
 * </pre>
 *
 */
// @Job
public class DemoTask {

    @Value("${spring.application.name}")
    private String appname;

    /**
     * 拦截器具体实现
     *
     * @param context Job运行的上下文
     */
   //  @Task(name = "测试任务", group = "测试组", cron = "0/10 * * ? * * *")
    public void sayHello(JobExecutionContext context) {
        System.out.println("====    ScheduleTask 123456789    ====" + appname);
    }
}