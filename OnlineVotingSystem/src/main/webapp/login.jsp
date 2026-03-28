<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - VoteSecure</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">
    <main class="auth-container">
        <div class="auth-card">
            <div class="auth-brand">
                <span class="auth-brand-icon">Vote</span>
                <h1>VoteSecure</h1>
                <p>Sign in to your account</p>
            </div>

            <c:if test="${param.registered == 'true'}">
                <div class="alert alert-success">
                    Registration successful. Please log in.
                </div>
            </c:if>
            <c:if test="${param.logout == 'true'}">
                <div class="alert alert-info">
                    You have been logged out.
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form id="loginForm" action="${pageContext.request.contextPath}/login"
                  method="post" novalidate>

                <div class="form-group">
                    <label for="email">Email address</label>
                    <input type="email" id="email" name="email"
                           class="form-control" placeholder="you@example.com"
                           value="${email}" required autocomplete="email">
                    <span class="field-error" id="emailError"></span>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <div class="input-group">
                        <input type="password" id="password" name="password"
                               class="form-control" placeholder="Enter password"
                               required autocomplete="current-password">
                        <button type="button" class="input-addon" id="togglePwd"
                                aria-label="Show password">Show</button>
                    </div>
                    <span class="field-error" id="passwordError"></span>
                </div>

                <button type="submit" class="btn btn-primary btn-block">
                    Sign In
                </button>
            </form>

            <p class="auth-footer-text">
                Do not have an account?
                <a href="${pageContext.request.contextPath}/register">Register here</a>
            </p>

            <div class="demo-creds">
                <p><strong>Demo credentials:</strong></p>
                <p>Admin: admin@vote.com / Admin@123</p>
                <p>Voter: alice@vote.com / Voter@123</p>
            </div>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
    <script>
        document.getElementById('togglePwd').addEventListener('click', function() {
            const pwd = document.getElementById('password');
            pwd.type = pwd.type === 'password' ? 'text' : 'password';
            this.textContent = pwd.type === 'password' ? 'Show' : 'Hide';
        });

        document.getElementById('loginForm').addEventListener('submit', function(e) {
            let valid = true;
            if (!validateEmail('email', 'emailError')) valid = false;
            if (!validateRequired('password', 'passwordError', 'Password is required.')) valid = false;
            if (!valid) e.preventDefault();
        });
    </script>
</body>
</html>
