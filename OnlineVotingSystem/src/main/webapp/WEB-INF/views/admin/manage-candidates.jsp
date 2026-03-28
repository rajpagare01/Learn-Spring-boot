<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Candidates  ${election.title}</title>
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
                    <h1>Candidates  ${election.title}</h1>
                </div>
                <span class="badge badge-${election.status == 'ACTIVE' ? 'active' : election.status == 'CLOSED' ? 'danger' : 'upcoming'}">
                    ${election.status}
                </span>
            </div>

            <!-- Add Candidate Form -->
            <div class="admin-section">
                <h2>Add Candidate</h2>
                <form action="${pageContext.request.contextPath}/admin/candidates/add"
                      method="post" id="addCandidateForm">
                    <input type="hidden" name="electionId" value="${election.id}">
                    <div class="form-row">
                        <div class="form-group flex-2">
                            <label for="name">Full Name *</label>
                            <input type="text" id="name" name="name"
                                   class="form-control" required placeholder="e.g. Emma Davis">
                        </div>
                        <div class="form-group flex-1">
                            <label for="party">Party / Affiliation</label>
                            <input type="text" id="party" name="party"
                                   class="form-control" placeholder="e.g. Progressive Party">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="bio">Short Bio</label>
                        <textarea id="bio" name="bio" class="form-control" rows="2"
                                  placeholder="Brief description of the candidate..."></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">+ Add Candidate</button>
                </form>
            </div>

            <!-- Candidates List -->
            <div class="admin-section">
                <h2>Current Candidates (${candidates.size()})</h2>
                <c:choose>
                    <c:when test="${empty candidates}">
                        <div class="empty-state">
                            <p>No candidates yet. Add one above.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="candidates-admin-grid">
                            <c:forEach var="c" items="${candidates}">
                                <div class="candidate-admin-card">
                                    <div class="cand-avatar">${fn:substring(c.name,0,1)}</div>
                                    <div class="cand-info">
                                        <strong>${c.name}</strong>
                                        <span class="cand-party">${c.party}</span>
                                        <c:if test="${not empty c.bio}">
                                            <p class="cand-bio">${c.bio}</p>
                                        </c:if>
                                        <span class="cand-votes">${c.voteCount} votes</span>
                                    </div>
                                    <div class="cand-actions">
                                        <button class="btn btn-warning btn-xs"
                                                onclick="editCandidate(${c.id},'${c.name}','${c.party}','${c.bio}')">
                                             Edit
                                        </button>
                                        <button class="btn btn-danger btn-xs"
                                                onclick="deleteCandidate(${c.id},'${c.name}')">
                                             Delete
                                        </button>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>

    <!-- Edit modal -->
    <div id="editModal" class="modal-overlay" style="display:none;">
        <div class="modal-box">
            <h2>Edit Candidate</h2>
            <form id="editForm" method="post"
                  action="${pageContext.request.contextPath}/admin/candidates/update">
                <input type="hidden" name="electionId" value="${election.id}">
                <input type="hidden" name="id" id="editId">
                <div class="form-group">
                    <label>Full Name</label>
                    <input type="text" id="editName" name="name" class="form-control" required>
                </div>
                <div class="form-group">
                    <label>Party</label>
                    <input type="text" id="editParty" name="party" class="form-control">
                </div>
                <div class="form-group">
                    <label>Bio</label>
                    <textarea id="editBio" name="bio" class="form-control" rows="2"></textarea>
                </div>
                <div class="modal-actions">
                    <button type="button" class="btn btn-secondary"
                            onclick="document.getElementById('editModal').style.display='none'">
                        Cancel
                    </button>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Hidden delete form -->
    <form id="deleteCandForm" method="post"
          action="${pageContext.request.contextPath}/admin/candidates/delete"
          style="display:none">
        <input type="hidden" name="electionId" value="${election.id}">
        <input type="hidden" name="id" id="deleteCandId">
    </form>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>
    <script src="${pageContext.request.contextPath}/js/admin.js"></script>
    <script>
        function editCandidate(id, name, party, bio) {
            document.getElementById('editId').value    = id;
            document.getElementById('editName').value  = name;
            document.getElementById('editParty').value = party;
            document.getElementById('editBio').value   = bio;
            document.getElementById('editModal').style.display = 'flex';
        }

        function deleteCandidate(id, name) {
            if (confirm('Delete candidate "' + name + '"?')) {
                document.getElementById('deleteCandId').value = id;
                document.getElementById('deleteCandForm').submit();
            }
        }

        document.getElementById('editModal').addEventListener('click', function(e) {
            if (e.target === this) this.style.display = 'none';
        });
    </script>
</body>
</html>


