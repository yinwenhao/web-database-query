/**
 * 
 */
package com.dhf.tools.action;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhf.tools.common.BaseAction;
import com.dhf.tools.common.MyResponseState;
import com.dhf.tools.common.SessionKeys;
import com.dhf.tools.dao.entity.User;
import com.dhf.tools.dao.mapper.UserMapper;
import com.dhf.tools.json.MyResponse;

/**
 * @author when_how
 * 
 */
//@Namespace("/login")
public class LoginAction extends BaseAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String account;
	
	private String password;
	
	@Autowired
	private UserMapper userMapper;

	/**
	 * 登陆页
	 * 
	 * @return
	 */
	@Action("/login")
	public String login() {
		return DEFAULT;
	}
	
	/**
	 * 退出登陆
	 * 
	 * @return
	 */
	@Action("/logout")
	public String logout() {
		getSession().clear();
		setVelocityLocation("/login.vm");
		return VELOCITY;
	}
	
	/**
	 * 登陆
	 * 
	 * @return
	 */
	@Action("/doLogin")
	public String doLogin() {
		MyResponse response = new MyResponse();
		User user = userMapper.getUserByAccount(getAccount());
		if (user == null) {
			response.setState(MyResponseState.ERROR_NO_SUCH_USER.getCode());
			response.setContent(MyResponseState.ERROR_NO_SUCH_USER.getMessage());
		} else if (!user.getPassword().equalsIgnoreCase(getPassword())) {
			response.setState(MyResponseState.ERROR_WRONG_PASSWORD.getCode());
			response.setContent(MyResponseState.ERROR_WRONG_PASSWORD.getMessage());
		} else {
			getSession().put(SessionKeys.USER, user);
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
