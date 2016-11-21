package com.dhf.tools.dao.mapper;

import java.util.List;

import com.dhf.tools.dao.entity.User;

public interface UserMapper {
	
	public User getUserByAccount(String account);
	
	public List<User> getUserList();
	
	public void deleteUser(String account);
	
	public void insertUser(User user);
	
	public void updatePermission(User user);
	
}
