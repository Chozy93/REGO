// ==============================
// 공통 로그인 가드 (UX 전용)
// ==============================
function requireLogin(target) {
  if (!target) return false;

  const isLogin = target.dataset.login === "true";

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

  /* ==================================================
     ❤️ 찜 버튼 (메인 + 상세 공통)
     - .product-card__like-btn (메인)
     - .product-like-btn        (상세)
  ================================================== */
  document.addEventListener("click", async (e) => {
    const likeBtn = e.target.closest(
      ".product-card__like-btn, .product-like-btn"
    );
    if (!likeBtn) return;

    e.preventDefault();
    e.stopPropagation();

    // 🔐 로그인 가드 (가장 먼저)
    if (!requireLogin(likeBtn)) return;

    const productId = likeBtn.dataset.productId;
    if (!productId) return;

    const icon = likeBtn.querySelector("span");
    const likeCountEl =
      likeBtn.closest(".product-detail, .product-card")
             ?.querySelector(".like-count");

    try {
      const res = await fetch(`/product/${productId}/like`, {
        method: "POST"
      });

      if (!res.ok) throw new Error("like failed");

      const result = await res.json();
      // { liked, likeCount }

      // ✅ UI 동기화 (서버 기준)
      if (icon) {
        icon.classList.toggle("filled", result.liked);
      }
      if (likeCountEl) {
        likeCountEl.textContent = result.likeCount;
      }

    } catch (err) {
      console.error(err);
      alert("찜 처리 중 오류가 발생했습니다.");
    }
  });

  /* =========================
     💬 거래 버튼
  ========================= */
  const dealBtn = document.getElementById("dealBtn");
  if (dealBtn) {
    dealBtn.addEventListener("click", (e) => {

      // 로그인 가드
      if (!requireLogin(e.currentTarget)) return;

      console.log("거래 요청 클릭");
      // TODO: DETAIL01_DEAL_REQ
    });
  }

});
