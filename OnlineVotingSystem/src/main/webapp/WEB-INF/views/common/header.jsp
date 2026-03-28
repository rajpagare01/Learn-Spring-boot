<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    com.voting.model.User sessionUser =
        (com.voting.model.User) session.getAttribute("user");
    boolean isAdmin = (sessionUser != null && sessionUser.isAdmin());
%>
<nav class="navbar">
    <div class="nav-container">
        <a class="nav-brand" href="${pageContext.request.contextPath}/">
            <span class="brand-icon"></span> VoteSecure
        </a>
        <button class="nav-toggle" id="navToggle" aria-label="Toggle navigation">
            <span></span><span></span><span></span>
        </button>
        <ul class="nav-links" id="navLinks">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <c:choose>
                        <c:when test="${sessionScope.role == 'ADMIN'}">
                            <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                            <li><a href="${pageContext.request.contextPath}/admin/elections">Elections</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="${pageContext.request.contextPath}/voter/dashboard">Dashboard</a></li>
                            <li><a href="${pageContext.request.contextPath}/results">Results</a></li>
                        </c:otherwise>
                    </c:choose>
                    <li class="nav-user">
                        <span class="user-avatar">${fn:substring(sessionScope.user.name,0,1)}</span>
                        <span class="user-name">${sessionScope.user.name}</span>
                    </li>
                    <li><a href="${pageContext.request.contextPath}/logout" class="btn-nav-logout">Logout</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
                    <li><a href="${pageContext.request.contextPath}/register" class="btn-nav-register">Register</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>


