package com.web.dao;

import com.web.pojo.Election;

public interface ElectionDao 
{
	boolean setActiveElection(String electionType);
    Election getActiveElection();
}
