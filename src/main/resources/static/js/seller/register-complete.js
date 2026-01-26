document.addEventListener("DOMContentLoaded", () => {
  let seconds = 10;
  const countEl = document.getElementById("redirect-count");

  const timer = setInterval(() => {
    seconds--;
    if (countEl) {
      countEl.textContent = seconds;
    }

    if (seconds <= 0) {
      clearInterval(timer);
      window.location.href = "/";
    }
  }, 1000);
});
