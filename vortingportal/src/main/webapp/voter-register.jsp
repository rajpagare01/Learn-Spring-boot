<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>MatdaanPortal - Voter Register</title>
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
</head>
<body>
    <div class="account-page">
        <div class="main-wrapper">
            <div class="content">
                <div class="container-fluid">

                    <div class="row">
                        <div class="col-md-8 offset-md-2">

                            <!-- Register Content -->
                            <div class="account-content">
                                <div class="row align-items-center justify-content-center">
                                    
                                    <div class="col-md-7 col-lg-6 login-left">
                                        <img src="assets/img/login-banner.png" class="img-fluid" alt="Voter Register Banner">
                                    </div>

                                    <div class="col-md-12 col-lg-6 login-right">
                                        <div class="login-header">
                                            <h3>Voter Register <a href="adminregister.jsp">Are you an Admin?</a></h3>
                                        </div>

                                        <!-- Registration Message -->
                                        <%
                                            String msg = (String) request.getAttribute("msg");
                                            if (msg != null) {
                                        %>
                                            <div class="alert alert-info"><%= msg %></div>
                                        <%
                                            }
                                        %>

                                        <!-- Register Form -->
                                         <form class="form-signin" action="UserRegServlet" method="post">
                                         <div class="form-group form-focus">
                                                <input type="text" name="username" class="form-control floating" required>
                                                <label class="focus-label">User Name</label>
                                            </div>
                                            <div class="form-group form-focus">
                                                <input type="text" name="fullname" class="form-control floating" required>
                                                <label class="focus-label">Full Name</label>
                                            </div>

                                            <div class="form-group form-focus">
                                                <input type="number" name="age" class="form-control floating" required>
                                                <label class="focus-label">Age</label>
                                            </div>

                                            <div class="form-group form-focus">
                                                <input type="text" name="mobile" class="form-control floating" required>
                                                <label class="focus-label">Mobile Number</label>
                                            </div>

                                            <div class="form-group form-focus">
                                                <input type="password" name="password" class="form-control floating" required>
                                                <label class="focus-label">Create Password</label>
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
                                                <!-- Optional: Social buttons -->
                                            </div>

                                            <a href="login.jsp">Back to Login Page</a>
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
        </div>
    </div>

    <!-- JS Files -->
    <script src="assets/js/jquery.min.js"></script>
    <script src="assets/js/popper.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script src="assets/js/script.js"></script>

    <%@ include file="footer.jsp" %>
</body>
</html>
