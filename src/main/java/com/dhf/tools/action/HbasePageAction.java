/**
 * 
 *//*
package com.dhf.tools.action;

import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhf.hbase.core.HBaseDaoTemplate;
import com.dhf.tools.common.BaseAction;
import com.dhf.tools.common.SessionKeys;
import com.dhf.tools.dao.entity.User;
import com.dhf.tools.service.QueryService;

*//**
 * @author longyaokun
 *
 *//*
public class HbasePageAction extends BaseAction {

	*//**
	 * serialVersionUID
	 *//*
	private static final long serialVersionUID = 1L;

	private List<String> tableList;

	private String account;

	@Autowired
	private QueryService queryService;

	@Autowired
	private HBaseDaoTemplate hbaseDaoTemplate;

	@Action("/hbasePage")
	public String hbasePage() {
		User user = (User) getSession().get(SessionKeys.USER);

		setTableList(hbaseDaoTemplate.listTables());
		setAccount(user.getAccount());
		setVelocityLocation("/hbasePage.vm");
		return VELOCITY;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public List<String> getTableList() {
		return tableList;
	}

	public void setTableList(List<String> tableList) {
		this.tableList = tableList;
	}

}
*/