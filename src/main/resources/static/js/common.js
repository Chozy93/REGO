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
     Accordion
  ========================= */

  modal.querySelectorAll(".category-modal-main-btn")
    .forEach((btn) => {
      btn.addEventListener("click", () => {
        const group = btn.closest(".category-modal-group");
        if (!group) return;

        group.classList.toggle("is-open");
      });
    });

});


//에러모달 조작
$(function () {

  /* ==================================================
     Error Modal (jQuery)
  ================================================== */
  const $errorModal = $(".error-modal");

  if ($errorModal.length === 0) return;

  const $backdrop = $errorModal.find(".error-modal-backdrop");
  const $messageEl = $errorModal.find(".error-modal-body p");

  // 열기
  window.openErrorModal = function (message) {
    if (message) {
      $messageEl.text(message);
    }
    $errorModal.removeClass("is-hidden");
  };

  // 닫기
  window.closeErrorModal = function () {
    $errorModal.addClass("is-hidden");
  };

  // backdrop 클릭 시 닫기
  $backdrop.on("click", function () {
    window.closeErrorModal();
  });

});

