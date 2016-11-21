package com.dhf.tools.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dhf.tools.dao.entity.Log;

public interface LogMapper {
	public void insertLog(Log log);
	public List<Log> queryLog(@Param(value="userName") String userName,@Param(value="startDate") String startDate,@Param(value="endDate") String endDate);
}
