package com.dhf.tools.trigger;

import org.quartz.Job;

/**
 * Job定义类
 * 
 * @author when_how
 *
 */
public class MyJobCron {

	/** jobName */
	private String jobName;
	
	/** cronExpression */
	private String cronExpression;
	
	private Class<? extends Job> jobClass;

	/**
	 * 构造函数
	 * 
	 * @param jobName
	 * @param cronExpression
	 */
	public MyJobCron(String jobName, Class<? extends Job> jobClass, String cronExpression) {
		setJobName(jobName);
		setJobClass(jobClass);
		setCronExpression(cronExpression);
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Class<? extends Job> getJobClass() {
		return jobClass;
	}

	public void setJobClass(Class<? extends Job> jobClass) {
		this.jobClass = jobClass;
	}

}
