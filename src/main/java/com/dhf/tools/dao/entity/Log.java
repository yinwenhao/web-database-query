package com.dhf.tools.dao.entity;

import java.sql.Timestamp;

public class Log {

	private String queryStatement;
	private String userName;
	private Timestamp queryTime;
	private long queryCost;
	private int state;
	private String errorMessage;
	private int databaseId;
	
	public int getDatabaseId() {
		return databaseId;
	}
	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		if(errorMessage!=null&&errorMessage.length()>200){
			errorMessage=errorMessage.substring(0,200);
		}
		this.errorMessage = errorMessage;
	}
	public String getQueryStatement() {
		return queryStatement;
	}
	public void setQueryStatement(String queryStatement) {
		this.queryStatement = queryStatement;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Timestamp getQueryTime() {
		return queryTime;
	}
	public void setQueryTime(Timestamp queryTime) {
		this.queryTime = queryTime;
	}
	public long getQueryCost() {
		return queryCost;
	}
	public void setQueryCost(long queryCost) {
		this.queryCost = queryCost;
	}
	
}
