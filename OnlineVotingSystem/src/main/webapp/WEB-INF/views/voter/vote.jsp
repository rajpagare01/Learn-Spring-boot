<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cast Vote  ${election.title}</title>
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
                <h1>Cast Your Vote</h1>
                <p class="page-subtitle">${election.title}</p>
            </div>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <!-- Election info bar -->
        <div class="info-bar">
            <div class="info-item">
                <span class="info-label"> Closes</span>
                <span class="info-value">
                    <fmt:formatDate value="${election.endDate}" pattern="dd MMM yyyy, hh:mm a"/>
                </span>
            </div>
            <div class="info-item">
                <span class="info-label"> Time left</span>
                <span class="info-value countdown-value" id="voteTimer"
                      data-end="${election.endDate.time}">Loading</span>
            </div>
            <div class="info-item">
                <span class="info-label"> Candidates</span>
                <span class="info-value">${candidates.size()}</span>
            </div>
        </div>

        <!-- Ballot -->
        <form id="ballotForm" action="${pageContext.request.contextPath}/vote"
              method="post">
            <input type="hidden" name="electionId" value="${election.id}">

            <div class="ballot-container">
                <h2 class="ballot-heading">Select one candidate</h2>
                <p class="ballot-note">
                     Your vote is final and cannot be changed after submission.
                </p>

                <div class="candidates-grid">
                    <c:forEach var="c" items="${candidates}" varStatus="st">
                        <label class="candidate-card" for="candidate_${c.id}">
                            <input type="radio" id="candidate_${c.id}"
                                   name="candidateId" value="${c.id}"
                                   class="candidate-radio" required>
                            <div class="candidate-info">
                                <div class="candidate-avatar">${fn:substring(c.name,0,1)}</div>
                                <div class="candidate-details">
                                    <h3 class="candidate-name">${c.name}</h3>
                                    <span class="candidate-party">${c.party}</span>
                                    <c:if test="${not empty c.bio}">
                                        <p class="candidate-bio">${c.bio}</p>
                                    </c:if>
                                </div>
                            </div>
                            <span class="candidate-check"></span>
                        </label>
                    </c:forEach>
                </div>

                <div class="ballot-actions">
                    <a href="${pageContext.request.contextPath}/voter/dashboard"
                       class="btn btn-secondary">Cancel</a>
                    <button type="button" id="submitVoteBtn" class="btn btn-primary btn-lg"
                            onclick="confirmVote()">
                         Submit Vote
                    </button>
                </div>
            </div>
        </form>

        <!-- Confirmation modal -->
        <div id="confirmModal" class="modal-overlay" style="display:none;">
            <div class="modal-box">
                <h2>Confirm Your Vote</h2>
                <p>You are voting for:</p>
                <div class="modal-candidate" id="modalCandidateName"></div>
                <p class="modal-warning">
                     This action is <strong>irreversible</strong>. You cannot change
                    your vote after submission.
                </p>
                <div class="modal-actions">
                    <button class="btn btn-secondary" onclick="closeModal()">Go Back</button>
                    <button class="btn btn-primary" onclick="submitVote()">Confirm Vote</button>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>

    <script src="${pageContext.request.contextPath}/js/timer.js"></script>
    <script>
        // Highlight selected candidate card
        document.querySelectorAll('.candidate-radio').forEach(radio => {
            radio.addEventListener('change', function() {
                document.querySelectorAll('.candidate-card')
                        .forEach(c => c.classList.remove('selected'));
                this.closest('.candidate-card').classList.add('selected');
            });
        });

        function confirmVote() {
            const selected = document.querySelector('.candidate-radio:checked');
            if (!selected) {
                alert('Please select a candidate before submitting.');
                return;
            }
            const label = document.querySelector(`label[for="${selected.id}"] .candidate-name`);
            document.getElementById('modalCandidateName').textContent = label.textContent;
            document.getElementById('confirmModal').style.display = 'flex';
        }

        function closeModal() {
            document.getElementById('confirmModal').style.display = 'none';
        }

        function submitVote() {
            document.getElementById('ballotForm').submit();
        }

        // Close modal on overlay click
        document.getElementById('confirmModal').addEventListener('click', function(e) {
            if (e.target === this) closeModal();
        });

        // Countdown
        const timerEl = document.getElementById('voteTimer');
        if (timerEl) startCountdown(timerEl, parseInt(timerEl.dataset.end));
    </script>
</body>
</html>


