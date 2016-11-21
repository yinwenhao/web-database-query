/**
 * 
 */
package com.dhf.tools.action;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhf.tools.common.BaseAction;
import com.dhf.tools.common.MD5Util;
import com.dhf.tools.common.MyConstants;
import com.dhf.tools.common.MyResponseState;
import com.dhf.tools.dao.entity.User;
import com.dhf.tools.dao.mapper.UserMapper;
import com.dhf.tools.json.MyResponse;

/**
 * @author when_how
 * 
 */
// @Namespace("/login")
public class RegisterAction extends BaseAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String account;

	private String password;

	@Autowired
	private UserMapper userMapper;

	/**
	 * 注册
	 * 
	 * @return
	 */
	@Action("/register")
	public String register() {
		MyResponse response = new MyResponse();
		if (getAccount() == null
				|| getAccount().length() < MyConstants.ACCOUNT_LENGTH_MIN
				|| getAccount().length() > MyConstants.ACCOUNT_LENGTH_MAX) {
			response.setState(MyResponseState.ERROR_REGISTER_ACCOUNT.getCode());
		} else if (getPassword() == null
				|| getPassword().length() < MyConstants.PASSWORD_LENGTH_MIN
				|| getPassword().length() > MyConstants.PASSWORD_LENGTH_MAX) {
			response.setState(MyResponseState.ERROR_REGISTER_PASSWORD.getCode());
		} else {
			User user = userMapper.getUserByAccount(getAccount());
			if (user == null) {
				// 创建新用户
				user = new User();
				user.setAccount(account);
				user.setPassword(MD5Util.MD5(password));
				userMapper.insertUser(user);
				response.setState(MyResponseState.SUCCESS);
			} else {
				response.setState(MyResponseState.ERROR_USER_EXIST.getCode());
			}
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
