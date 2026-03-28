package com.web.dao;

import java.util.List;

import com.web.pojo.ModifyVoter;

public interface ModifyVoterDao {
	boolean addVoter(ModifyVoter v);
	boolean updateVoter(ModifyVoter v);
	Boolean deleteVoter(ModifyVoter v);
	ModifyVoter getVoterById(int voter_id);
	List<ModifyVoter> getModifyVoters();
	
	

}
