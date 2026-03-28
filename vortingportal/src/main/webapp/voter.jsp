<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Voter Page - MatdaanPortal</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">

    <!-- Favicons -->
    <link type="image/x-icon" href="assets/img/logo.png" rel="icon">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">

    <!-- Fontawesome CSS -->
    <link rel="stylesheet" href="assets/plugins/fontawesome/css/fontawesome.min.css">
    <link rel="stylesheet" href="assets/plugins/fontawesome/css/all.min.css">

    <!-- Main CSS -->
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>

<%@ include file="header3.jsp" %>

<!-- Voter Welcome Section -->
<section class="section section-doctor">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <div class="section-header text-center">
                    <h3>Welcome to the Voter Dashboard</h3>

                    <%
                        String username = (String) session.getAttribute("username");
                        if (username != null) {
                    %>
                        <h5 class="mt-3">Hello, <%= username %> Voter Page</h5>
                    <%
                        }
                    %>

                    <%
                        String electionType = (String) session.getAttribute("activeElectionType");
                        if (electionType != null && !electionType.trim().isEmpty()) {
                    %>
                        <p class="text-muted"><strong>Active Election:</strong> <%= electionType %></p>
                    <%
                        } else {
                    %>
                        <p class="text-muted"><strong>Active Election:</strong> None</p>
                    <%
                        }
                    %>
                </div>
            </div>

            <div class="col-lg-8 offset-lg-2">
                <div class="about-content text-center">
                    <p>
                        Voting is the cornerstone of democracy. MatdaanPortal ensures your vote is counted fairly, securely, and efficiently.
                    </p>
                    <p>
                        Be an active citizen. Participate in shaping your future with every vote you cast.
                    </p>

                    <div class="mt-4">
                        <a href="voting.jsp" class="btn btn-primary mr-2">Cast Your Vote</a>
                        <a href="results.jsp" class="btn btn-success">View Live Results</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- /Voter Welcome Section -->

<%@ include file="footer.jsp" %>

<!-- Scripts -->
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/popper.min.js"></script>
<script src="assets/js/bootstrap.min.js"></script>
<script src="assets/js/script.js"></script>

</body>
</html>
