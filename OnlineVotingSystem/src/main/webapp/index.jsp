<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>VoteSecure  Online Voting System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp"/>

    <main class="main-content">
        <!-- Hero -->
        <section class="hero">
            <div class="hero-content">
                <h1 class="hero-title">Secure. Transparent. Trusted.</h1>
                <p class="hero-subtitle">
                    Cast your vote with confidence. VoteSecure ensures every
                    vote is counted exactly once, with full auditability.
                </p>
                <div class="hero-actions">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <c:choose>
                                <c:when test="${sessionScope.role == 'ADMIN'}">
                                    <a href="${pageContext.request.contextPath}/admin/dashboard"
                                       class="btn btn-primary btn-lg">Admin Dashboard</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/voter/dashboard"
                                       class="btn btn-primary btn-lg">My Dashboard</a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/register"
                               class="btn btn-primary btn-lg">Get Started</a>
                            <a href="${pageContext.request.contextPath}/login"
                               class="btn btn-outline btn-lg">Login</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="hero-visual"></div>
        </section>

        <!-- Features -->
        <section class="features">
            <h2 class="section-title">Why VoteSecure?</h2>
            <div class="features-grid">
                <div class="feature-card">
                    <div class="feature-icon"></div>
                    <h3>One Vote Per Person</h3>
                    <p>Enforced at both application and database level  no duplicate votes possible.</p>
                </div>
                <div class="feature-card">
                    <div class="feature-icon"></div>
                    <h3>Live Countdown</h3>
                    <p>Real-time election timers keep voters informed of deadlines.</p>
                </div>
                <div class="feature-card">
                    <div class="feature-icon"></div>
                    <h3>Instant Results</h3>
                    <p>Results are published automatically when elections close.</p>
                </div>
                <div class="feature-card">
                    <div class="feature-icon"></div>
                    <h3>Role-Based Access</h3>
                    <p>Separate admin and voter portals with session-based security.</p>
                </div>
            </div>
        </section>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>


