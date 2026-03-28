<%@page import="com.web.pojo.candidate"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
  <title>Candidate List Page</title>

  <!-- Favicons -->
  <link type="image/x-icon" href="assets/img/logo.png" rel="icon">

  <!-- Bootstrap CSS -->
  <link rel="stylesheet" href="assets/css/bootstrap.min.css">

  <!-- Fontawesome CSS -->
  <link rel="stylesheet" href="assets/plugins/fontawesome/css/fontawesome.min.css">
  <link rel="stylesheet" href="assets/plugins/fontawesome/css/all.min.css">

  <!-- Main CSS -->
  <link rel="stylesheet" href="assets/css/style.css">

  <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!--[if lt IE 9]>
    <script src="assets/js/html5shiv.min.js"></script>
    <script src="assets/js/respond.min.js"></script>
  <![endif]-->
</head>
<body>

<%@include file="header1.jsp" %>

<div class="container mt-5">
  <div class="row">
    <div class="col-md-10 mx-auto">
      <div class="card">
        <div class="card-header bg-primary text-white text-center">
          <h4>CANDIDATE LIST</h4>
        </div>
        <div class="card-body">
          <table class="table table-bordered table-striped">
            <thead class="thead-dark">
              <tr>
                <th>Full Name</th>
                <th>Age</th>
                <th>Party Name</th>
                <th>Candidate Photo</th>
                <th>Candidate Post</th>
                <th>Candidate ID</th>
                
              </tr>
            </thead>
            <tbody>
              <%
                List<candidate> candidateList = (List<candidate>) request.getAttribute("allcandidate");
                if (candidateList != null && !candidateList.isEmpty()) {
                  for (candidate c : candidateList) {
              %>
                <tr>
                  <td><%= c.getFullname() %></td>
                  <td><%= c.getAge() %></td>
                  <td><%= c.getPartyname() %></td>
                  <td><img src="images/<%= c.getPhoto() %>" width="100" height="100"></td>
                  <td><%= c.getElection_type() %></td>
                  <td><%=c.getId() %></td>
                  
                </tr>
              <%
                  }
                } else {
              %>
                <tr>
                  <td colspan="5" class="text-center text-danger">No candidates found.</td>
                </tr>
              <%
                }
              %>
            </tbody>
          </table>

          <div class="text-center mt-3">
            <a href="admin.jsp" class="btn btn-secondary">Back To Admin's Home Page</a>
          </div>

        </div>
      </div>
    </div>
  </div>
</div>

<%@include file="footer.jsp" %>

</body>
</html>
