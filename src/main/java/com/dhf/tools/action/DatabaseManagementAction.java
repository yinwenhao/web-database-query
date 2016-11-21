package com.dhf.tools.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhf.tools.common.BaseAction;
import com.dhf.tools.common.SessionKeys;
import com.dhf.tools.dao.entity.Database;
import com.dhf.tools.dao.entity.User;
import com.dhf.tools.dao.mapper.DatabaseMapper;

public class DatabaseManagementAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4187794715424336761L;
	private String currentUser;
	private List<Database> databaseList;
	
	@Autowired
	private DatabaseMapper databaseMapper;
	@Action("/dbManagement")
	public String databaseManagement() {
		User user = (User) getSession().get(SessionKeys.USER);
		setCurrentUser(user.getAccount());
		setDatabaseList(databaseMapper.getDatabaseList());
		setVelocityLocation("/databaseManagement.vm");
		return VELOCITY;
	}
	public List<Database> getDatabaseList() {
	    return databaseList;
    }
	public void setDatabaseList(List<Database> databaseList) {
	    this.databaseList = databaseList;
    }
	public String getCurrentUser() {
	    return currentUser;
    }
	public void setCurrentUser(String currentUser) {
	    this.currentUser = currentUser;
    }

}
