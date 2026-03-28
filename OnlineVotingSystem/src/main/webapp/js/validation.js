/* ── VoteSecure — validation.js ──────────────────────────── */

/**
 * Shows an error message below a field.
 * Returns false so callers can chain: valid = setError(...) && valid
 */
function setError(fieldId, errorId, message) {
    const field = document.getElementById(fieldId);
    const span  = document.getElementById(errorId);
    if (field) field.classList.add('is-invalid');
    if (span)  span.textContent = message;
    return false;
}

function clearError(fieldId, errorId) {
    const field = document.getElementById(fieldId);
    const span  = document.getElementById(errorId);
    if (field) field.classList.remove('is-invalid');
    if (span)  span.textContent = '';
    return true;
}

/** Validates a required text field */
function validateRequired(fieldId, errorId, message) {
    const val = (document.getElementById(fieldId)?.value || '').trim();
    return val.length > 0
        ? clearError(fieldId, errorId)
        : setError(fieldId, errorId, message || 'This field is required.');
}

/** Validates an email field */
function validateEmail(fieldId, errorId) {
    const val = (document.getElementById(fieldId)?.value || '').trim();
    const re  = /^[\w.+\-]+@[a-zA-Z\d\-]+\.[a-zA-Z]{2,}$/;
    if (!val) return setError(fieldId, errorId, 'Email is required.');
    if (!re.test(val)) return setError(fieldId, errorId, 'Please enter a valid email address.');
    return clearError(fieldId, errorId);
}

/** Validates password length */
function validatePassword(fieldId, errorId) {
    const val = document.getElementById(fieldId)?.value || '';
    if (!val) return setError(fieldId, errorId, 'Password is required.');
    if (val.length < 8) return setError(fieldId, errorId, 'Password must be at least 8 characters.');
    return clearError(fieldId, errorId);
}

/** Validates password confirmation */
function validateConfirm(pwFieldId, confirmFieldId, errorId) {
    const pw  = document.getElementById(pwFieldId)?.value  || '';
    const cfm = document.getElementById(confirmFieldId)?.value || '';
    if (!cfm) return setError(confirmFieldId, errorId, 'Please confirm your password.');
    if (pw !== cfm) return setError(confirmFieldId, errorId, 'Passwords do not match.');
    return clearError(confirmFieldId, errorId);
}

/**
 * Password strength indicator.
 * Populates a coloured bar and a label element.
 */
function updateStrength(password, barId, labelId) {
    const bar   = document.getElementById(barId);
    const label = document.getElementById(labelId);
    if (!bar || !label) return;

    let score = 0;
    if (password.length >= 8)  score++;
    if (password.length >= 12) score++;
    if (/[A-Z]/.test(password)) score++;
    if (/[0-9]/.test(password)) score++;
    if (/[^A-Za-z0-9]/.test(password)) score++;

    const levels = [
        { pct: '20%', color: '#dc2626', text: 'Very weak' },
        { pct: '40%', color: '#f97316', text: 'Weak' },
        { pct: '60%', color: '#eab308', text: 'Fair' },
        { pct: '80%', color: '#22c55e', text: 'Strong' },
        { pct: '100%',color: '#15803d', text: 'Very strong' },
    ];
    const lvl = levels[Math.max(0, Math.min(score - 1, 4))];
    bar.style.width      = lvl.pct;
    bar.style.background = lvl.color;
    label.textContent    = password.length > 0 ? lvl.text : '';
    label.style.color    = lvl.color;
}

/* Inline field validation on blur */
document.addEventListener('DOMContentLoaded', function () {
    const rules = {
        email:           () => validateEmail('email', 'emailError'),
        password:        () => validatePassword('password', 'passwordError'),
        confirmPassword: () => validateConfirm('password', 'confirmPassword', 'confirmError'),
        name:            () => validateRequired('name', 'nameError', 'Full name is required.'),
    };
    Object.entries(rules).forEach(([id, fn]) => {
        const el = document.getElementById(id);
        if (el) el.addEventListener('blur', fn);
    });
});
