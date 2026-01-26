// 공통 JS 

//카테고리 모달 조작
document.addEventListener("DOMContentLoaded", () => {
  const modal = document.querySelector(".category-modal");
  if (!modal) return;

  const overlay = modal.querySelector(".category-modal-overlay");
  const closeBtn = modal.querySelector(".category-modal-close-btn");

  /* =========================
     Open / Close
  ========================= */

  const openModal = () => {
    modal.classList.remove("is-hidden");
  };

  const closeModal = () => {
    modal.classList.add("is-hidden");
  };

  // 외부에서 호출 가능하게 window에 노출
  window.openCategoryModal = openModal;
  window.closeCategoryModal = closeModal;

  overlay.addEventListener("click", closeModal);
  closeBtn.addEventListener("click", closeModal);

  /* ESC key */
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") {
      closeModal();
    }
  });

  /* =========================
     Accordion (Category Modal)
  ========================= */

  modal.querySelectorAll(".category-modal-toggle")
    .forEach((btn) => {
      btn.addEventListener("click", (e) => {
        e.preventDefault();
        e.stopPropagation();

        const group = btn.closest(".category-modal-group");
        if (!group) return;

        group.classList.toggle("is-open");
      });
    });


});
