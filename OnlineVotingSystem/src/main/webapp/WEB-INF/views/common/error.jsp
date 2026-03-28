<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error  VoteSecure</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp"/>
    <main class="main-content">
        <div class="error-page">
            <div class="error-icon"></div>
            <h1>Something went wrong</h1>
            <p class="error-msg">
                <c:choose>
                    <c:when test="${not empty error}">${error}</c:when>
                    <c:otherwise>An unexpected error occurred. Please try again.</c:otherwise>
                </c:choose>
            </p>
            <div class="error-actions">
                <a href="javascript:history.back()" class="btn btn-secondary">Go Back</a>
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Home</a>
            </div>
        </div>
    </main>
    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>


