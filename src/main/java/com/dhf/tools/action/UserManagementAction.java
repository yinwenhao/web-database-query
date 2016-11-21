package com.dhf.tools.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhf.tools.common.BaseAction;
import com.dhf.tools.common.SessionKeys;
import com.dhf.tools.dao.entity.User;
import com.dhf.tools.dao.mapper.UserMapper;

public class UserManagementAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4187794715424336761L;
	private String currentUser;
	private String account;
	
	private String permission;
	private List<User> userList;
	
	@Autowired
	private UserMapper userMapper;
	@Action("/userManagement")
	public String userManagement() {
		User user = (User) getSession().get(SessionKeys.USER);
		setUserList(userMapper.getUserList());
		setAccount(user.getAccount());
		setCurrentUser(user.getAccount());
		setVelocityLocation("/userManagement.vm");
		return VELOCITY;
	}
	@Action("/queryUser")
	public String queryUser() {
		User user = (User) getSession().get(SessionKeys.USER);
		userList=new ArrayList<User>();
		userList.add(userMapper.getUserByAccount(account));
		setAccount(user.getAccount());
		setCurrentUser(user.getAccount());
		setVelocityLocation("/userManagement.vm");
		return VELOCITY;
	}
	public String getAccount() {
	    return account;
    }
	public void setAccount(String account) {
	    this.account = account;
    }
	public String getPermission() {
	    return permission;
    }
	public void setPermission(String permission) {
	    this.permission = permission;
    }
	public List<User> getUserList() {
	    return userList;
    }
	public void setUserList(List<User> userList) {
	    this.userList = userList;
    }

	public String getCurrentUser() {
	    return currentUser;
    }

	public void setCurrentUser(String currentUser) {
	    this.currentUser = currentUser;
    }

}
