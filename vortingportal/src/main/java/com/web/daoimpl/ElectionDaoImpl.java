package com.web.daoimpl;

import java.sql.*;
import com.web.dao.ElectionDao;
import com.web.pojo.Election;

public class ElectionDaoImpl implements ElectionDao {

    public ElectionDaoImpl() {
        // Load the MySQL JDBC driver once
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load JDBC driver:");
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/matdaanportal", "root", "root"
        );
    }

    @Override
    public boolean setActiveElection(String electionType) {
        System.out.println("Attempting to set election: " + electionType);
        System.out.println("Running query: insert into election (id,election_type) values(1,?) on duplicate key update election_type=?");

      

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("insert into election(id, election_type) values (1,?) on duplicate key update election_type=?")) {

            ps.setString(1, electionType);
            ps.setString(2, electionType);

            int rowsAffected = ps.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Database error in setActiveElection:");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Election getActiveElection() {
        System.out.println("Fetching active election...");


        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("select * from election where id=1");
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Election e = new Election();
                e.setId(rs.getInt("id"));
                e.setElection_type(rs.getString("election_type"));
                System.out.println("Found election: " + e.getElection_type());
                return e;
            } else {
                System.out.println("No active election found.");
                return null;
            }

        } catch (SQLException e) {
            System.err.println("Database error in getActiveElection:");
            e.printStackTrace();
            return null;
        }
    }
}
