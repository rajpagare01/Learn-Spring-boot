/* ── VoteSecure — timer.js ────────────────────────────────── */

/**
 * startCountdown(element, endTimestampMs)
 * Updates `element.textContent` every second with a live HH:MM:SS countdown.
 * Adds .countdown-urgent class when ≤ 1 hour remaining.
 */
function startCountdown(el, endMs) {
    function tick() {
        const diff = endMs - Date.now();
        if (diff <= 0) {
            el.textContent = 'Election closed';
            el.classList.add('countdown-urgent');
            return;
        }
        const h = Math.floor(diff / 3_600_000);
        const m = Math.floor((diff % 3_600_000) / 60_000);
        const s = Math.floor((diff % 60_000) / 1_000);

        el.textContent = [
            String(h).padStart(2, '0'),
            String(m).padStart(2, '0'),
            String(s).padStart(2, '0'),
        ].join(':');

        if (diff <= 3_600_000) el.classList.add('countdown-urgent');
        else                    el.classList.remove('countdown-urgent');
    }
    tick();
    setInterval(tick, 1000);
}

/* Auto-init: scan for elements with data-end attribute */
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('[data-end]').forEach(function (el) {
        const endMs = parseInt(el.dataset.end, 10);
        if (!isNaN(endMs)) {
            // Use closest .countdown-value span if this element is a container
            const target = el.classList.contains('countdown-value')
                ? el
                : el.querySelector('.countdown-value');
            if (target) startCountdown(target, endMs);
        }
    });

    // Also initialise named timer elements referenced by id (vote.jsp)
    const voteTimer = document.getElementById('voteTimer');
    if (voteTimer && voteTimer.dataset.end) {
        startCountdown(voteTimer, parseInt(voteTimer.dataset.end, 10));
    }
});
