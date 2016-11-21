/**
 * 
 */
package com.dhf.tools.action;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import net.sf.jsqlparser.JSQLParserException;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhf.tools.common.BaseAction;
import com.dhf.tools.common.MyDownloadFile;
import com.dhf.tools.common.MyResponseState;
import com.dhf.tools.common.SessionKeys;
import com.dhf.tools.common.TimeUtil;
import com.dhf.tools.dao.entity.Log;
import com.dhf.tools.dao.entity.User;
import com.dhf.tools.dao.mapper.LogMapper;
import com.dhf.tools.jdbc.DatabaseConnector;
import com.dhf.tools.json.MyResponse;
import com.dhf.tools.service.QueryService;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author when_how
 * 
 */

public class QueryAction extends BaseAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String sql;
	
	private int databaseId;
	@Autowired
	private LogMapper logMapper;
	@Autowired
	private QueryService queryService;
	
	/**
	 * 查询
	 * 
	 * @return
	 * @throws JsonProcessingException
	 */
	@Action("/query")
	public String query() throws JsonProcessingException {
		this.sql = new StringBuffer(sql).reverse().toString();
		MyResponse response = new MyResponse();
		if (getSql() == null || getSql().isEmpty() || (getSql().contains(";") && !getSql().endsWith(";"))) {
			response.setState(MyResponseState.ERROR_SQL_NOT_ALLOWED.getCode());
			setResponse(response);
			return SUCCESS;
		}
		if (!DatabaseConnector.CONNECTOR_MAP.containsKey(getDatabaseId())) {
			response.setState(MyResponseState.ERROR_PEMISSION_DENIED.getCode());
			setResponse(response);
			return SUCCESS;
		} else {
			User user = (User) getSession().get(SessionKeys.USER);
			if(!user.isPermissionAllowed(databaseId)){
				response.setState(MyResponseState.ERROR_PEMISSION_DENIED.getCode());
				response.setContent(MyResponseState.ERROR_PEMISSION_DENIED.getMessage());
				setResponse(response);
				return SUCCESS;
			}
			synchronized (user) {
				if (getSession().containsKey(SessionKeys.STATEMENT)) {
					response.setState(MyResponseState.ERROR_ONLY_ONE_SQL.getCode());
					response.setContent(MyResponseState.ERROR_ONLY_ONE_SQL.getMessage());
					setResponse(response);
					return SUCCESS;
				} else {
					Log queryLog= new Log();
					queryLog.setUserName(user.getAccount());
					queryLog.setDatabaseId(databaseId);
					queryLog.setQueryStatement(sql);					
					queryLog.setQueryTime(new Timestamp(System.currentTimeMillis()));
					try {
						response.setState(MyResponseState.SUCCESS);
						response.setContent(queryService.query(getSql(), getDatabaseId(), getSession()));
						log.info("The size of resultSet is "+(response.getContent().length()/(1024*1024))+"MB");
						response.setSqlTime((Long) getSession().get(SessionKeys.SQL_TIME));
						getSession().remove(SessionKeys.SQL_TIME);
						queryLog.setQueryCost(response.getSqlTime());
						queryLog.setState(response.getState());
					} catch (SQLException e) {
						log.info("get SQLException:",e);
						response.setState(MyResponseState.ERROR_SQL_EXECUTE.getCode());
						response.setContent(e.getMessage());
						setResponse(response);
						getSession().remove(SessionKeys.STATEMENT);
						queryLog.setState(response.getState());
						queryLog.setErrorMessage(response.getContent());
						return SUCCESS;
					} catch (JSQLParserException e) {
						log.info("get JSQLParserException:",e);
						response.setState(MyResponseState.ERROR_SQL_PARSER.getCode());
						response.setContent(MyResponseState.ERROR_SQL_PARSER.getMessage());
						setResponse(response);
						queryLog.setState(response.getState());
						queryLog.setErrorMessage(response.getContent());
						return SUCCESS;
					}finally{
						logMapper.insertLog(queryLog);
					}
				}
			}
		}
		setResponse(response);
		return SUCCESS;
	}
	
	/**
	 * 终止正在进行的sql
	 * 
	 * @return
	 * @throws JsonProcessingException
	 */
	@Action("/stop")
	public String stop() throws JsonProcessingException {
		MyResponse response = new MyResponse();
		if (!DatabaseConnector.CONNECTOR_MAP.containsKey(getDatabaseId())) {
			response.setState(MyResponseState.ERROR_PEMISSION_DENIED.getCode());
			setResponse(response);
			return SUCCESS;
		} else {
			Object o = getSession().get(SessionKeys.STATEMENT);
				if (o != null) {
					Statement ps = (Statement) o;
					try {
						ps.cancel();
						ps.close();
						getSession().remove(SessionKeys.STATEMENT);
					} catch (SQLException e) {
						// 停止sql失败
						log.error("stop sql error.", e);
						response.setState(MyResponseState.ERROR);
						response.setContent(e.getMessage());
						setResponse(response);
						return SUCCESS;
					}
					response.setState(MyResponseState.SUCCESS);
				} else {
					response.setState(MyResponseState.ERROR_NO_SQL_RUNNING.getCode());
					setResponse(response);
					return SUCCESS;
				}
		}
		setResponse(response);
		return SUCCESS;
	}
	
	@Action("/downloadResultFile")
	public String downloadResultFile() {
		MyDownloadFile f = (MyDownloadFile) getSession().get(SessionKeys.MY_DOWNLOAD_FILE);
		if (f != null && f.getResult().size() > 0 && f.getResult().get(0).size() > 0) {
			DatabaseConnector dc = DatabaseConnector.CONNECTOR_MAP.get(f.getDatabaseId());
			String rn = "\r\n";
			StringBuilder sb = new StringBuilder();
			
			String[] keys = f.getResult().get(0).keySet().toArray(new String[0]);
			for (int i=0; i<keys.length; i++) {
				if (i>0) {
					sb.append("|");
				}
				sb.append(keys[i]);
			}
			sb.append(rn);
			for (Map<String, Object> m : f.getResult()) {
				for (int i=0; i<keys.length; i++) {
					if (i>0) {
						sb.append("|");
					}
					sb.append(m.get(keys[i]));
				}
				sb.append(rn);
			}
			setStringResponse(sb.toString());
			setFileName(TimeUtil.formatDateToString(new Date()) + ".csv");
		} else {
			// 结果为空
			MyResponse response = new MyResponse();
			response.setState(MyResponseState.ERROR);
			response.setContent("结果集为空");
			setResponse(response);
			return SUCCESS;
		}
		return FILE;
	}
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public int getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}

}
