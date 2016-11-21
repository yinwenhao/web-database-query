package com.dhf.tools.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Initialization implements ServletContextListener {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	/**
	 * 这个在struts2初始化之前执行，具体看日志
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		log.info("initialize start");
		
		log.info("initialize end");
	}

}
