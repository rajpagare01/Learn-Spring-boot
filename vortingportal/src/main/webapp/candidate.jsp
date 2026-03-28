<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Add Candidate</title>
  <link type="image/x-icon" href="assets/img/logo.png" rel="icon">
  <link rel="stylesheet" href="assets/css/bootstrap.min.css">
  <link rel="stylesheet" href="assets/plugins/fontawesome/css/all.min.css">
  <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>

<%@ include file="header1.jsp" %>

<%
  String activeElection = (String) session.getAttribute("activeElectionType");
  if (activeElection == null) {
    activeElection = "Not Set";
  }

  String msg = (String) session.getAttribute("msg");
%>

<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-6">
      <div class="card shadow">
        <div class="card-header bg-primary text-white text-center">
          <h4>Add Candidate</h4>
        </div>
        <div class="card-body">

          <!-- Show message -->
          <% if (msg != null) { %>
            <div class="alert alert-info text-center"><%= msg %></div>
            <% session.removeAttribute("msg"); %>
          <% } %>

          <form action="CandidateServlet" method="post" enctype="multipart/form-data">
            <!-- Hidden ID for update/delete (set to 0 or ignore for add) -->
            <input type="hidden" name="id" value="0">

            <div class="form-group">
              <label for="full_name">Full Name</label>
              <input type="text" class="form-control" name="full_name" required>
            </div>

            <div class="form-group">
              <label for="age">Age</label>
              <input type="number" class="form-control" name="age" required>
            </div>

            <div class="form-group">
              <label for="party_name">Party Name</label>
              <input type="text" class="form-control" name="party_name" required>
            </div>

            <div class="form-group">
              <label for="election_type">Election Type</label>
              <select class="form-control" name="election_type" required>
                <option value="">-- Select Election Type --</option>
                <option value="Panchayat" <%= "Panchayat".equals(activeElection) ? "selected" : "" %>>Panchayat</option>
                <option value="MLA" <%= "MLA".equals(activeElection) ? "selected" : "" %>>MLA</option>
                <option value="Lok Sabha" <%= "Lok Sabha".equals(activeElection) ? "selected" : "" %>>Lok Sabha</option>
              </select>
            </div>

            <div class="form-group">
              <label for="photo">Candidate Photo</label>
              <input type="file" class="form-control" name="photo" accept="image/*" required>
            </div>

            <div class="form-group text-center">
              <input type="submit" name="b2" value="Add Candidate" class="btn btn-success btn-block">
              <a href="candidatemanagement.jsp" class="btn btn-secondary btn-block">Back to Candidate List</a>
            </div>
          </form>

        </div>
      </div>
    </div>
  </div>
</div>

<%@ include file="footer.jsp" %>
</body>
</html>
