package com.dhf.tools.action;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhf.tools.common.BaseAction;
import com.dhf.tools.common.MyResponseState;
import com.dhf.tools.common.TargetDatabases;
import com.dhf.tools.dao.entity.User;
import com.dhf.tools.dao.mapper.UserMapper;
import com.dhf.tools.json.MyResponse;

public class UserAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4187794715424336761L;
	private String account;
	private User user;
	private String permission;
	private Map<Integer, String> databaseList;
	@Autowired
	private UserMapper userMapper;
	private String currentUser;
	@Action("/updatePermission")
	public String updatePermission() {
		MyResponse response = new MyResponse();
		User user = userMapper.getUserByAccount(getAccount());
		setCurrentUser(getCurrentUserFromSession().getAccount());
		if (user == null) {
			response.setState(MyResponseState.ERROR_NO_SUCH_USER.getCode());
			response.setContent(MyResponseState.ERROR_NO_SUCH_USER.getMessage());
		}  else {
			user.setPermission(permission);
			userMapper.updatePermission(user);
			response.setState(MyResponseState.SUCCESS);
		}
		setResponse(response);
		return SUCCESS;
	}
	@Action("/editUser")
	public String editUser() {
		setCurrentUser(getCurrentUserFromSession().getAccount());
		User user = userMapper.getUserByAccount(account);
		setDatabaseList(TargetDatabases.databaseList);
		setUser(user);
		setVelocityLocation("/editUser.vm");
		return VELOCITY;
	}
	@Action("/deleteUser")
	public String deleteUser() {
		MyResponse response = new MyResponse();
		User user = userMapper.getUserByAccount(getAccount());
		setCurrentUser(getCurrentUserFromSession().getAccount());
		if (user == null) {
			response.setState(MyResponseState.ERROR_NO_SUCH_USER.getCode());
			response.setContent(MyResponseState.ERROR_NO_SUCH_USER.getMessage());
		}  else {
			userMapper.deleteUser(account);
			response.setState(MyResponseState.SUCCESS);
		}
		setResponse(response);
		return SUCCESS;
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
	public Map<Integer, String> getDatabaseList() {
	    return databaseList;
    }
	public void setDatabaseList(Map<Integer, String> databaseList) {
	    this.databaseList = databaseList;
    }
	public User getUser() {
	    return user;
    }
	public void setUser(User user) {
	    this.user = user;
    }
	public String getCurrentUser() {
	    return currentUser;
    }
	public void setCurrentUser(String currentUser) {
	    this.currentUser = currentUser;
    }

}
