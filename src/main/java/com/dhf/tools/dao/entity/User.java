package com.dhf.tools.dao.entity;

public class User {
	
	private static final String split = ",";
	
	private String account;
	
	private String password;
	
	private int permission_level;
	
	private String permission;
	
	private int[] permissionList;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPermission_level() {
		return permission_level;
	}

	public void setPermission_level(int permission_level) {
		this.permission_level = permission_level;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
		String[] ps = this.permission.split(split);
		permissionList = new int[ps.length];
		for (int i=0; i<ps.length; i++) {
			if (!ps[i].isEmpty()) {
				permissionList[i] = Integer.valueOf(ps[i]);
			}
		}
	}

	public int[] getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(int[] permissionList) {
		this.permissionList = permissionList;
	}

	public boolean isPermissionAllowed(int databaseId){
		for(int i=0;i<permissionList.length;i++){
			if(permissionList[i]==databaseId){
				return true;
			}
		}
		return false;
	}
}
