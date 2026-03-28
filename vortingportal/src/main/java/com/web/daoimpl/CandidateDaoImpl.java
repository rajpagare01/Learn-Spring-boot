package com.web.daoimpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.web.dao.candidateDao;
import com.web.pojo.candidate;

public class CandidateDaoImpl implements candidateDao {
    Connection con = null;

    public CandidateDaoImpl() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/matdaanportal", "root", "root");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean addcandidate(candidate c) {
        try {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO candidate (full_name, age, party_name, photo, election_type) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, c.getFullname());
            ps.setInt(2, c.getAge());
            ps.setString(3, c.getPartyname());
            ps.setString(4, c.getPhoto());
            ps.setString(5, c.getElection_type());

            int x = ps.executeUpdate();
            return x > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatecandidate(candidate c) {
        try {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE candidate SET full_name = ?, age = ?, party_name = ?, photo = ?, election_type = ? WHERE id=?");
            ps.setString(1, c.getFullname());
            ps.setInt(2, c.getAge());
            ps.setString(3, c.getPartyname());
            ps.setString(4, c.getPhoto());
            ps.setString(5, c.getElection_type());
            ps.setInt(6, c.getId());

            int x = ps.executeUpdate();
            return x > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deletecandidate(candidate c) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM candidate WHERE id = ?");
            ps.setInt(1, c.getId());
            int x = ps.executeUpdate();
            return x > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public candidate searchCandidate(String name) {
        candidate c = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM candidate WHERE full_name = ?");
            ps.setString(1, name); 
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c = new candidate();
                c.setFullname(rs.getString("full_name"));
                c.setAge(rs.getInt("age"));
                c.setPartyname(rs.getString("party_name"));
                c.setPhoto(rs.getString("photo"));
                c.setElection_type(rs.getString("election_type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public List<candidate> getCandidates() {
        List<candidate> candidates = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM candidate");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                candidate c = new candidate();
                c.setFullname(rs.getString("full_name"));
                c.setAge(rs.getInt("age"));
                c.setPartyname(rs.getString("party_name"));
                c.setPhoto(rs.getString("photo"));
                c.setElection_type(rs.getString("election_type"));
                c.setId(rs.getInt("id"));
                candidates.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }

    public List<candidate> getCandidatesByElectionType(String electionType) {
        List<candidate> candidates = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM candidate WHERE election_type = ?");
            ps.setString(1, electionType);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                candidate c = new candidate();
                c.setFullname(rs.getString("full_name"));
                c.setAge(rs.getInt("age"));
                c.setPartyname(rs.getString("party_name"));
                c.setPhoto(rs.getString("photo"));
                c.setElection_type(rs.getString("election_type"));
                candidates.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }

    public String getActiveElectionType() {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT election_type FROM election WHERE id = 1");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("election_type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
