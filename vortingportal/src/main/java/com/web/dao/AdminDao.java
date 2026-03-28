package com.web.dao;

import com.web.pojo.Admin;
import com.web.pojo.User;

public interface AdminDao 
{
	 boolean checkUserCredential(Admin a);
		boolean addNewUser(Admin a);
}
