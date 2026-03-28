<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Results  ${election.title}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/voter.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp"/>

    <main class="main-content">
        <div class="page-header">
            <a href="${pageContext.request.contextPath}/voter/dashboard"
               class="btn btn-outline btn-sm"> Back</a>
            <div>
                <h1>Election Results</h1>
                <p class="page-subtitle">${election.title}</p>
            </div>
        </div>

        <!-- Summary bar -->
        <div class="info-bar">
            <div class="info-item">
                <span class="info-label">Status</span>
                <span class="badge badge-${election.status == 'CLOSED' ? 'danger' : 'active'}">
                    ${election.status}
                </span>
            </div>
            <div class="info-item">
                <span class="info-label">Total Votes</span>
                <span class="info-value">${totalVotes}</span>
            </div>
            <div class="info-item">
                <span class="info-label">Your Vote</span>
                <c:choose>
                    <c:when test="${hasVoted}">
                        <span class="badge badge-success">Voted </span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge badge-neutral">Did not vote</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Results table with bar chart -->
        <div class="results-container">
            <h2>Vote Tally</h2>
            <c:forEach var="c" items="${candidates}" varStatus="st">
                <c:set var="pct" value="${totalVotes > 0 ? (c.voteCount * 100 / totalVotes) : 0}"/>
                <div class="result-row ${st.index == 0 ? 'result-winner' : ''}">
                    <div class="result-rank">
                        <c:choose>
                            <c:when test="${st.index == 0}"></c:when>
                            <c:when test="${st.index == 1}"></c:when>
                            <c:when test="${st.index == 2}"></c:when>
                            <c:otherwise>${st.index + 1}</c:otherwise>
                        </c:choose>
                    </div>
                    <div class="result-info">
                        <div class="result-name">${c.name}
                            <span class="result-party">${c.party}</span>
                        </div>
                        <div class="result-bar-wrap">
                            <div class="result-bar" style="width: ${pct}%"></div>
                        </div>
                    </div>
                    <div class="result-stats">
                        <span class="result-votes">${c.voteCount} votes</span>
                        <span class="result-pct">${pct}%</span>
                    </div>
                </div>
            </c:forEach>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>


