package com.web.pojo;

public class ModifyVoter 
{

	@Override
	public String toString() {
		return "ModifyVoter [username=" + username + ", password=" + password + ", fullname=" + full_name + ", age=" + age+ ", mobile=" + mobile + ",voter_id=" + voter_id +"]";
	}
	private String username;
	private String password;
	private String full_name;
	private int age;
	private String mobile;
	private int voter_id;
	
	
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
	public String getFullname() {
		return full_name;
	}
	public void setFullname(String fullname) {
		this.full_name = fullname;
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
	public int getVoter_id() {
		return voter_id;
	}
	public void setVoter_id(int voter_id) {
		this.voter_id = voter_id;
	}
	
	
	
	
	
	
	
	

}
