<%@ page import="com.web.pojo.candidate" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Candidate Management</title>
  <link type="image/x-icon" href="assets/img/logo.png" rel="icon">
  <link rel="stylesheet" href="assets/css/bootstrap.min.css">
  <link rel="stylesheet" href="assets/plugins/fontawesome/css/all.min.css">
  <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>

<%@ include file="header1.jsp" %>

<%
  String activeElection = (String) session.getAttribute("activeElectionType");
  if (activeElection == null) activeElection = "No active election selected.";
  String msg = (String) session.getAttribute("msg");
  if (msg != null) session.removeAttribute("msg");

  candidate c = (candidate) request.getAttribute("candidate"); // for update/delete
%>

<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-6">
      <div class="card shadow">
        <div class="card-header bg-primary text-white text-center">
          <h4>Candidate Management</h4>
        </div>
        <div class="card-body">

          <div class="alert alert-info text-center mb-3">
            Current Active Election: <strong><%= activeElection %></strong>
          </div>

          <% if (msg != null) { %>
            <div class="alert alert-warning text-center"><%= msg %></div>
          <% } %>

          <form action="CandidateServlet" method="post" enctype="multipart/form-data">

            <!-- Candidate ID (visible for update/delete) -->
            <div class="form-group">
              <label for="id">Candidate ID</label>
              <input type="number" id="id" name="id" class="form-control" 
                     value="<%= (c != null) ? c.getId() : "" %>" placeholder="Enter Candidate ID ">
            </div>

            <div class="form-group">
              <label for="fullName">Full Name</label>
              <input type="text" id="fullName" name="full_name" class="form-control" 
                     value="<%= (c != null) ? c.getFullname() : "" %>">
            </div>

            <div class="form-group">
              <label for="age">Age</label>
              <input type="number" id="age" name="age" class="form-control" 
                     value="<%= (c != null) ? c.getAge() : "" %>">
            </div>

            <div class="form-group">
              <label for="partyName">Party Name</label>
              <input type="text" id="partyName" name="party_name" class="form-control" 
                     value="<%= (c != null) ? c.getPartyname() : "" %>">
            </div>

            <div class="form-group">
              <label for="election_type">Election Type</label>
              <select id="election_type" name="election_type" class="form-control">
                <option value="">-- Select Election Type --</option>
                <option value="Panchayat" <%= "Panchayat".equals((c != null) ? c.getElection_type() : activeElection) ? "selected" : "" %>>Panchayat</option>
                <option value="MLA" <%= "MLA".equals((c != null) ? c.getElection_type() : activeElection) ? "selected" : "" %>>MLA</option>
                <option value="Lok Sabha" <%= "Lok Sabha".equals((c != null) ? c.getElection_type() : activeElection) ? "selected" : "" %>>Lok Sabha</option>
              </select>
            </div>

            <div class="form-group">
              <label for="photo">Candidate Photo</label>
              <input type="file" id="photo" name="photo" class="form-control">
              <% if (c != null && c.getPhoto() != null && !c.getPhoto().isEmpty()) { %>
                <small>Current Photo: <img src="images/<%= c.getPhoto() %>" width="80" height="80"></small>
              <% } %>
            </div>

            <div class="form-group">
              <input type="submit" name="b2" value="Add Candidate" class="btn btn-success btn-block">
              <input type="submit" name="b2" value="Update Candidate" class="btn btn-primary btn-block">
              <input type="submit" name="b2" value="Delete Candidate" class="btn btn-danger btn-block">
            </div>

            <a href="admin.jsp" class="btn btn-secondary btn-block">Back to Admin Page</a>
          </form>

        </div>
      </div>
    </div>
  </div>
</div>

<%@ include file="footer.jsp" %>
</body>
</html>
