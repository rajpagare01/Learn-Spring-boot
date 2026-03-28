<%@page import="com.web.pojo.ModifyVoter"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
		<title>Voter List Page</title>
		
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
  <div class="container mt-6">
    <div class="row">
        <div class="col-md-9 mx-auto">
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <h4 class="text-center">VOTER LIST</h4>
                </div>
                <div class="card-body">
                    <table class="table table-striped">
                        <thead class="thead-dark">
           
            <br>
            <br>
			<table class="table table-bordered">
			<tr>
				<th>S.No.</th>
				<th>User name</th>
				<th>Password</th>
				<th>Full Name</th>
				<th>Age</th>
				<th>Mobile NO.</th>
				<th>Voter Id</th>
			</tr>
			<%

			List<ModifyVoter>lst=(List<ModifyVoter>) request.getAttribute("allvoter");
			if(lst != null && !lst.isEmpty())
			{
			for(int i=0; i<lst.size(); i++)
			{
				ModifyVoter v = lst.get(i);
				%>
					<tr>
						<td><%=i+1 %></td>
						<td><%=v.getUsername() %></td>
						<td><%=v.getPassword() %></td>
						<td><%=v.getFullname() %></td>
						<td><%=v.getAge() %></td>
						<td><%=v.getMobile() %></td>
						<td><%=v.getVoter_id() %></td>
						
					</tr>
				<%
			}
			}
			%>
			
			</table>
			<a href="admin.jsp">Back To Admin's Home Page</a><br><br>
			
			<br>
            
          </div>
        </div>
      </div>
    </div>
  </div>
    </div>
  </div>
            
 <%@include file="footer.jsp" %>           
</body>
</html>