<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register  VoteSecure</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">
    <main class="auth-container">
        <div class="auth-card">
            <div class="auth-brand">
                <span class="auth-brand-icon"></span>
                <h1>Create Account</h1>
                <p>Register to participate in elections</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form id="registerForm"
                  action="${pageContext.request.contextPath}/register"
                  method="post" novalidate>

                <div class="form-group">
                    <label for="name">Full Name</label>
                    <input type="text" id="name" name="name"
                           class="form-control" placeholder="Alice Johnson"
                           value="${name}" required autocomplete="name">
                    <span class="field-error" id="nameError"></span>
                </div>

                <div class="form-group">
                    <label for="email">Email Address</label>
                    <input type="email" id="email" name="email"
                           class="form-control" placeholder="you@example.com"
                           value="${email}" required autocomplete="email">
                    <span class="field-error" id="emailError"></span>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <div class="input-group">
                        <input type="password" id="password" name="password"
                               class="form-control" placeholder="Min 8 characters"
                               required autocomplete="new-password">
                        <button type="button" class="input-addon" id="togglePwd"
                                aria-label="Show password"></button>
                    </div>
                    <!-- Password strength bar -->
                    <div class="strength-bar-wrap">
                        <div class="strength-bar" id="strengthBar"></div>
                    </div>
                    <span class="strength-label" id="strengthLabel"></span>
                    <span class="field-error" id="passwordError"></span>
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword"
                           class="form-control" placeholder="Repeat password"
                           required autocomplete="new-password">
                    <span class="field-error" id="confirmError"></span>
                </div>

                <button type="submit" class="btn btn-primary btn-block">
                    Create Account
                </button>
            </form>

            <p class="auth-footer-text">
                Already have an account?
                <a href="${pageContext.request.contextPath}/login">Sign in</a>
            </p>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
    <script>
        // Password toggle
        document.getElementById('togglePwd').addEventListener('click', function() {
            const pwd = document.getElementById('password');
            pwd.type = pwd.type === 'password' ? 'text' : 'password';
            this.textContent = pwd.type === 'password' ? '' : '';
        });

        // Live password strength
        document.getElementById('password').addEventListener('input', function() {
            updateStrength(this.value, 'strengthBar', 'strengthLabel');
        });

        // Form submit validation
        document.getElementById('registerForm').addEventListener('submit', function(e) {
            let valid = true;
            if (!validateRequired('name', 'nameError', 'Full name is required.')) valid = false;
            if (!validateEmail('email', 'emailError')) valid = false;
            if (!validatePassword('password', 'passwordError')) valid = false;
            if (!validateConfirm('password', 'confirmPassword', 'confirmError')) valid = false;
            if (!valid) e.preventDefault();
        });
    </script>
</body>
</html>


