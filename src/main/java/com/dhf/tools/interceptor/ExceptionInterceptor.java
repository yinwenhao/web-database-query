package com.dhf.tools.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhf.tools.common.BaseAction;
import com.dhf.tools.common.MyResponseState;
import com.dhf.tools.json.MyResponse;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class ExceptionInterceptor implements Interceptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void destroy() {
		log.info("ExceptionInterceptor destroy");
	}

	@Override
	public void init() {
		log.info("ExceptionInterceptor init");
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
        long startTime = System.currentTimeMillis();
		try {
			return invocation.invoke();
//		} catch (JSQLParserException e) {
//			MyResponse response = new MyResponse(MyResponseState.ERROR, e.getMessage());
//			((BaseAction) invocation.getAction()).setResponse(response);
//			log.error("JSQLParserException", e);
//			return BaseAction.ERROR;
//		} catch (SQLException e) {
//			MyResponse response = new MyResponse(MyResponseState.ERROR, e.getMessage());
//			((BaseAction) invocation.getAction()).setResponse(response);
//			log.error("sql error.", e);
//			return BaseAction.ERROR;
//		} catch (JsonProcessingException e) {
//			MyResponse response = new MyResponse(MyResponseState.ERROR, "json转换失败了...");
//			((BaseAction) invocation.getAction()).setResponse(response);
//			log.error("json error.", e);
//			return BaseAction.ERROR;
		} catch (Throwable e) {
			MyResponse response = new MyResponse(MyResponseState.ERROR, e.getMessage());
			((BaseAction) invocation.getAction()).setResponse(response);
			log.error("ExceptionInterceptor catch an error.", e);
			return BaseAction.ERROR;
		} finally {
	        long executionTime = System.currentTimeMillis() - startTime;
	        StringBuilder message = new StringBuilder(100);
	        message.append("Executed action [");
	        message.append(invocation.getProxy().getActionName());
	        message.append("!");
	        message.append(invocation.getProxy().getMethod());
	        HttpServletRequest request= (HttpServletRequest) invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
	        message.append(request.getParameterMap().toString());
	        message.append("] took ").append(executionTime).append(" ms.");
	        log.info(message.toString());
		}
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
