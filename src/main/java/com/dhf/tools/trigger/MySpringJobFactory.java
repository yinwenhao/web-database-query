package com.dhf.tools.trigger;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhf.tools.init.IniData;

/**
 *  spring-context-support包里的这个：org.springframework.scheduling.quartz.SpringBeanJobFactory不靠谱
 *  它也只是和quartz原来的一样new了个对象，而且没有注入
 *  所以还是用自己写的
 *  
 * @author yinwenhao
 *
 */
public class MySpringJobFactory implements JobFactory {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler)
			throws SchedulerException {
		JobDetail jobDetail = bundle.getJobDetail();
		Class<? extends Job> jobClass = jobDetail.getJobClass();
		try {
			if (log.isDebugEnabled()) {
				log.debug("Producing instance of Job '" + jobDetail.getKey()
						+ "', class=" + jobClass.getName());
			}
			return IniData.applicationContext.getBean(jobClass);
		} catch (Exception e) {
			SchedulerException se = new SchedulerException(
					"Problem instantiating class '"
							+ jobDetail.getJobClass().getName() + "'", e);
			throw se;
		}
	}

}
