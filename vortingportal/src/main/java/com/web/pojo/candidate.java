package com.web.pojo;

public class candidate 
{

	
	
	@Override
	public String toString() {
		return "candidate [full_name=" + full_name + ", age=" + age + ", party_name=" + party_name + ", photo=" + photo
				+ ", election_type=" + election_type + ", id=" + id + "]";
	}
	private String full_name;
	private int age;
	private String party_name;
	private String photo;
	private String election_type;
	private int id;
		
	
	
	public String getFullname() {
		return full_name;
	}
	public void setFullname(String full_name) {
		this.full_name = full_name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int string) {
		this.age = string;
	}
	public String getPartyname() {
		return party_name;
	}
	public void setPartyname(String partyname) {
		this.party_name = partyname;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public void setElectionType(String electionType) {
		this.election_type = electionType;
		
	}
	public String getElection_type() {
		return election_type;
	}
	public void setElection_type(String election_type) {
		this.election_type = election_type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
	
	

}
