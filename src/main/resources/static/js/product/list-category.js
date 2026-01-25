document.addEventListener("DOMContentLoaded", () => {
  const dropdown = document.getElementById("listCategoryDropdown");
  const toggleBtn = document.getElementById("listCategoryToggle");

  if (!dropdown || !toggleBtn) return;

  toggleBtn.addEventListener("click", (e) => {
    e.stopPropagation();
    dropdown.classList.toggle("is-open");
  });

  document.addEventListener("click", () => {
    dropdown.classList.remove("is-open");
  });
});
