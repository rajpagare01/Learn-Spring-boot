package com.web.daoimpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.web.dao.UserDao;
import com.web.pojo.User;

public class UserDaoImpl implements UserDao {
    
    Connection con = null;
    
    public UserDaoImpl() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://"
                + "localhost:3306/matdaanportal?user=root&password=root");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkUserCredential(User u) {
        try {
            PreparedStatement ps = con.prepareStatement(
                "select * from user where username = ? and password = ?");
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addNewUser(User u) {
        try {
            PreparedStatement ps = con.prepareStatement(
                "insert into user(username, password, full_name, age, mobile, voter_id) values (?, ?, ?, ?, ?, ?)");
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getFullName());
            ps.setInt(4, u.getAge());
            ps.setString(5, u.getMobile());
            ps.setInt(6, u.getVoter_id());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getVoterIdByUsername(String username) {
        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT voter_id FROM user WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? String.valueOf(rs.getInt("voter_id")) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isValidVoter(String voter_id) {
        try {
            PreparedStatement ps = con.prepareStatement(
                "select * from user where voter_id = ?");
            ps.setString(1, voter_id);
            return ps.executeQuery().next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean markAsVoted(String voter_id) {
        try {
            PreparedStatement ps = con.prepareStatement(
                "update user set has_voted = 1 where voter_id = ?");
            ps.setString(1, voter_id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isOwnVoterId(String username, String voterId) {
        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT 1 FROM user WHERE username = ? AND voter_id = ?");
            ps.setString(1, username);
            ps.setInt(2, Integer.parseInt(voterId.trim()));
            return ps.executeQuery().next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByUsername(String username) {
        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM user WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("full_name"));
                user.setVoter_id(rs.getInt("voter_id"));
                user.setAge(rs.getInt("age"));
                user.setMobile(rs.getString("mobile"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}