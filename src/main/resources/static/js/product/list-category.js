document.addEventListener("DOMContentLoaded", () => {
  const mega = document.getElementById("categoryMega");
  const toggle = mega?.querySelector(".category-mega-toggle");

  if (!mega || !toggle) return;

  toggle.addEventListener("click", (e) => {
    e.stopPropagation();
    mega.classList.toggle("is-open");
  });

  document.addEventListener("click", () => {
    mega.classList.remove("is-open");
  });
});
