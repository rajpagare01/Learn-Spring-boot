package com.web.daoimpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mysql.cj.x.protobuf.MysqlxSql.StmtExecute;
import com.web.dao.votingDao;

public  class VotingDaoImpl implements votingDao 
{
	Connection con=null;	
	public VotingDaoImpl() {
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


	public boolean recordVote(String voterId, String candidateName, String electionType) {
		
	        
	        try {
	        	
	        PreparedStatement ps = con.prepareStatement("insert into voting(voter_id, candidate_name,election_type) values(?, ?,?)"); 
	            ps.setString(1, voterId);
	            ps.setString(2, candidateName);
	            ps.setString(3, electionType);
	            int rowsAffected = ps.executeUpdate();
	            return rowsAffected > 0;
	        } 
	        catch (Exception e)
	        {
	            e.printStackTrace();
	            return false;
	        }
	}


	
	public boolean hasVotedInElection(String voterId, String electionType) {
        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM voting WHERE voter_id = ? AND election_type = ?"
            );
            ps.setString(1, voterId);
            ps.setString(2, electionType);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // returns true if a vote exists
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

	




	@Override
	public int getVoteCount(String candidate_Name,String electionTYpe) {
		int count=0;
		try {
            PreparedStatement ps = con.prepareStatement("select count(*) as count from voting where candidate_name = ? and election_type=?");
            ps.setString(1, candidate_Name);
            ps.setString(2, electionTYpe);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }


	@Override
	public int getTotalVotesByElectionType(String electionType) {
		int total=0;
		try {
		        PreparedStatement ps = con.prepareStatement("select count(*)  from voting where election_type=?");
		        ps.setString(1, electionType);
		        ResultSet rs = ps.executeQuery();
		        if (rs.next()) {
		            total = rs.getInt(1);
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return total;
		    
	}


	@Override
	public boolean recordVote(String voterId, String candidateName) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean castVote(String voter_Id, String candidate_name) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public int getTotalVotesByElectionType() {
		// TODO Auto-generated method stub
		return 0;
	}


	


	
	}


