package com.dhf.tools.action;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dhf.tools.common.AES;
import com.dhf.tools.common.BaseAction;
import com.dhf.tools.common.MyConstants;
import com.dhf.tools.common.MyResponseState;
import com.dhf.tools.dao.entity.Database;
import com.dhf.tools.dao.mapper.DatabaseMapper;
import com.dhf.tools.json.MyResponse;

public class DatabaseAction extends BaseAction{

	/**
	 * 
	 */
    private static final long serialVersionUID = 8982868088878208985L;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private String currentUser;
    private int databaseId;
    private String url;
    private String username;
    private String password;
    private String driver;
    private String name;
    private Database database;
    @Autowired
    private DatabaseMapper databaseMapper;
    
    @Action("/editDatabase")
	public String editDatabase() {
		setCurrentUser(getCurrentUserFromSession().getAccount());
		Database db=databaseMapper.getDatabaseById(databaseId);
		try {
	        db.setPassword(AES.decrypt(db.getPassword(), MyConstants.AES_KEY));
        } catch (Exception e) {
        	log.info("Error when AES decrypt password:"+db.getPassword(),e);
        }
		setDatabase(db);
		setVelocityLocation("/editDatabase.vm");
		return VELOCITY;
	}
	@Action("/deleteDatabase")
	public String deleteDatabase() {
		MyResponse response = new MyResponse();
		setCurrentUser(getCurrentUserFromSession().getAccount());
		databaseMapper.deleteDatabase(databaseId);
		response.setState(MyResponseState.SUCCESS);		
		setResponse(response);
		return SUCCESS;
	}
	@Action("/updateDatabase")
	public String updateDatabase() {
		MyResponse response = new MyResponse();
		setCurrentUser(getCurrentUserFromSession().getAccount());
		Database oldDatabase=databaseMapper.getDatabaseById(databaseId);
		if(oldDatabase==null){
			response.setState(MyResponseState.ERROR_NO_SUCH_DB.getCode());
			response.setContent(MyResponseState.ERROR_NO_SUCH_DB.getMessage());
		}else {
			Database newDatabase=new Database();
			newDatabase.setId(databaseId);
			newDatabase.setName(name);
			newDatabase.setUrl(url);
			newDatabase.setUsername(username);
			try {
	            newDatabase.setPassword(AES.encrypt(password, MyConstants.AES_KEY));
            } catch (Exception e) {
	           log.info("Error when AES encrypt password:"+password,e);
            }
			newDatabase.setDriver(driver);
			databaseMapper.updateDatabase(newDatabase);
			response.setState(MyResponseState.SUCCESS);	
		}	
		setResponse(response);
		return SUCCESS;
	}
	@Action("/addDatabase")
	public String addDatabase() {
		setCurrentUser(getCurrentUserFromSession().getAccount());
		setVelocityLocation("/addDatabase.vm");
		return VELOCITY;
	}
	@Action("/addDatabaseInfo")
	public String addDatabaseInfo() {
		MyResponse response = new MyResponse();
		setCurrentUser(getCurrentUserFromSession().getAccount());

		Database newDatabase = new Database();
		newDatabase.setId(databaseMapper.getNextId());
		newDatabase.setName(name);
		newDatabase.setUrl(url);
		newDatabase.setUsername(username);
		try {
			newDatabase.setPassword(AES.encrypt(password, MyConstants.AES_KEY));
		} catch (Exception e) {
			log.info("Error when AES encrypt password:" + password, e);
		}
		newDatabase.setDriver(driver);
		databaseMapper.insertDatabase(newDatabase);
		response.setState(MyResponseState.SUCCESS);
		setResponse(response);
		return SUCCESS;
	}
	public int getDatabaseId() {
		return databaseId;
	}
	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCurrentUser() {
	    return currentUser;
    }
	public void setCurrentUser(String currentUser) {
	    this.currentUser = currentUser;
    }
	public Database getDatabase() {
	    return database;
    }
	public void setDatabase(Database database) {
	    this.database = database;
    }
	    
	public static void main(String[] args) throws Exception {
	    System.out.println(AES.encrypt("y9aXgwEGB6TT1Ils", MyConstants.AES_KEY));
    }
}
