// header.js
document.addEventListener("DOMContentLoaded", () => {
  const categoryBtn = document.querySelector(".header-category-btn");
  if (!categoryBtn) return;

  categoryBtn.addEventListener("click", () => {
    window.openCategoryModal();
  });
});