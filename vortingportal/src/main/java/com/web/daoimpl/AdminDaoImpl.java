package com.web.daoimpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.web.dao.AdminDao;
import com.web.pojo.Admin;

import com.web.pojo.Admin;

public class AdminDaoImpl implements  AdminDao
{
	Connection con=null;	
	public AdminDaoImpl() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://"
		+ "localhost:3306/matdaanportal?user=root&password=root");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public boolean checkUserCredential(Admin a) {
		try
		{
			PreparedStatement ps = 
con.prepareStatement
("select * from admin where username = ? and password = ?");
			ps.setString(1, a.getUsername());
			ps.setString(2, a.getPassword());
			ResultSet rs = ps.executeQuery();
			if(rs.next()==true)
				return true;
			else
				return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean addNewUser(Admin a) {
		try
		{
			PreparedStatement ps = con.prepareStatement(
	                "insert into admin(username, password, full_name, age, mobile,adminid) values (?, ?, ?, ?, ?,?)");
	            ps.setString(1, a.getUsername());
	            ps.setString(2, a.getPassword());
	            ps.setString(3, a.getFullName());
	            ps.setInt(4, a.getAge());
	            ps.setString(5, a.getMobile());
	            ps.setString(6, a.getAdminid());
	            
			int x = ps.executeUpdate();
			if(x>0)
				return true;
			else
				return false;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
}
}
