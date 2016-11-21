package com.dhf.tools.init;

import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dhf.tools.trigger.JobManager;
import com.dhf.tools.trigger.job.JobLoadConfigFromDatabase;

public class IniData extends HttpServlet {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	public static ApplicationContext applicationContext;

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 这个是最后执行的，具体看日志
	 */
	@Override
	public void init() throws ServletException {
		log.info("initialize start");
		applicationContext= WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		
		// 加载数据库的配置
		applicationContext.getBean(JobLoadConfigFromDatabase.class).loadDatabaseConfig();
		
		try {
			// 启动定时任务
			JobManager.initJobTrigger();
		} catch (SchedulerException e) {
			log.error("JobManager.initJobTrigger error.", e);
		} catch (ParseException e) {
			log.error("JobManager.initJobTrigger error.", e);
		}
		log.info("initialize end");
	}
	
}
