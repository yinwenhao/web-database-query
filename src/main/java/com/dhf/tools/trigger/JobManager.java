package com.dhf.tools.trigger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhf.tools.trigger.job.JobLoadConfigFromDatabase;

public class JobManager {

	/** log */
	private static final Logger log = LoggerFactory.getLogger(JobManager.class);

	/** 定时从数据库载入一些配置 */
	public static final String JOB_LOAD_CONFIG_FROM_DATABASE = "JOB_LOAD_CONFIG_FROM_DATABASE";

	/**
	 * 初始化JobList
	 * 
	 * @return
	 */
	private static List<MyJobCron> initJobList() {
		log.info("init job list start");
		List<MyJobCron> jobList = new ArrayList<MyJobCron>();

		jobList.add(new MyJobCron(JOB_LOAD_CONFIG_FROM_DATABASE,
				JobLoadConfigFromDatabase.class, "0 0/1 * ? * * *"));

		log.info("init job list end");
		return jobList;
	}

	/**
	 * 容器启动时初始化任务
	 * 
	 * @param jobList
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void initJobTrigger()
			throws SchedulerException, ParseException {
		log.info("scheduler job start");

		// 初始化工厂
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();

		for (MyJobCron job : initJobList()) {
			JobDetail jobDetail = JobBuilder.newJob(job.getJobClass())
					.withIdentity(job.getJobName(), Scheduler.DEFAULT_GROUP)
					.build();
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withSchedule(
							CronScheduleBuilder.cronSchedule(job
									.getCronExpression())).build();
			scheduler.scheduleJob(jobDetail, trigger);

			log.info("scheduler job: [" + jobDetail.getKey() + "]");
			
		}

		// 任务启动
		scheduler.start();

		log.info("scheduler job end");
	}

}
