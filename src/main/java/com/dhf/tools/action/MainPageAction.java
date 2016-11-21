/**
 * 
 */
package com.dhf.tools.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhf.tools.common.BaseAction;
import com.dhf.tools.common.SessionKeys;
import com.dhf.tools.common.TargetDatabases;
import com.dhf.tools.dao.entity.User;
import com.dhf.tools.service.QueryService;

/**
 * @author when_how
 * 
 */

public class MainPageAction extends BaseAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<Integer, String> databaseList;
	
	private String account;

	@Autowired
	private QueryService queryService;

	@Action("/mainPage")
	public String mainPage() {
		User user = (User) getSession().get(SessionKeys.USER);
		Map<Integer, String> m = new HashMap<Integer, String>();
		for (int i : user.getPermissionList()) {
			if (TargetDatabases.databaseList.containsKey(i)) {
				m.put(i, TargetDatabases.databaseList.get(i));
			}
		}
		setDatabaseList(m);
		setAccount(user.getAccount());
		setVelocityLocation("/mainPage.vm");
		return VELOCITY;
	}

	public Map<Integer, String> getDatabaseList() {
		return databaseList;
	}

	public void setDatabaseList(Map<Integer, String> databaseList) {
		this.databaseList = databaseList;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

}
