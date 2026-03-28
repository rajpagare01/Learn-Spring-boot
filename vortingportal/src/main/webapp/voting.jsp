<%@ page import="java.util.List" %>
<%@ page import="com.web.daoimpl.CandidateDaoImpl" %>
<%@ page import="com.web.pojo.candidate" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
 <meta charset="UTF-8">
 <title>Voterportal - Voting</title>
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
<%@ include file="header3.jsp" %>

<div class="login_section">
  <div class="container">
    <div class="row">
      <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
        <div class="card card-signin my-5">
          <div class="card-body">
            <h5 class="card-title text-center">Choose Candidate To Vote</h5>
            <br>

            <%
              String msg = (String) request.getAttribute("msg");
              if (msg != null) {
            %>
                <div class="alert alert-info text-center"><%= msg %></div>
            <%
              }
            %>
            <br>

            <%
              String electionType = (String) session.getAttribute("activeElectionType");
              if (electionType == null || electionType.trim().isEmpty()) {
                  electionType = "None";
              }

              CandidateDaoImpl daoimpl = new CandidateDaoImpl();
              List<candidate> candidateList = daoimpl.getCandidatesByElectionType(electionType);
            %>

            <h6 class="text-center">Active Election: <b><%= electionType %></b></h6>
            <hr>

            <%
              if ("None".equals(electionType) || candidateList == null || candidateList.isEmpty()) {
            %>
                <div class="alert alert-warning text-center">No candidates found for the active election.</div>
            <%
              } else {
            %>

            <form action="VotingServlet" method="post">
              <%
                for (int i = 0; i < candidateList.size(); i++) {
                  candidate c = candidateList.get(i);
              %>
                  <div class="mb-3">
                    <input type="radio" name="selected_candidate" value="<%= c.getFullname() %>" required <%= (i == 0) ? "checked" : "" %>>
                    <img src="images/<%= c.getPhoto() %>" width="100" height="100" alt="<%= c.getFullname() %>">
                    <b><%= c.getFullname() %></b> — <%= c.getPartyname() %>
                  </div>
              <%
                }
              %>
              <div class="form-group">
                <label for="voterid">Enter Voter ID</label>
                <input type="text" class="form-control" id="voterid" name="voter_id" required>
              </div>
              <br>
              <input type="submit" class="btn btn-primary btn-block" value="Vote">
            </form>

            <%
              } // end else
            %>

          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<%@ include file="footer.jsp" %>
</body>
</html>
