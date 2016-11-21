package com.dhf.tools.dao.entity;

public class DatabaseSecretColumn {
	
	private int database_id;
	
	private String secret_columns;

	public int getDatabase_id() {
		return database_id;
	}

	public void setDatabase_id(int database_id) {
		this.database_id = database_id;
	}

	public String getSecret_columns() {
		return secret_columns;
	}

	public void setSecret_columns(String secret_columns) {
		this.secret_columns = secret_columns;
	}

}
