package com.web.pojo;

public class User {
    @Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", fullName=" + fullName + ", age=" + age
				+ ", mobile=" + mobile +",voter_id=" + voter_id +"]";
	}

	private String username;
    private String password;
    private String fullName;
    private int age;
    private String mobile;
    private int voter_id;
    ;

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

	public int getVoter_id() {
		return voter_id;
	}

	public void setVoter_id(int voter_id) {
		this.voter_id = voter_id;
	}

	

	
    
    
}