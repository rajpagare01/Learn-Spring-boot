<%@page import="com.web.daoimpl.VotingDaoImpl"%>
<%@page import="com.web.daoimpl.CandidateDaoImpl"%>
<%@page import="com.web.pojo.candidate"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>matdaanportal - Election Results</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
    <link href="assets/img/logo.png" rel="icon">
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/plugins/fontawesome/css/fontawesome.min.css">
    <link rel="stylesheet" href="assets/plugins/fontawesome/css/all.min.css">
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>

<%@include file="header3.jsp" %>

<div class="container mt-5">
    <div class="row">
        <div class="col-md-10 mx-auto">
            <div class="card">
                <div class="card-header bg-primary text-white text-center">
                    <h4>Live Election Results</h4>
                </div>
                <div class="card-body">
                    <%
                        CandidateDaoImpl candidateDao = new CandidateDaoImpl();
                        VotingDaoImpl votingDao = new VotingDaoImpl();

                        String electionType = (String) session.getAttribute("activeElectionType");
                        if (electionType == null || electionType.trim().isEmpty()) {
                            electionType = candidateDao.getActiveElectionType(); // fallback if session is missing
                        }

                        List<candidate> candidates = candidateDao.getCandidatesByElectionType(electionType);
                        int totalVotes = votingDao.getTotalVotesByElectionType(electionType);
                    %>

                    <h5 class="text-center mb-4">Election Type: <strong><%= electionType %></strong></h5>

                    <% if (candidates == null || candidates.isEmpty()) { %>
                        <div class="alert alert-warning text-center">No candidates available for this election.</div>
                    <% } else { %>
                        <table class="table table-striped">
                            <thead class="thead-dark">
                                <tr>
                                    <th>Candidate</th>
                                    <th>Party</th>
                                    <th>Votes</th>
                                    <th>Percentage</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    for (candidate c : candidates) {
                                        int votes = votingDao.getVoteCount(c.getFullname(), electionType);
                                        double percentage = totalVotes > 0 ? (votes * 100.0 / totalVotes) : 0.0;
                                %>
                                <tr>
                                    <td><%= c.getFullname() %></td>
                                    <td><%= c.getPartyname() %></td>
                                    <td><%= votes %></td>
                                    <td><%= String.format("%.1f", percentage) %>%</td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                        <div class="text-right mt-3">
                            <strong>Total Votes Cast: <%= totalVotes %></strong>
                        </div>
                    <% } %>
                </div>
            </div>

            <div class="text-center mt-4">
                <a href="voter.jsp" class="btn btn-secondary">Back to voter page</a>
            </div>
        </div>
    </div>
</div>

<%@include file="footer.jsp" %>
</body>
</html>
