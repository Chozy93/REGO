/**
 * Chat Room UI JS
 * - textarea auto height
 * - Enter / Shift+Enter handling (UI only)
 * - appointment / payment modal open & close
 */

document.addEventListener("DOMContentLoaded", () => {

  /* ==================================================
     Textarea Auto Height + Enter Handling
  ================================================== */
  const textarea = document.querySelector(".chat-room-textarea");

  if (textarea) {
    const resize = () => {
      textarea.style.height = "auto";
      textarea.style.height = textarea.scrollHeight + "px";
    };

    textarea.addEventListener("input", resize);

    textarea.addEventListener("keydown", (e) => {
      if (e.key === "Enter" && !e.shiftKey) {
        e.preventDefault();

        const value = textarea.value.trim();
        if (value.length === 0) return;

        // 실제 전송 로직은 나중에 구현
        textarea.value = "";
        resize();
      }
    });
  }

  /* ==================================================
     Modal Helper (UI only)
  ================================================== */
  function bindModal(openId, modalId, closeId) {
    const openBtn = document.getElementById(openId);
    const modal = document.getElementById(modalId);
    const closeBtn = document.getElementById(closeId);

    if (!modal) return;

    const open = () => {
      modal.classList.remove("is-hidden");
      modal.classList.add("is-active");
    };

    const close = () => {
      modal.classList.remove("is-active");
      modal.classList.add("is-hidden");
    };

    // open button
    if (openBtn) {
      openBtn.addEventListener("click", open);
    }

    // close button
    if (closeBtn) {
      closeBtn.addEventListener("click", close);
    }

    // backdrop click
    const backdrop = modal.querySelector(".chat-modal-backdrop");
    if (backdrop) {
      backdrop.addEventListener("click", close);
    }

    // ESC key
    document.addEventListener("keydown", (e) => {
      if (e.key === "Escape" && modal.classList.contains("is-active")) {
        close();
      }
    });
  }

  /* ==================================================
     Bind Chat Modals
  ================================================== */

  // 약속 설정 모달
  bindModal(
    "open-appointment-modal",
    "appointment-modal",
    "close-appointment-modal"
  );

  // 송금 모달
  bindModal(
    "open-payment-modal",
    "payment-modal",
    "close-payment-modal"
  );

});
