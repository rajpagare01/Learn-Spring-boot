<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard  VoteSecure</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp"/>

    <div class="admin-layout">
        <!-- Sidebar -->
        <aside class="admin-sidebar">
            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/admin/dashboard"
                   class="sidebar-link active"> Dashboard</a>
                <a href="${pageContext.request.contextPath}/admin/elections"
                   class="sidebar-link"> Elections</a>
                <a href="${pageContext.request.contextPath}/logout"
                   class="sidebar-link sidebar-logout"> Logout</a>
            </nav>
        </aside>

        <!-- Main area -->
        <main class="admin-main">
            <div class="admin-page-header">
                <h1>Admin Dashboard</h1>
                <a href="${pageContext.request.contextPath}/admin/elections/new"
                   class="btn btn-primary">+ New Election</a>
            </div>

            <!-- Stats cards -->
            <div class="stats-grid">
                <div class="stat-card stat-blue">
                    <div class="stat-icon"></div>
                    <div class="stat-body">
                        <div class="stat-value">${totalElections}</div>
                        <div class="stat-label">Total Elections</div>
                    </div>
                </div>
                <div class="stat-card stat-green">
                    <div class="stat-icon"></div>
                    <div class="stat-body">
                        <div class="stat-value">${activeElections}</div>
                        <div class="stat-label">Active Now</div>
                    </div>
                </div>
                <div class="stat-card stat-red">
                    <div class="stat-icon"></div>
                    <div class="stat-body">
                        <div class="stat-value">${closedElections}</div>
                        <div class="stat-label">Closed</div>
                    </div>
                </div>
                <div class="stat-card stat-purple">
                    <div class="stat-icon"></div>
                    <div class="stat-body">
                        <div class="stat-value">${totalVotes}</div>
                        <div class="stat-label">Total Votes Cast</div>
                    </div>
                </div>
            </div>

            <!-- Recent elections table -->
            <div class="admin-section">
                <div class="admin-section-header">
                    <h2>All Elections</h2>
                    <a href="${pageContext.request.contextPath}/admin/elections"
                       class="btn btn-outline btn-sm">Manage All</a>
                </div>

                <c:choose>
                    <c:when test="${empty recentElections}">
                        <div class="empty-state">
                            <p>No elections yet.
                               <a href="${pageContext.request.contextPath}/admin/elections/new">Create one </a>
                            </p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-wrapper">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Title</th>
                                        <th>Status</th>
                                        <th>Start</th>
                                        <th>End</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="el" items="${recentElections}" varStatus="st">
                                        <tr>
                                            <td>${st.index + 1}</td>
                                            <td><strong>${el.title}</strong></td>
                                            <td>
                                                <span class="badge badge-${el.status == 'ACTIVE' ? 'active' : el.status == 'CLOSED' ? 'danger' : 'upcoming'}">
                                                    ${el.status}
                                                </span>
                                            </td>
                                            <td><fmt:formatDate value="${el.startDate}" pattern="dd MMM yyyy"/></td>
                                            <td><fmt:formatDate value="${el.endDate}"   pattern="dd MMM yyyy"/></td>
                                            <td class="action-cell">
                                                <a href="${pageContext.request.contextPath}/admin/candidates?electionId=${el.id}"
                                                   class="btn btn-outline btn-xs">Candidates</a>
                                                <a href="${pageContext.request.contextPath}/results?electionId=${el.id}"
                                                   class="btn btn-outline btn-xs">Results</a>
                                                <a href="${pageContext.request.contextPath}/admin/elections/edit?id=${el.id}"
                                                   class="btn btn-warning btn-xs">Edit</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>
    <script src="${pageContext.request.contextPath}/js/admin.js"></script>
</body>
</html>


