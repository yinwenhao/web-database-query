package com.dhf.tools.interceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhf.tools.common.BaseAction;
import com.dhf.tools.common.MyResponseState;
import com.dhf.tools.common.SessionKeys;
import com.dhf.tools.dao.entity.User;
import com.dhf.tools.json.MyResponse;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class AuthInterceptor implements Interceptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static Map<String, Integer> map = new HashMap<String, Integer>();
	private static Map<String, Integer> adminMap = new HashMap<String, Integer>();

	// 初始化Set
	static {
		// 不需要验证登陆的，数字为：0
		map.put("LoginAction", 0);
		map.put("RegisterAction", 0);
		map.put("AdminLoginAction", 0);
		adminMap.put("UserManagementAction", -1);
		adminMap.put("UserAction", -1);
		adminMap.put("DatabaseManagementAction", -1);
		adminMap.put("DatabaseAction", -1);
		adminMap.put("LogManagementAction", -1);
		// 部分方法不需要验证登陆的，数字为：1
		
		// 需要验证ip的，数字为：2
		
		// 部分方法需要验证ip的，数字为：3
		
	}
	
	/**
	 *  部分方法不需要验证登陆的
	 */
	private static Map<String, Set<String>> methodMap = new HashMap<String, Set<String>>();
	
	static {
		
	}
	
	/**
	 *  部分方法需要验证ip的
	 */
	private static Map<String, Set<String>> methodMapIp = new HashMap<String, Set<String>>();
	
	static {
		
	}

	@Override
	public void destroy() {
		log.info("AuthInterceptor destroy");
	}

	@Override
	public void init() {
		log.info("AuthInterceptor init");
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String actionName = invocation.getAction().getClass().getSimpleName();
		String methodName = invocation.getProxy().getMethod();
		Map<String, Object> session = invocation.getInvocationContext().getSession();
		if (map.containsKey(actionName)) {
			// 可能不需要验证登陆
			if (map.get(actionName) == 0) {
				// 不需要验证登陆
				return invocation.invoke();
			} else if (map.get(actionName) == 1) {
				// 部分方法不需要验证登陆的
				if (methodMap.get(actionName).contains(methodName)) {
					// 不需要验证登陆
					return invocation.invoke();
				}
			}
		} else {
			// 需要验证登陆
			if(isAdmin(actionName)){
				if (session.containsKey(SessionKeys.USER)) {
					User user=(User) session.get(SessionKeys.USER);
					if(user.getPermission_level()==1){
						return invocation.invoke();
					}
				}
				return BaseAction.ADMIN_LOGIN;
				
			}else{
				if (session.containsKey(SessionKeys.USER)) {
					// 已经登陆，正常访问
					return invocation.invoke();
				}
			}
		}
		if("query".equals(methodName)||"hbaseQuery".equals(methodName)||"descTable".equals(methodName)){
			MyResponse response = new MyResponse(MyResponseState.ERROR_SESSION_TIMEOUT.getCode(), MyResponseState.ERROR_SESSION_TIMEOUT.getMessage());
			((BaseAction) invocation.getAction()).setResponse(response);
			return BaseAction.SESSION_TIMEOUT;
		}
		// 需要登陆，返回到登陆页
		return BaseAction.DEFAULT;
	}
	private boolean isAdmin(String actionName){
		if(adminMap.containsKey(actionName)){
			return true;
		}
		return false;
	}
	/**
	 * 获得真实IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("Cdn-Src-Ip");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
}
