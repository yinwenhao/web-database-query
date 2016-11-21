package com.dhf.tools.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhf.tools.common.BaseAction;
import com.dhf.tools.common.SessionKeys;
import com.dhf.tools.dao.entity.Log;
import com.dhf.tools.dao.entity.User;
import com.dhf.tools.dao.mapper.LogMapper;

public class LogManagementAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4187794715424336761L;
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private String currentUser;
	private String userName;
	
	private String startDate;
	private String endDate;
	private List<Log> logList;
	
	@Autowired
	private LogMapper logMapper;
	@Action("/logManagement")
	public String userManagement() {
		User user = (User) getSession().get(SessionKeys.USER);
		if(userName==null){
			userName="";
		}
		if(startDate==null){
			Calendar now = Calendar.getInstance();
			now.add(Calendar.DATE, -7);
		    startDate=new SimpleDateFormat("yyyy-MM-dd").format(now.getTime());
		}
		if(endDate==null){
			endDate="";
		}
		setLogList(logMapper.queryLog(userName,startDate,endDate));	
		setCurrentUser(user.getAccount());
		setVelocityLocation("/logManagement.vm");
		return VELOCITY;
	}

	@Action("/searchLog")
	public String searchLog() {
		User user = (User) getSession().get(SessionKeys.USER);
		String endDateWithTime="";
		if(StringUtils.isNotEmpty(endDate)){
			endDateWithTime=endDate+" 23:59:59";
		}
		setLogList(logMapper.queryLog(userName,startDate,endDateWithTime));	
		setCurrentUser(user.getAccount());
		setVelocityLocation("/logManagement.vm");
		return VELOCITY;
	}


	public String getCurrentUser() {
	    return currentUser;
    }

	public void setCurrentUser(String currentUser) {
	    this.currentUser = currentUser;
    }

	public String getUserName() {
	    return userName;
    }

	public void setUserName(String userName) {
	    this.userName = userName;
    }

	public String getStartDate() {
	    return startDate;
    }

	public void setStartDate(String startDate) {
	    this.startDate = startDate;
    }

	public String getEndDate() {
	    return endDate;
    }

	public void setEndDate(String endDate) {
	    this.endDate = endDate;
    }

	public List<Log> getLogList() {
	    return logList;
    }

	public void setLogList(List<Log> logList) {
	    this.logList = logList;
    }

}
