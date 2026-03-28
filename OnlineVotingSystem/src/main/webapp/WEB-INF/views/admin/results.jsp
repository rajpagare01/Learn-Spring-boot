<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Results  ${election.title}  Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp"/>

    <div class="admin-layout">
        <aside class="admin-sidebar">
            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-link"> Dashboard</a>
                <a href="${pageContext.request.contextPath}/admin/elections"  class="sidebar-link active"> Elections</a>
                <a href="${pageContext.request.contextPath}/logout"           class="sidebar-link sidebar-logout"> Logout</a>
            </nav>
        </aside>

        <main class="admin-main">
            <div class="admin-page-header">
                <div>
                    <a href="${pageContext.request.contextPath}/admin/elections"
                       class="btn btn-outline btn-sm"> Elections</a>
                    <h1>Results  ${election.title}</h1>
                </div>
                <span class="badge badge-${election.status == 'ACTIVE' ? 'active' : election.status == 'CLOSED' ? 'danger' : 'upcoming'}">
                    ${election.status}
                </span>
            </div>

            <div class="info-bar">
                <div class="info-item">
                    <span class="info-label">Total Votes</span>
                    <span class="info-value">${totalVotes}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Candidates</span>
                    <span class="info-value">${candidates.size()}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Closed On</span>
                    <span class="info-value">
                        <fmt:formatDate value="${election.endDate}" pattern="dd MMM yyyy HH:mm"/>
                    </span>
                </div>
            </div>

            <div class="results-container">
                <h2>Live Vote Tally</h2>
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
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>


