// js/auth/login-modal.js

function openLoginModal() {
  document.getElementById("loginModal").classList.remove("is-hidden");
  document.body.style.overflow = "hidden";
}

function closeLoginModal() {
  document.getElementById("loginModal").classList.add("is-hidden");
  document.body.style.overflow = "";
}

/* 비밀번호 토글 */
document.addEventListener("click", (e) => {
  const btn = e.target.closest(".password-toggle");
  if (!btn) return;

  const input = document.getElementById("loginPassword");
  const icon = btn.querySelector("span");

  if (input.type === "password") {
    input.type = "text";
    icon.textContent = "visibility_off";
  } else {
    input.type = "password";
    icon.textContent = "visibility";
  }
});

/* 아이디 기억 */
document.addEventListener("DOMContentLoaded", () => {
  const email = document.getElementById("loginEmail");
  const remember = document.getElementById("rememberEmail");

  const saved = localStorage.getItem("savedEmail");
  if (saved) {
    email.value = saved;
    remember.checked = true;
  }

  document.querySelector(".login-modal__form")
    ?.addEventListener("submit", () => {
      remember.checked
        ? localStorage.setItem("savedEmail", email.value)
        : localStorage.removeItem("savedEmail");
    });
});


function openFindIdModalFromLogin() {
  closeLoginModal();
  openFindIdModal();
}

function openFindPasswordModalFromLogin() {
  closeLoginModal();
  openFindPasswordModal();
}
