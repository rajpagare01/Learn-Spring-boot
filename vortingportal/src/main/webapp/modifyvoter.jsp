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
		
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
			<script src="assets/js/html5shiv.min.js"></script>
			<script src="assets/js/respond.min.js"></script>
		<![endif]-->
</head>
<body>
<%@include file="header4.jsp" %>
  <div class="login_section">
     <div class="container">
    <div class="row">
      <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
        <div class="card card-signin my-5">
          <div class="card-body">
            <h5 class="card-title text-center">Modify's Voter's</h5>
            <br>
            <%
            String msg = (String)request.getAttribute("msg");
            if(msg!=null)
            	out.print(msg);
            %>
            <br>
            <form class="form-signin" action="ModifyVoterServlet" method="post">
            
              <div class="form-label-group">
                <input type="text" id="inputEmail" class="form-control" placeholder="username" name="username" >
                <label for="inputEmail"></label>
              </div>

              <div class="form-label-group">
                <input type="text" id="inputEmail" class="form-control" placeholder="password" name="password" >
                <label for="inputEmail"></label>
              </div>
              
              <div class="form-label-group">
                <input type="text" id="inputEmail" class="form-control" placeholder="full_name" name="full_name" >
                <label for="inputEmail"></label>
              </div>
               <div class="form-label-group">
                <input type="text" id="inputEmail" class="form-control" placeholder="age" name="age" min="18" max="120" >
                <label for="inputEmail"></label>
              </div>
              <div class="form-label-group">
                <input type="text" id="inputEmail" class="form-control" placeholder="Mobile" name="mobile" >
                <label for="inputEmail"></label>
               </div>
               <div class="form-label-group">
                <input type="text" id="inputEmail" class="form-control" placeholder="voter_id" name="voter_id" >
                <label for="inputEmail"></label>
               </div>
                
               <input class="btn btn-lg btn-primary btn-block text-uppercase" type="submit" value="Add Voter" name="b1">
               <input class="btn btn-lg btn-primary btn-block text-uppercase" type="submit" value="Update Voter" name="b1">
               <input class="btn btn-lg btn-primary btn-block text-uppercase" type="submit" value="Delete Voter" name="b1">
                
              <hr class="my-4">
              <br>
              
              <a href="voter.jsp">Back To Voter Page</a><br><br>
              <br>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
    </div>
  </div>
  <!-- login section end-->

  <%@include file="footer.jsp" %>
</body>
</html>