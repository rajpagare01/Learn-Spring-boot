package com.web.pojo;

public class voting
{

	
	
	@Override
	public String toString() {
		return "voting [voter_id=" + voter_id + ", candidate_name=" + candidate_name + ", election_type="
				+ election_type + "]";
	}
	private String voter_id;
	private String candidate_name;
	private String election_type;
	public String getVoter_id() {
		return voter_id;
	}
	public void setVoter_id(String voter_id) {
		this.voter_id = voter_id;
	}
	public String getFull_name() {
		return candidate_name;
	}
	public void setFull_name(String candidate_name) {
		this.candidate_name = candidate_name;
	}
	public String getElection_type() {
		return election_type;
	}
	public void setElection_type(String election_type) {
		this.election_type = election_type;
	}
	
	

}
