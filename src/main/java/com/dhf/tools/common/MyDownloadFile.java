package com.dhf.tools.common;

import java.util.List;
import java.util.Map;

public class MyDownloadFile {
	
	private List<Map<String, Object>> result;
	
	private int databaseId;
	
	private String sql;
	
	public MyDownloadFile(int databaseId, String sql, List<Map<String, Object>> result) {
		setDatabaseId(databaseId);
		setSql(sql);
		setResult(result);
	}

	public List<Map<String, Object>> getResult() {
		return result;
	}

	public void setResult(List<Map<String, Object>> result) {
		this.result = result;
	}

	public int getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

}
