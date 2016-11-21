package com.dhf.tools.sqlParse;

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.util.TablesNamesFinder;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("mySQLParser")
public class MySQLParser {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private Map<Integer, Map<String, Set<String>>> tableColumnSet;

	private CCJSqlParserManager pm = new CCJSqlParserManager();
	
	public ReadWriteLock lock = new ReentrantReadWriteLock();

	public static void main(String[] args) throws JSQLParserException {
		CCJSqlParserManager pm = new CCJSqlParserManager();
		String sql = "SELECT c0 水电费,aa.c1,aa.c1 as cc1, replace(c,''),c5-aa.c6,(select c9 from t2),`c2` cc2 FROM MY_TABLE1 aa, MY_TABLE2, (SELECT c3 cc3 FROM MY_TABLE3) LEFT OUTER JOIN MY_TABLE4 "
				+ " WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT c4 FROM MY_TABLE6)";
		Statement statement = CCJSqlParserUtil.parse(new StringReader(sql));
		if (statement instanceof Select) {
			Select selectStatement = (Select) statement;
			if (selectStatement.getSelectBody() instanceof PlainSelect) {
				List<SelectItem> list = ((PlainSelect) selectStatement
						.getSelectBody()).getSelectItems();
				for (SelectItem si : list) {
					SecretSelectItemVisitor selectItemVisitor =new SecretSelectItemVisitor();
					si.accept(selectItemVisitor);
					System.out.println(si);
					System.out.println(selectItemVisitor.isSecret());
				}
			}

			TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
			List<String> tableList = tablesNamesFinder
					.getTableList(selectStatement);
			for (String table : tableList) {
				System.out.println(table);

			}
		}

	}

	private Set<String> generateSecretColumnSet(String sql, Map<String, Set<String>> map) throws JSQLParserException{
		Set<String> secretColumnSet=new HashSet<String>();
		
		Statement statement = CCJSqlParserUtil.parse(sql);
		
		if (statement instanceof Select) {
			Select selectStatement = (Select) statement;
			TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
			List<String> tableList = tablesNamesFinder
					.getTableList(selectStatement);
			for (String t : tableList) {
				String table = t.replaceAll("`", "").toLowerCase();
				if (map.containsKey(table)) {
					Set<String> configuredSecretColumns = map.get(table);
					if (selectStatement.getSelectBody() instanceof PlainSelect) {
						List<SelectItem> list = ((PlainSelect) selectStatement
								.getSelectBody()).getSelectItems();
						for (SelectItem si : list) {
							if(si instanceof SelectExpressionItem){
								SelectExpressionItem selectExpressionItem=(SelectExpressionItem)si;
								SecretSelectItemVisitor selectItemVisitor =new SecretSelectItemVisitor();
								selectItemVisitor.setSecretColumnSet(configuredSecretColumns);
								selectExpressionItem.accept(selectItemVisitor);
								String columnName=selectExpressionItem.getAlias()!=null?selectExpressionItem.getAlias().getName():getColumnName(selectExpressionItem.getExpression());
								if(selectItemVisitor.isSecret()){
									secretColumnSet.add(columnName);
								}
							}
						}
					}
				}
			}
		}
		return secretColumnSet;
	}

	private String getColumnName(Expression expression) {
		if(expression instanceof Column){
			return ((Column)expression).getColumnName().replaceAll("`", "").toLowerCase();
		}
	    return expression.toString();
    }
	public void checkQueryResult(Integer databaseId, List<Map<String, Object>> result, String sql)
			throws JSQLParserException {
		if("select table_name from information_schema.tables where table_schema=(select database())".equals(sql)){
			return;
		}
		sql=sql.replaceAll("STRAIGHT_JOIN", "JOIN").replaceAll("straight_join", "join");
		if (log.isDebugEnabled()) {
			log.debug("input sql: " + sql);
		}
		if (result == null || result.size() <= 0) {
			return;
		}
		String queryHead = sql.split(" ")[0].toLowerCase();
		if (!queryHead.equals("select")) {
			return;
		}
		Map<Integer, Map<String, Set<String>>> mmap = getTableColumnSet();
		Map<String, Set<String>> map = mmap.get(databaseId);
		if(map==null){
			return;
		}
		Set<String> secretColumnSet=generateSecretColumnSet(sql,map);
		if(CollectionUtils.isNotEmpty(secretColumnSet)){
			for(String columnName:secretColumnSet){
				changeListMapValueByKey(result,columnName, "*");
			}
		}	
		
	}

	private void changeListMapValueByKey(List<Map<String, Object>> listMap,
			String key, Object value) {
		for (Map<String, Object> m : listMap) {
			if(m.containsKey(key)){
				m.put(key, value);
			}
		}
	}

	private String getContainsSetObject(String select, Set<String> set) {
		for (String c : set) {
			if (select.contains(c)) {
				return c;
			}
		}
		return null;
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
			this.tableColumnSet = tableColumnSet;
		} finally {
			lock.writeLock().unlock();
		}
	}
	
}
