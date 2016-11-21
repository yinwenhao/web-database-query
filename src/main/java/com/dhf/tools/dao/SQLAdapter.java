package com.dhf.tools.dao;

public class SQLAdapter {

	private String sql;

	public SQLAdapter(String sql) {
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

}
