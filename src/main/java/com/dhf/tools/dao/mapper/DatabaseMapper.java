package com.dhf.tools.dao.mapper;

import java.util.List;

import com.dhf.tools.dao.entity.Database;
import com.dhf.tools.dao.entity.DatabaseConfig;
import com.dhf.tools.dao.entity.DatabaseSecretColumn;

public interface DatabaseMapper {
	
	
	public List<Database> getDatabaseList();
	
	public List<DatabaseSecretColumn> getDatabaseSecretColumnList();
	
	public List<DatabaseConfig> getConfigList();
	
	public void insertDatabase(Database database);
	
	public void updateDatabase(Database database);
	
	public Database getDatabaseById(int id);
	
	public void deleteDatabase(int id);
	
	public int getNextId();
}
