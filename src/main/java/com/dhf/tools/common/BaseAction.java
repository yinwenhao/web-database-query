package com.dhf.tools.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import com.dhf.tools.dao.entity.User;
import com.dhf.tools.json.MyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author when_how
 * 
 */
@ParentPackage("default")
@Results({
	@Result(name = BaseAction.DEFAULT, type = "velocity", params = {
			"location", "/WEB-INF/templet/login.vm" }),
		@Result(name = BaseAction.ADMIN_LOGIN, type = "velocity", params = {
					"location", "/WEB-INF/templet/adminLogin.vm" }),
		@Result(name = BaseAction.SUCCESS, type = "stream", params = {
				"bufferSize", BaseAction.BUFFER_SIZE, "contentType",
				BaseAction.CONTENT_TYPE, "inputName", "inputStream" }),
		@Result(name = BaseAction.FILE, type = "stream", params = { "bufferSize",
				BaseAction.BUFFER_SIZE, "contentType", BaseAction.CONTENT_TYPE,
				"inputName", "stringInputStream", "contentDisposition",
				"attachment;fileName=${fileName}" }),
		@Result(name = BaseAction.ERROR, type = "stream", params = { "bufferSize",
				BaseAction.BUFFER_SIZE, "contentType", BaseAction.CONTENT_TYPE,
				"inputName", "inputStreamException" }),
		@Result(name = BaseAction.SESSION_TIMEOUT, type = "stream", params = { "bufferSize",
				BaseAction.BUFFER_SIZE, "contentType", BaseAction.CONTENT_TYPE,
				"inputName", "sessionTimeOutException" }),
		@Result(name = BaseAction.VELOCITY, type = "velocity", params = {
				"location", "${velocityLocation}" }) })
public class BaseAction extends ActionSupport implements SessionAware {
	
	public static final String DEFAULT = "default";
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String VELOCITY = "velocity";
	public static final String FILE = "file";
	public static final String ADMIN_LOGIN = "admin";
	public static final String SESSION_TIMEOUT="session_timeout";
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1060001L;

	/** 缓冲区大小 */
	protected static final String BUFFER_SIZE = "1024";

	/** 文件类型 */
	protected static final String CONTENT_TYPE = "text/html";

	private String velocityLocation;

	private String fileName;

	/** 给客户端的string响应结果 */
	private String stringResponse;

	/** 给客户端的响应结果 */
	private MyResponse response;

	private InputStream inputStream;
	private InputStream stringInputStream;
	private InputStream inputStreamException;
	private InputStream sessionTimeOutException;
	public static final String[] keys = new String[] { "playerDataChange",
			"chats", "bobao", "fcmTips" };

	/** session */
	private Map<String, Object> session = new HashMap<String, Object>();

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		setInputStream(new ByteArrayInputStream(objectMapper
				.writeValueAsString(response).getBytes()));
		return inputStream;
	}

	public void setResponse(MyResponse response) {
		this.response = response;
	}

	public MyResponse getResponse() {
		return response;
	}

	public InputStream getInputStreamException() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		setInputStreamException(new ByteArrayInputStream(objectMapper
				.writeValueAsString(response).getBytes()));
		return inputStreamException;
	}
	
	public void setInputStreamException(InputStream inputStreamException) {
		this.inputStreamException = inputStreamException;
	}

	public InputStream getStringInputStream() throws UnsupportedEncodingException {
		setStringInputStream(new ByteArrayInputStream(stringResponse.getBytes("GBK")));
		return stringInputStream;
	}

	public void setStringInputStream(InputStream stringInputStream) {
		this.stringInputStream = stringInputStream;
	}

	public String getStringResponse() {
		return stringResponse;
	}

	public void setStringResponse(String stringResponse) {
		this.stringResponse = stringResponse;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getVelocityLocation() {
		return velocityLocation;
	}

	public void setVelocityLocation(String velocityLocation) {
		this.velocityLocation = "/WEB-INF/templet" + velocityLocation;
	}
	
	public void setVelocityLocationWholePath(String velocityLocation) {
		this.velocityLocation = velocityLocation;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	protected User getCurrentUserFromSession(){
		return (User) getSession().get(SessionKeys.USER);
	}

	public InputStream getSessionTimeOutException() throws JsonProcessingException {
		response.setState(MyResponseState.ERROR_SESSION_TIMEOUT.getCode());
		response.setContent(MyResponseState.ERROR_SESSION_TIMEOUT.getMessage());
		ObjectMapper objectMapper = new ObjectMapper();
		setSessionTimeOutException(new ByteArrayInputStream(objectMapper
				.writeValueAsString(response).getBytes()));
	    return sessionTimeOutException;
    }

	public void setSessionTimeOutException(InputStream sessionTimeOutException) {
	    this.sessionTimeOutException = sessionTimeOutException;
    }
}
