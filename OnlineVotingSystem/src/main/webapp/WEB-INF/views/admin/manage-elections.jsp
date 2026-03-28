<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Elections  VoteSecure</title>
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

            <!-- Flash messages -->
            <c:if test="${param.created == 'true'}">
                <div class="alert alert-success"> Election created successfully.</div>
            </c:if>
            <c:if test="${param.updated == 'true'}">
                <div class="alert alert-success"> Election updated successfully.</div>
            </c:if>
            <c:if test="${param.deleted == 'true'}">
                <div class="alert alert-info"> Election deleted.</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <!-- Create / Edit Form -->
            <div class="admin-section">
                <h2>${not empty election ? 'Edit Election' : 'Create New Election'}</h2>

                <form id="electionForm"
                      action="${pageContext.request.contextPath}/admin/elections/${not empty election ? 'update' : 'create'}"
                      method="post" novalidate>

                    <c:if test="${not empty election}">
                        <input type="hidden" name="id" value="${election.id}">
                    </c:if>

                    <div class="form-row">
                        <div class="form-group flex-2">
                            <label for="title">Election Title *</label>
                            <input type="text" id="title" name="title"
                                   class="form-control"
                                   value="${election.title}" required
                                   placeholder="e.g. Student Council President 2025">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="description">Description</label>
                        <textarea id="description" name="description"
                                  class="form-control" rows="3"
                                  placeholder="Brief description of the election...">${election.description}</textarea>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="startDate">Start Date & Time *</label>
                            <input type="datetime-local" id="startDate" name="startDate"
                                   class="form-control" required
                                   value="${not empty election ? election.startDate : ''}">
                        </div>
                        <div class="form-group">
                            <label for="endDate">End Date & Time *</label>
                            <input type="datetime-local" id="endDate" name="endDate"
                                   class="form-control" required
                                   value="${not empty election ? election.endDate : ''}">
                        </div>
                    </div>

                    <div class="form-actions">
                        <c:if test="${not empty election}">
                            <a href="${pageContext.request.contextPath}/admin/elections"
                               class="btn btn-secondary">Cancel</a>
                        </c:if>
                        <button type="submit" class="btn btn-primary">
                            ${not empty election ? 'Update Election' : 'Create Election'}
                        </button>
                    </div>
                </form>
            </div>

            <!-- Elections List -->
            <c:if test="${not empty elections}">
            <div class="admin-section">
                <h2>All Elections</h2>
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Title</th>
                                <th>Status</th>
                                <th>Start</th>
                                <th>End</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="el" items="${elections}">
                                <tr>
                                    <td><strong>${el.title}</strong></td>
                                    <td>
                                        <span class="badge badge-${el.status == 'ACTIVE' ? 'active' : el.status == 'CLOSED' ? 'danger' : 'upcoming'}">
                                            ${el.status}
                                        </span>
                                    </td>
                                    <td><fmt:formatDate value="${el.startDate}" pattern="dd MMM yyyy HH:mm"/></td>
                                    <td><fmt:formatDate value="${el.endDate}"   pattern="dd MMM yyyy HH:mm"/></td>
                                    <td class="action-cell">
                                        <a href="${pageContext.request.contextPath}/admin/candidates?electionId=${el.id}"
                                           class="btn btn-outline btn-xs"> Candidates</a>
                                        <a href="${pageContext.request.contextPath}/results?electionId=${el.id}"
                                           class="btn btn-outline btn-xs"> Results</a>
                                        <a href="${pageContext.request.contextPath}/admin/elections/edit?id=${el.id}"
                                           class="btn btn-warning btn-xs"> Edit</a>
                                        <button class="btn btn-danger btn-xs"
                                                onclick="confirmDelete(${el.id}, '${el.title}')">
                                             Delete
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            </c:if>

            <!-- Hidden delete form -->
            <form id="deleteForm" method="post"
                  action="${pageContext.request.contextPath}/admin/elections/delete"
                  style="display:none">
                <input type="hidden" name="id" id="deleteId">
            </form>
        </main>
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>
    <script src="${pageContext.request.contextPath}/js/admin.js"></script>
    <script>
        function confirmDelete(id, title) {
            if (confirm('Delete election "' + title + '"?\nThis will also delete all candidates and votes.')) {
                document.getElementById('deleteId').value = id;
                document.getElementById('deleteForm').submit();
            }
        }

        // Date validation
        document.getElementById('electionForm').addEventListener('submit', function(e) {
            const start = new Date(document.getElementById('startDate').value);
            const end   = new Date(document.getElementById('endDate').value);
            if (end <= start) {
                e.preventDefault();
                alert('End date must be after start date.');
            }
        });
    </script>
</body>
</html>


