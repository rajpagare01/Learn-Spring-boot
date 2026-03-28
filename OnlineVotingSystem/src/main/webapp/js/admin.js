/* ── VoteSecure — admin.js ────────────────────────────────── */

document.addEventListener('DOMContentLoaded', function () {

    /* Auto-hide flash alerts after 4 seconds */
    document.querySelectorAll('.alert').forEach(function (el) {
        setTimeout(function () {
            el.style.transition = 'opacity .5s';
            el.style.opacity    = '0';
            setTimeout(function () { el.remove(); }, 500);
        }, 4000);
    });

    /* Set datetime-local min to now (prevent past dates for new elections) */
    const startEl = document.getElementById('startDate');
    const endEl   = document.getElementById('endDate');
    if (startEl && !startEl.value) {
        const now = new Date();
        now.setMinutes(now.getMinutes() - now.getTimezoneOffset()); // local time
        const iso = now.toISOString().slice(0, 16);
        startEl.min = iso;
        if (endEl) endEl.min = iso;
    }

    /* Keep end-date min in sync with start-date */
    if (startEl && endEl) {
        startEl.addEventListener('change', function () {
            endEl.min = this.value;
            if (endEl.value && endEl.value < this.value) endEl.value = '';
        });
    }
});
