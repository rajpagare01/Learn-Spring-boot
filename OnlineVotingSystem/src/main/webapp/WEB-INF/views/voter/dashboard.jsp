<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Dashboard  VoteSecure</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/voter.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp"/>

    <main class="main-content">
        <div class="page-header">
            <div>
                <h1>Welcome, ${sessionScope.user.name}! </h1>
                <p class="page-subtitle">Here are the elections you can participate in.</p>
            </div>
        </div>

        <!-- Flash messages -->
        <c:if test="${param.voted == 'true'}">
            <div class="alert alert-success"> Your vote has been recorded successfully!</div>
        </c:if>

        <!-- Active Elections -->
        <section class="section">
            <h2 class="section-heading"> Active Elections</h2>
            <c:choose>
                <c:when test="${empty activeElections}">
                    <div class="empty-state">
                        <div class="empty-icon"></div>
                        <p>No active elections at the moment. Check back soon!</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="cards-grid">
                        <c:forEach var="el" items="${activeElections}">
                            <div class="election-card">
                                <div class="election-card-header">
                                    <span class="badge badge-active">ACTIVE</span>
                                    <h3>${el.title}</h3>
                                    <p class="election-desc">${el.description}</p>
                                </div>
                                <div class="election-meta">
                                    <div class="meta-row">
                                        <span> Closes:</span>
                                        <strong>
                                            <fmt:formatDate value="${el.endDate}"
                                                pattern="dd MMM yyyy, hh:mm a"/>
                                        </strong>
                                    </div>
                                    <!-- Countdown timer -->
                                    <div class="countdown" data-end="${el.endDate.time}">
                                        <span class="countdown-label">Time remaining:</span>
                                        <span class="countdown-value" id="timer-${el.id}">--:--:--</span>
                                    </div>
                                </div>
                                <div class="election-card-footer">
                                    <c:choose>
                                        <c:when test="${votedMap[el.id]}">
                                            <span class="voted-badge"> Voted</span>
                                            <a href="${pageContext.request.contextPath}/results?electionId=${el.id}"
                                               class="btn btn-outline btn-sm">View Results</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/vote?electionId=${el.id}"
                                               class="btn btn-primary btn-sm">Cast Vote </a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>

        <!-- Closed Elections -->
        <section class="section">
            <h2 class="section-heading"> Closed Elections  Results</h2>
            <c:choose>
                <c:when test="${empty closedElections}">
                    <p class="muted-text">No closed elections yet.</p>
                </c:when>
                <c:otherwise>
                    <div class="table-wrapper">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Election</th>
                                    <th>Closed On</th>
                                    <th>Your Vote</th>
                                    <th>Results</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="el" items="${closedElections}">
                                    <tr>
                                        <td>${el.title}</td>
                                        <td>
                                            <fmt:formatDate value="${el.endDate}"
                                                pattern="dd MMM yyyy"/>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${votedMap[el.id]}">
                                                    <span class="badge badge-success">Voted</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-neutral">Not voted</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/results?electionId=${el.id}"
                                               class="btn btn-outline btn-sm">View</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>

        <!-- Upcoming Elections -->
        <c:if test="${not empty upcomingElections}">
        <section class="section">
            <h2 class="section-heading"> Upcoming Elections</h2>
            <div class="cards-grid">
                <c:forEach var="el" items="${upcomingElections}">
                    <div class="election-card election-card-upcoming">
                        <span class="badge badge-upcoming">UPCOMING</span>
                        <h3>${el.title}</h3>
                        <p class="election-desc">${el.description}</p>
                        <div class="meta-row">
                            <span> Opens:</span>
                            <strong>
                                <fmt:formatDate value="${el.startDate}"
                                    pattern="dd MMM yyyy, hh:mm a"/>
                            </strong>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>
        </c:if>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>
    <script src="${pageContext.request.contextPath}/js/timer.js"></script>
</body>
</html>


