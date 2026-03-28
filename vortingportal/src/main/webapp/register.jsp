
<!DOCTYPE html> 
<html lang="en">
	
<!-- MatdaanPortal/login.html  27 July 2025 04:12:20 GMT -->
<head>
		<meta charset="utf-8">
		<title>MatdaanPortal-Voter Register</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
		
		<!-- Favicons -->
		<link href="assets/img/logo.png" rel="icon">
		
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
    <%@include file="header.jsp" %>
	  <class="account-page">

		<!-- Main Wrapper -->
		<div class="main-wrapper">
		
			<!-- Page Content -->
			<div class="content">
				<div class="container-fluid">
					
					<div class="row">
						<div class="col-md-8 offset-md-2">
								
							<!-- Register Content -->
							<div class="account-content">
								<div class="row align-items-center justify-content-center">
									<div class="col-md-7 col-lg-6 login-left">
										<img src="assets/img/login-banner.png" class="img-fluid" alt="Doccure Register">	
									</div>
									<div class="col-md-12 col-lg-6 login-right">
										<div class="login-header">
											<h3>Voter Register<a href="adminlogin.jsp">Are you an Admin?</a></h3>
										</div>
										
										<!-- Register Form -->
										 <form class="form-signin" action="UserRegServlet" method="post">
										
										<%
                                          String msg = (String)request.getAttribute("msg");
                                          if(msg!=null)
                                          {
                                        	out.print(msg);
                                          }
                                         %>
											<div class="form-group form-focus">
												<input type="text" class="form-control floating" name="name">
												<label class="focus-label">User Name</label>
											</div>
                                             
                                             <div class="form-group form-focus">
												<input type="text" class="form-control floating" name="fname">
												<label class="focus-label">Full Name</label>
											</div>

											<div class="form-group form-focus">
												<input type="text" class="form-control floating" name="age">
												<label class="focus-label">Age</label>
											</div>
											<div class="form-group form-focus">
												<input type="text" class="form-control floating" name="mno">
												<label class="focus-label">Mobile Number</label>
											</div>
											<div class="form-group form-focus">
												<input type="password" class="form-control floating" name="pass">
												<label class="focus-label">Create Password</label>
											</div>
											<div class="form-group form-focus">
												<input type="text" class="form-control floating" name="voter_id">
												<label class="focus-label">Voter ID</label>
											</div>
											
											<div class="text-right">
												<a class="forgot-link" href="login.jsp">Already have an account?</a>
											</div>
											<button class="btn btn-primary btn-block btn-lg login-btn" type="submit">Register</button>
											<div class="login-or">
												<span class="or-line"></span>
												<span class="span-or">or</span>
											</div>
											<div class="row form-row social-login">
											</div>
											<a href="login.jsp">Back To Login Page</a>
										</form>
										<!-- /Register Form -->
										
									</div>
								</div>
							</div>
							<!-- /Register Content -->
								
						</div>
					</div>

				</div>

			</div>		
			<!-- /Page Content -->
   
		</div>
		<!-- /Main Wrapper -->
	  
		<!-- jQuery -->
		<script src="assets/js/jquery.min.js"></script>
		
		<!-- Bootstrap Core JS -->
		<script src="assets/js/popper.min.js"></script>
		<script src="assets/js/bootstrap.min.js"></script>
		
		<!-- Custom JS -->
		<script src="assets/js/script.js"></script>
		<%@include file="footer.jsp" %>
	</body>

<!-- MatdaanPortal/login.html  27 July 2025 04:12:20 GMT -->
</html>