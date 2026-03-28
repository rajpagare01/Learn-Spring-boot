<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
		<title>candidate Page</title>
		
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
  <div class="container text-center mt-5">
  <h3>Select the Active Election</h3>
  <form action="ElectionServlet" method="post">
    <button type="submit" name="electionType" value="Panchayat" class="btn btn-primary m-2">Panchayat</button>
    <button type="submit" name="electionType" value="MLA" class="btn btn-success m-2">MLA</button>
    <button type="submit" name="electionType" value="Lok Sabha" class="btn btn-warning m-2">Lok Sabha</button>
  </form>

  
  <c:if test="${not empty msg}">
    <div class="alert alert-info mt-3">${msg}</div>
  </c:if>

  <c:if test="${not empty currentElection}">
    <div class="alert alert-success mt-2">
      Current Active Election: <strong>${currentElection}</strong>
    </div>
  </c:if>

  <a href="admin.jsp" class="btn btn-secondary mt-4">Back to Admin Page</a>
</div>
<%@ include file="footer.jsp" %>

<!-- JS -->
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/bootstrap.min.js"></script>
</body>
</html>