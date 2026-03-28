<%@page import="com.web.pojo.ModifyVoter"%>
<%@page import="com.web.daoimpl.ModifyVoterDaoImpl"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
<title>Voter Page</title>

<!-- Favicons -->
<link type="image/x-icon" href="assets/img/logo.png" rel="icon">

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="assets/css/bootstrap.min.css">

<!-- Fontawesome CSS -->
<link rel="stylesheet" href="assets/plugins/fontawesome/css/fontawesome.min.css">
<link rel="stylesheet" href="assets/plugins/fontawesome/css/all.min.css">

<!-- Main CSS -->
<link rel="stylesheet" href="assets/css/style.css">

<!--[if lt IE 9]>
<script src="assets/js/html5shiv.min.js"></script>
<script src="assets/js/respond.min.js"></script>
<![endif]-->
</head>
<body>
<%@include file="header1.jsp" %>
<div class="login_section">
 <div class="container">
  <div class="row">
   <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
    <div class="card card-signin my-5">
     <div class="card-body">
      <h5 class="card-title text-center">Upgrade Voter's</h5>
      <br>
      <%
          String msg = (String)request.getAttribute("msg");
          if(msg != null)
              out.print(msg);

          String voter = request.getParameter("voter_id");
          

          int voterid = 0;
          boolean validId = false;
          if(voter != null) {
              try {
                  voterid = Integer.parseInt(voter);
                  validId = true;
              } catch(NumberFormatException e) {
                  out.print("<p style='color:red;'>Invalid voter ID format.</p>");
              }
          } else {
              out.print("<p style='color:red;'>No voter ID provided in request.</p>");
          }

          if(validId) {
              ModifyVoterDaoImpl daoimpl = new ModifyVoterDaoImpl();
              ModifyVoter v = daoimpl.getVoterById(voterid);

              if(v != null) {
      %>
      <br>
      <form class="form-signin" action="ModifyVoterServlet" method="post">
      
        <div class="form-label-group">
          <input type="text" id="username" class="form-control" placeholder="username" value="<%= v.getUsername() != null ? v.getUsername() : "" %>" name="username" required>
          <label for="username">Username</label>
        </div>

        <div class="form-label-group">
          <input type="password" id="password" class="form-control" placeholder="password" value="<%= v.getPassword() != null ? v.getPassword() : "" %>" name="password" required>
          <label for="password">Password</label>
        </div>

        <div class="form-label-group">
          <input type="text" id="fullname" class="form-control" placeholder="full_name" value="<%= v.getFullname() != null ? v.getFullname() : "" %>" name="full_name" required>
          <label for="fullname">Full Name</label>
        </div>

        <div class="form-label-group">
          <input type="number" id="age" class="form-control" placeholder="age" value="<%= v.getAge() %>" name="age" required min="18" max="120">
          <label for="age">Age</label>
        </div>

        <div class="form-label-group">
          <input type="text" id="mobile" class="form-control" placeholder="Mobile" value="<%= v.getMobile() != null ? v.getMobile() : "" %>" name="mobile">
          <label for="mobile">Mobile</label>
        </div>
        
        <div class="form-label-group">
            <input type="text" id="inputEmail" class="form-control" placeholder="voter_id" value="<%= v.getVoter_id()%>" name="voter_id" >
        <label for="inputEmail">Voter ID</label>
                </div>
        <input class="btn btn-lg btn-primary btn-block text-uppercase" type="submit" value="Upgrade Voter" name="b1">
        <hr class="my-4">
        <br>
      </form>
      <%
              } else {
                  out.print("<p style='color:red;'>Voter not found for ID: " + voterid + "</p>");
              }
          }
      %>
      <a href="admin.jsp">Back To Admin Page</a><br><br>
     </div>
    </div>
   </div>
  </div>
 </div>
</div>
<%@include file="footer.jsp" %>
</body>
</html>
