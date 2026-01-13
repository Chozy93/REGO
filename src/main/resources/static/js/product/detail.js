// ==============================
// 공통 로그인 가드 (UX 전용)
// ==============================
function requireLogin(target) {
  const isLogin = target?.dataset?.login === "true";

  if (!isLogin) {
    alert("로그인이 필요합니다.");
    return false;
  }
  return true;
}

// ==============================
// DOM Ready
// ==============================
document.addEventListener("DOMContentLoaded", () => {

  console.log("🔥 detail.js loaded");

  /* =========================
     ❤️ 찜 버튼
  ========================= */
  const likeBtn = document.querySelector(".product-card__like-btn");
  if (likeBtn) {
    const icon = likeBtn.querySelector("span");
    const likeCountEl = document.querySelector(".like-count");

    likeBtn.addEventListener("click", async (e) => {
      e.preventDefault();
      e.stopPropagation();

      if (!requireLogin(e.currentTarget)) return;

      const productId = likeBtn.dataset.productId;
      if (!productId) return;

      try {
        const res = await fetch(`/product/${productId}/like`, {
          method: "POST"
        });

        if (!res.ok) return;

        const result = await res.json();
        icon.classList.toggle("filled", result.liked);

        if (likeCountEl) {
          likeCountEl.textContent = result.likeCount;
        }

        localStorage.setItem("likeChanged", "true");

      } catch (err) {
        console.error(err);
      }
    });
  }

  /* =========================
     💬 거래 버튼
  ========================= */
  const dealBtn = document.getElementById("dealBtn");
  if (dealBtn) {
    dealBtn.addEventListener("click", (e) => {
      if (!requireLogin(e.currentTarget)) return;
      console.log("거래 요청 클릭");
    });
  }

  /* =========================
     🚨 신고 모달 제어
  ========================= */
  const reportOpenBtn   = document.getElementById("reportOpenBtn");
  const reportModal     = document.getElementById("reportModal");
  const reportCancelBtn = document.getElementById("reportCancelBtn");
  const reportCloseBtn  = document.getElementById("reportCloseBtn");
  const reportForm      = document.getElementById("reportForm");

  /* 신고 모달 열기 */
  if (reportOpenBtn && reportModal) {
    reportOpenBtn.addEventListener("click", (e) => {
      if (!requireLogin(e.currentTarget)) return;
      reportModal.classList.remove("is-hidden");
    });
  }

  /* ❌ 닫기 버튼 */
  if (reportCloseBtn && reportModal) {
    reportCloseBtn.addEventListener("click", () => {
      reportModal.classList.add("is-hidden");
    });
  }

  /* ❌ 취소 버튼 */
  if (reportCancelBtn && reportModal) {
    reportCancelBtn.addEventListener("click", () => {
      reportModal.classList.add("is-hidden");
    });
  }

  /* ❌ 배경 클릭 시 닫기 */
  if (reportModal) {
    reportModal.addEventListener("click", (e) => {
      if (e.target.classList.contains("modal-backdrop")) {
        reportModal.classList.add("is-hidden");
      }
    });
  }

  /* 🔥 내부 패널 클릭 시 닫히지 않게 */
  const modalPanel = reportModal?.querySelector(".modal-panel");
  if (modalPanel) {
    modalPanel.addEventListener("click", (e) => {
      e.stopPropagation();
    });
  }


  /* =========================
     🚨 신고 submit
  ========================= */
  if (reportForm) {
    reportForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      e.stopPropagation(); // ⭐ submit 전파 차단 (중요)

      if (reportForm.dataset.login !== "true") {
        alert("로그인이 필요합니다.");
        return;
      }

      const productId = reportForm.dataset.productId;
      const checked = reportForm.querySelector("input[name='reason']:checked");

      if (!checked) {
        alert("신고 사유를 선택해주세요.");
        return;
      }

      try {
        const res = await fetch(`/product/${productId}/report`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            reasonCode: checked.value
          })
        });

        const result = await res.json();

        // ❌ 실패 (이미 신고한 경우)
        if (!result.success) {
          alert(result.message);

          reportModal.classList.add("is-hidden");

          if (reportOpenBtn) {
            reportOpenBtn.textContent = "신고완료";
            reportOpenBtn.classList.add("is-disabled");
            reportOpenBtn.disabled = true;
          }
          return;
        }

        // ✅ 성공
        alert("신고가 접수되었습니다.");

        reportForm.reset();
        reportModal.classList.add("is-hidden");

        if (reportOpenBtn) {
          reportOpenBtn.textContent = "신고완료";
          reportOpenBtn.classList.add("is-disabled");
          reportOpenBtn.disabled = true;
        }

      } catch (err) {
        console.error(err);
        alert("신고 처리 중 오류가 발생했습니다.");
      }
    });
  }


 }); // ✅ 이 줄이 반드시 필요 

