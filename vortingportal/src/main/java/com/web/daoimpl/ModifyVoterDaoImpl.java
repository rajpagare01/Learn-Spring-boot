package com.web.daoimpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.web.dao.ModifyVoterDao;
import com.web.pojo.ModifyVoter;

public class ModifyVoterDaoImpl implements ModifyVoterDao
{
	Connection con =null;
	public ModifyVoterDaoImpl() {
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/matdaanportal","root","root");
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean addVoter(ModifyVoter v) {
		try
		{
			PreparedStatement ps=con.prepareStatement("insert into user(username, password, full_name, age, mobile,voter_id) values(?,?,?,?,?,?)");
			ps.setString(1, v.getUsername());
			ps.setString(2, v.getPassword());
			ps.setString(3, v.getFullname());
			ps.setInt(4, v.getAge());
			ps.setString(5, v.getMobile());
			ps.setInt(6, v.getVoter_id());
			
		
			int x=ps.executeUpdate();
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

	@Override
	public boolean updateVoter(ModifyVoter v) 
	{
		try
		{
			PreparedStatement ps=con.prepareStatement("update user set username = ?, password = ?, full_name = ?, age = ?,mobile = ? where voter_id = ?");
			ps.setString(1, v.getUsername());
			ps.setString(2, v.getPassword());
			ps.setString(3, v.getFullname());
			ps.setInt(4, v.getAge());
			ps.setString(5, v.getMobile());
			ps.setInt(6, v.getVoter_id());
			
			int x=ps.executeUpdate();
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

	@Override
	public Boolean deleteVoter(ModifyVoter v) {
		try
		{
			PreparedStatement ps=con.prepareStatement("delete from user where voter_id=?");
			ps.setInt(1, v.getVoter_id());
			int x=ps.executeUpdate();
			if(x>0)
				return true;
			else
				return false;
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
			
		}
	}

	public ModifyVoter getVoterById(int voter_id)
	
	{
		
	ModifyVoter v = null;
		try
		{
		PreparedStatement ps=con.prepareStatement("select * from user where voter_id =?");
		ps.setInt(1, voter_id);
		ResultSet rs=ps.executeQuery();
		if(rs.next()==true)
		{
			 v = new ModifyVoter();
		 v.setUsername(rs.getString("username"));
		 v.setPassword(rs.getString("password"));
		 v.setFullname(rs.getString("full_name"));
		 v.setAge(rs.getInt("age"));
		 v.setMobile(rs.getString("mobile"));
		 v.setVoter_id(rs.getInt("voter_id"));
	      
		 
		}
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return v;
		
	}

	@Override
	public List<ModifyVoter> getModifyVoters() {
	ArrayList<ModifyVoter>lst=new ArrayList<>();
	try
	{
		PreparedStatement ps =con.prepareStatement("select * from user");
		ResultSet rs=ps.executeQuery();
		while(rs.next())
		{
			ModifyVoter v =new ModifyVoter();
			v.setUsername(rs.getString("username"));
			 v.setPassword(rs.getString("password"));
			 v.setFullname(rs.getString("full_name"));
			 v.setAge(rs.getInt("age"));
			 v.setMobile(rs.getString("mobile"));
			 v.setVoter_id(rs.getInt("voter_id"));
		
			 lst.add(v);
		}
	}
	catch (Exception e) 
	{
		e.printStackTrace();
	
	}
	return lst;
	
	}

}
