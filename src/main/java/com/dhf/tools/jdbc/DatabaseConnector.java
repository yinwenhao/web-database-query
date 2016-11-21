package com.dhf.tools.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.sf.jsqlparser.JSQLParserException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import com.dhf.tools.common.SessionKeys;
import com.dhf.tools.sqlParse.MySQLParser;

@Component("databaseConnector")
@Scope("prototype") // 多例
public class DatabaseConnector {
	
	public static int defaultStatementTimeout = 30;
	
	private String driver;
	private String password;
	private String userName;
	private String url;
	private Integer databaseId;
	private DriverManagerDataSource dataSource;
	@Autowired
	private MySQLParser mySQLParser;
	public static Map<Integer, Map<String, Set<String>>> tableColumnSet;
	
	public ReadWriteLock lock = new ReentrantReadWriteLock();
	public static Map<Integer, DatabaseConnector> CONNECTOR_MAP = new ConcurrentHashMap<Integer, DatabaseConnector>();

	public void initDatabaseConnector(Integer databaseId, String driver, String url, String userName, String password) throws ClassNotFoundException, SQLException {
		DatabaseConnector dc = CONNECTOR_MAP.get(databaseId);
		if (dc != null) {
			if (dc.getDriver().equals(driver) && dc.getUrl().equals(url) && dc.getUserName().equals(userName) && dc.getPassword().equals(password)) {
				return;
			} else {
				CONNECTOR_MAP.remove(databaseId);
			}
		}
		createDataSource(driver, url, userName, password);	
		setConnectorInfo(databaseId, driver, url, userName, password);
		CONNECTOR_MAP.put(getDatabaseId(), this);
	}

	private void setConnectorInfo(Integer databaseId, String driver, String url, String userName, String password) {
	    setDriver(driver);
		setUrl(url);
		setUserName(userName);
		setPassword(password);
		setDatabaseId(databaseId);
    }

	private void createDataSource(String driver, String url, String userName, String password) {
	    dataSource = new DriverManagerDataSource(); 
		dataSource.setDriverClassName(driver); 
		dataSource.setUrl(url); 
		dataSource.setUsername(userName); 
		dataSource.setPassword(password);
    }

	public List<Map<String, Object>> executeQuery(String sql, Map<String, Object> session) throws SQLException, JSQLParserException {
		Statement ps = DataSourceUtils.getConnection(dataSource).createStatement();
		session.put(SessionKeys.STATEMENT, ps);
		ps.setQueryTimeout(defaultStatementTimeout);
		long startTime = System.currentTimeMillis();
		ResultSet rs = ps.executeQuery(sql);
		long time = System.currentTimeMillis() - startTime;
		session.put(SessionKeys.SQL_TIME, time);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		ResultSetMetaData metaData = rs.getMetaData();
		int imax = metaData.getColumnCount();
		int rowNum=1;
		Map<Integer, Map<String, Set<String>>> mmap = getTableColumnSet();
		Map<String, Set<String>> map = mmap.get(databaseId);
		
		while (rs.next()) {
			if(rowNum<10000){
			Map<String, Object> m = new HashMap<String, Object>();
			for (int i=1; i<=imax; i++) {
				Object value=rs.getObject(i);
				if(value instanceof Timestamp){
					value=((Timestamp)value).toString();
				}
				String tableName= metaData.getTableName(i);
				String columnName=metaData.getColumnName(i);
				String columnAliasName=metaData.getColumnLabel(i);
				if(map!=null&&map.containsKey(tableName)){
					if(isSecretColumn(columnName, map.get(tableName))){
						value="*";
					}
				}
				if(value!=null && value instanceof String){
					value=StringEscapeUtils.escapeHtml3((String) value);
				}
				m.put(columnAliasName, value);
			}
			result.add(m);
			}
			rowNum++;
		}
		// 关闭记录集
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// 关闭声明
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		session.remove(SessionKeys.STATEMENT);
	//	mySQLParser.checkQueryResult(getDatabaseId(), result, sql);
		return result;
	}
	private boolean isSecretColumn(String columnName, Set<String> columnNameSet) {
		for (String c : columnNameSet) {
			if (columnName.equalsIgnoreCase(c)) {
				return true;
			}
		}
		return false;
	}
	public Map<Integer, Map<String, Set<String>>> getTableColumnSet() {
		lock.readLock().lock();
		try {
			return tableColumnSet;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void setTableColumnSet(Map<Integer, Map<String, Set<String>>> tableColumnSet) {
		lock.writeLock().lock();
		try {
			DatabaseConnector.tableColumnSet = tableColumnSet;
		} finally {
			lock.writeLock().unlock();
		}
	}
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(Integer databaseId) {
		this.databaseId = databaseId;
	}

}
