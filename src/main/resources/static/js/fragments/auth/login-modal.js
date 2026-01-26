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

$(document).ready(function () {
  const $form = $(".login-modal__form");
  const $errorBox = $("#loginError");

  if ($form.length === 0) return;

  $form.on("submit", function (e) {
    e.preventDefault(); // 기본 submit 막기

    // 에러 초기화
    $errorBox.text("").addClass("is-hidden");

    $.ajax({
      url: "/login",
      type: "POST",
      data: $form.serialize(),   // email, password 자동 직렬화
      xhrFields: {
        withCredentials: true    // JSESSIONID 유지
      },
      success: function () {
        // ✅ 로그인 성공 → SSR 반영
        location.reload();
      },
      error: function (xhr) {
        // ❌ 로그인 실패
        if (xhr.status === 401 && xhr.responseJSON) {
          $errorBox
            .text(xhr.responseJSON.message)
            .removeClass("is-hidden");
          return;
        }

        // 기타 예외
        $errorBox
          .text("로그인 처리 중 오류가 발생했습니다.")
          .removeClass("is-hidden");
      }
    });
  });
});

