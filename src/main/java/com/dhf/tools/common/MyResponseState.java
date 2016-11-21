package com.dhf.tools.common;

public class MyResponseState {

	public static final int ERROR = -1;
	
	public static final int SUCCESS = 1;
	
	/** 没有这个用户 */
	public static final MyResponseState ERROR_NO_SUCH_USER = new MyResponseState(2,"该用户名不存在");
	
	/** 密码错误 */
	public static final MyResponseState ERROR_WRONG_PASSWORD = new MyResponseState(3,"密码不正确");
	
	/** 没有权限 */
	public static final MyResponseState ERROR_PEMISSION_DENIED = new MyResponseState(4,"您没有权限访问该数据库，请联系管理员添加权限");
	
	/** 用户已经存在 */
	public static final MyResponseState ERROR_USER_EXIST = new MyResponseState(5);

	/** 用户名不符合要求 */
	public static final MyResponseState ERROR_REGISTER_ACCOUNT = new MyResponseState(6);
	
	/** 密码不符合要求 */
	public static final MyResponseState ERROR_REGISTER_PASSWORD = new MyResponseState(7);

	/** 一次只能执行一条sql */
	public static final MyResponseState ERROR_ONLY_ONE_SQL = new MyResponseState(8,"您还有SQL未执行完毕，请稍后再试");
	
	/** 没有sql在执行 */
	public static final MyResponseState ERROR_NO_SQL_RUNNING = new MyResponseState(9);

	/** 非法的sql */
	public static final MyResponseState ERROR_SQL_NOT_ALLOWED = new MyResponseState(10);
	/** 没有这个数据库 */
	public static final MyResponseState ERROR_NO_SUCH_DB = new MyResponseState(11,"该数据库不存在");
	
	/** 没有这个用户 */
	public static final MyResponseState ERROR_PERMISSION_NOT_ALLOWED = new MyResponseState(12,"您的权限不够");
	
	public static final MyResponseState ERROR_SESSION_TIMEOUT = new MyResponseState(13,"您的身份已经过期，请重新登陆");
	
	public static final MyResponseState ERROR_SQL_PARSER = new MyResponseState(14,"SQL解析失败");
	
	public static final MyResponseState ERROR_SQL_EXECUTE = new MyResponseState(14,"SQL执行失败");
	private int code;
	private String message;

	public MyResponseState(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public MyResponseState(int code) {
		this.code = code;
		this.message = "";
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
