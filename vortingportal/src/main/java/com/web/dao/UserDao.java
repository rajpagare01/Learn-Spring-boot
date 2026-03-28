package com.web.dao;

import com.web.pojo.User;

public interface UserDao {
	
	 boolean checkUserCredential(User u);
	boolean addNewUser(User u);
	
	
}