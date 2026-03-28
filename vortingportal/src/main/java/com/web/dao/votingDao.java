package com.web.dao;

public interface votingDao 
{
	boolean recordVote(String voterId, String candidateName);
	 boolean hasVotedInElection(String voterId, String electionType);
    boolean castVote(String voter_Id, String candidate_name); 
    int getVoteCount(String candidateName , String electionYype);
    public int getTotalVotesByElectionType(String electionType);	int getTotalVotesByElectionType();
    
   
}
