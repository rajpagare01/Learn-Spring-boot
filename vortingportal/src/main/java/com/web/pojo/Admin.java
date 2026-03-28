package com.web.pojo;

public class Admin
{

	@Override
	public String toString() {
	
		return "admin [username=" + username + ", password=" + password + ", fullName=" + fullName + ", age=" + age 
				+", mobile=" + mobile + " , adminid=" + adminid +"]";
	}
    private String username;
    private String password;
    private String fullName;
    private int age;
    private String mobile;
    private String adminid;
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
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAdminid() {
		return adminid;
	}
	public void setAdminid(String adminid) {
		this.adminid = adminid;
	}
	

    
}
