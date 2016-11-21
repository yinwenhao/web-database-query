/**
 * 
 *//*
package com.dhf.tools.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhf.hbase.core.HBaseDaoTemplate;
import com.dhf.hbase.core.exception.HBaseDataRetrieveException;
import com.dhf.hbase.core.model.ResultData;
import com.dhf.tools.common.BaseAction;
import com.dhf.tools.common.MyResponseState;
import com.dhf.tools.json.MyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



*//**
 * @author longyaokun
 *
 *//*
public class HbaseQueryAction extends BaseAction {

	*//**
	 * serialVersionUID
	 *//*
	private static final long serialVersionUID = 1L;

	private String tableName;

	private String startKey;

	private String endKey;

	private int limit;

	private boolean idTransform;
	private String account;
	@Autowired
	private HBaseDaoTemplate hbaseDaoTemplate;

	@Action("/hbaseQuery")
	public String hbaseQuery() {
		MyResponse response = new MyResponse();
		try {
			List<ResultData> results=null;
			if (StringUtils.isEmpty(endKey)) {
				results = new ArrayList<ResultData>();
				ResultData result = hbaseDaoTemplate.get(tableName,
				        startKey, idTransform);
				if (result != null) {
					results.add(result);
				}
			} else {
				results = hbaseDaoTemplate.scan(tableName, startKey, endKey, limit, idTransform);
			}
			response.setContent(transferToString(results));
			response.setState(MyResponseState.SUCCESS);
		} catch (Exception e) {
			response.setContent(e.getMessage());
			response.setState(MyResponseState.ERROR);
		}
		setResponse(response);
		return SUCCESS;
	}

	@Action("/descTable")
	public String descTable() {
		MyResponse response = new MyResponse();
		try {
			String tableInfo = hbaseDaoTemplate.descTable(tableName);
			response.setContent(tableInfo);
			response.setState(MyResponseState.SUCCESS);
		} catch (HBaseDataRetrieveException e) {
			response.setContent(e.getMessage());
			response.setState(MyResponseState.ERROR);
		}
		setResponse(response);
		return SUCCESS;
	}
	private String transferToString(List<ResultData> rDatas)
	        throws JsonProcessingException {
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		ObjectMapper objectMapper = new ObjectMapper();
		if (CollectionUtils.isEmpty(rDatas)) {
			return objectMapper.writeValueAsString(results);
		}
		Map<byte[], Set<byte[]>> cfNameMap = getColumnFamilyMap(rDatas);
		for (ResultData rData : rDatas) {
			NavigableMap<byte[], NavigableMap<byte[], byte[]>> map=rData.getData();
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("rowKey", Bytes.toString(rData.getRow()));
			for (byte[] family : cfNameMap.keySet()) {
				NavigableMap<byte[], byte[]> familyMap = map.get(family);
				Set<byte[]> qualifierSet = cfNameMap.get(family);
				if (familyMap == null) {
					for (byte[] qualifier : qualifierSet) {
						result.put(Bytes.toString(family) + ":" + Bytes.toString(qualifier), null);
					}
				} else {
					for (byte[] qualifier : qualifierSet) {
						result.put(Bytes.toString(family) + ":" + Bytes.toString(qualifier),
						        Bytes.toString(familyMap.get(qualifier)));
					}
				}
			}
			results.add(result);

		}

		return objectMapper.writeValueAsString(results);
	}

	private Map<byte[], Set<byte[]>> getColumnFamilyMap(List<ResultData> rDatas) {
		Map<byte[], Set<byte[]>> cfMap = new HashMap<byte[], Set<byte[]>>();
		if (CollectionUtils.isEmpty(rDatas)) {
			return cfMap;
		}
		for(ResultData rData:rDatas){
			NavigableMap<byte[], NavigableMap<byte[], byte[]>> dataMap=rData.getData();
			for (byte[] family : dataMap.keySet()) {
				NavigableMap<byte[], byte[]> familyMap = dataMap.get(family);
				Set<byte[]> columnSet = cfMap.get(family);
				if (columnSet == null) {
					columnSet = new HashSet<byte[]>();
					cfMap.put(family, columnSet);
				}
				for (byte[] qualifier : familyMap.keySet()) {
					columnSet.add(qualifier);
				}
			}
		}
		
		return cfMap;
	}

	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getStartKey() {
		return startKey;
	}

	public void setStartKey(String startKey) {
		this.startKey = startKey;
	}

	public String getEndKey() {
		return endKey;
	}

	public void setEndKey(String endKey) {
		this.endKey = endKey;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public boolean isIdTransform() {
		return idTransform;
	}

	public void setIdTransform(boolean idTransform) {
		this.idTransform = idTransform;
	}

}
*/