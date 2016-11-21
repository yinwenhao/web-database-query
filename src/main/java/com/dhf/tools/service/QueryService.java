package com.dhf.tools.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.JSQLParserException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhf.tools.common.MyDownloadFile;
import com.dhf.tools.common.SessionKeys;
import com.dhf.tools.jdbc.DatabaseConnector;
import com.dhf.tools.sqlParse.MySQLParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("queryService")
public class QueryService {
	
	@Autowired
	private MySQLParser mySQLParser;
	
	public String query(String sql, Integer databaseId, Map<String, Object> session) throws SQLException, JSQLParserException, JsonProcessingException {
		DatabaseConnector connector = DatabaseConnector.CONNECTOR_MAP.get(databaseId);
		List<Map<String, Object>> result = connector.executeQuery(sql, session);
		mySQLParser.checkQueryResult(databaseId, result, sql);
		session.put(SessionKeys.MY_DOWNLOAD_FILE, new MyDownloadFile(databaseId, sql, result));
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(result);
	}

}
