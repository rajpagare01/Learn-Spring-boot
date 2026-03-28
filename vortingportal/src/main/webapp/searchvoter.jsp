<%@page import="com.web.daoimpl.ModifyVoterDaoImpl"%>
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
<%@include file="header4.jsp" %>
  <div class="container mt-10">
    <div class="row">
        <div class="col-md-16 mx-auto">
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <h4 class="text-center">Searched Voter</h4>
                </div>
                <div class="card-body">
                    <table class="table table-striped">
                        <thead class="thead-dark">
           
            <br>
             <form action="" method="get">
                            <div class="form-group">
                                <label for="voterId">Enter Voter ID:</label>
                                <input type="text" class="form-control" id="voterId" name="voter_id" 
                                       placeholder="Enter voter ID" required>
                            </div>
                            <br>
                            <button type="submit" class="btn btn-primary btn-block">Search Voter</button>
                            <br>
                            <div class="text-center">
                                <a href="admin.jsp" class="btn btn-link">Back to Admin Home Page</a>
                            </div>
                        </form>

                       
                        <%
                        try {
                            int voter_id = Integer.parseInt(request.getParameter("voter_id"));
                		
                        if (voter_id>0) {
                            ModifyVoterDaoImpl daoimpl = new ModifyVoterDaoImpl();
                            ModifyVoter v =daoimpl.getVoterById(voter_id);
                            
                            if (v != null) {
                        %>
                        <div class="search-results">
                            <h4>Voter Details</h4>
                            <div class="table-responsive">
                                <table class="table table-bordered table-striped">
                                    <thead class="thead-dark">
                                        <tr>
                                            
			                            	<th>User name</th>
			                             	<th>Password</th>
	                             			<th>Full Name</th>
				                            <th>Age</th>
				                            <th>Mobile NO.</th>
				                            <th>Voter Id</th>
                                            
                                        </tr>
                                    </thead>
                                    <tbody>
                                    
                                        <tr>
                                            <td><%=v.getUsername() %></td>
						                    <td><%=v.getPassword() %></td>
						                    <td><%=v.getFullname() %></td>
						                    <td><%=v.getAge() %></td>
						                    <td><%=v.getMobile() %></td>
					                    	<td><%=v.getVoter_id() %></td>
                                            
                                        </tr>
                                    </tbody>
                                </table>
                                <a href="upgradevoter.jsp?voter_id=<%= v.getVoter_id() %>">Edit Voter</a>
                            </div>
                        </div>
                         <%
                            }
                            
                            else 
                            {
                    %>
                    <div class="alert alert-warning mt-3">
                        No voter found with ID: <%=voter_id%>
                    </div>
                    <%
                            }
                        } 
                        }
                        catch (NumberFormatException e) 
                        {
                        	e.printStackTrace();
                    %>
                    <div class="alert alert-danger mt-3">
                        Invalid Voter ID format. Please enter a numeric value.
                    </div>
                    <%
                        }
           
                    %>
                    </div>
                </div>
            </div>
        </div>
    </div>

<%@include file="footer.jsp" %>   
</body>
</html>