package com.dhf.tools.trigger.job;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.dhf.tools.common.AES;
import com.dhf.tools.common.MyConstants;
import com.dhf.tools.common.TargetDatabases;
import com.dhf.tools.dao.entity.Database;
import com.dhf.tools.dao.entity.DatabaseConfig;
import com.dhf.tools.dao.entity.DatabaseSecretColumn;
import com.dhf.tools.dao.mapper.DatabaseMapper;
import com.dhf.tools.dao.mapper.UserMapper;
import com.dhf.tools.jdbc.DatabaseConnector;
import com.dhf.tools.sqlParse.MySQLParser;

import io.netty.util.internal.ConcurrentSet;

@Component("jobLoadConfigFromDatabase")
public class JobLoadConfigFromDatabase implements Job, ApplicationContextAware {
	/** log */
	private Logger log = LoggerFactory
			.getLogger(JobLoadConfigFromDatabase.class);
	
	private ApplicationContext applicationContext;

	@Autowired
	private MySQLParser mySQLParser;

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private DatabaseMapper databaseMapper;

	@Override
	public void execute(JobExecutionContext je) throws JobExecutionException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("execute job: [" + getClass() + "]");
			}
			loadDatabaseConfig();
		} catch (Exception e) {
			log.error("", e);
		}
	}
	
	public void loadDatabaseConfig() {
		doDatabasesConfig();
		doMySQLParser();
		doDatabasesList();
	}

	private void doDatabasesConfig() {
		log.debug("doDatabasesConfig");
		List<DatabaseConfig> configList = databaseMapper.getConfigList();
		for (DatabaseConfig dc : configList) {
			try {
				if (dc.getKey().equals("jdbc.defaultStatementTimeout")) {
					DatabaseConnector.defaultStatementTimeout = Integer
							.valueOf(dc.getValue());
				}
			} catch (Exception e) {
				log.error("doDatabasesConfig error. key: " + dc.getKey()
						+ " value: " + dc.getValue(), e);
			}
		}
	}

	/**
	 * 列出可查询的数据库
	 * 
	 */
	private void doDatabasesList() {
		log.debug("doDatabasesList");
		Map<Integer, String> map = new HashMap<Integer, String>();
		List<Database> datatbaseList = databaseMapper.getDatabaseList();
		for (Database db : datatbaseList) {
			map.put(db.getId(), db.getName());
			DatabaseConnector databaseConnector = (DatabaseConnector) applicationContext.getBean("databaseConnector");
			try {
				databaseConnector.initDatabaseConnector(db.getId(), db.getDriver(), db.getUrl(), db.getUsername(), AES.decrypt(db.getPassword(),MyConstants.AES_KEY));
			} catch (ClassNotFoundException e) {
				log.error("init database connector error: driver class not found.", e);
				continue;
			} catch (SQLException e) {
				log.error("can not connect to database: "+db.getName(), e);
				continue;
			} catch (Exception e) {
				log.error("Error init database "+db.getName()+" :",e);
				continue;
            }
		}
		TargetDatabases.databaseList = map;
	}

	/**
	 * MySQLParser需要的回调
	 * 
	 */
	private void doMySQLParser() {
		log.debug("doMySQLParser");
		Map<Integer, Map<String, Set<String>>> mmap = new HashMap<Integer, Map<String, Set<String>>>();
		List<DatabaseSecretColumn> dscList = databaseMapper
				.getDatabaseSecretColumnList();
		for (DatabaseSecretColumn dsc : dscList) {
			Map<String, Set<String>> map = new HashMap<String, Set<String>>();
			String secretColumns = dsc.getSecret_columns();
			if (secretColumns == null) {
				return;
			}
			for (String s1 : secretColumns.split(MyConstants.split1)) {
				if (s1.isEmpty()) {
					continue;
				}
				String[] ss = s1.split(MyConstants.split2);
				String table = ss[0];
				Set<String> columnSet = new ConcurrentSet<String>();
				if (ss.length >= 2) {
					String cs = ss[1];
					for (String c : cs.split(MyConstants.split3)) {
						if (!c.isEmpty()) {
							columnSet.add(c.toLowerCase());
						}
					}
				}
				if (!columnSet.isEmpty()) {
					map.put(table.toLowerCase(), columnSet);
				}
			}
			mmap.put(dsc.getDatabase_id(), map);
		}
		DatabaseConnector databaseConnector = (DatabaseConnector) applicationContext.getBean("databaseConnector");
		databaseConnector.setTableColumnSet(mmap);
		mySQLParser.setTableColumnSet(mmap);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
