/* =========================
   ❤️ 상품 찜 토글 (공통)
   - 메인 / 상세 공용
   - 서버 기준 UI 동기화
========================= */

/* =========================
   🔁 상세 → 메인 복귀 시 동기화
========================= */
document.addEventListener("DOMContentLoaded", () => {
  const likeChanged = localStorage.getItem("likeChanged");

  if (likeChanged === "true") {
    localStorage.removeItem("likeChanged");
    // 서버 기준으로 메인 카드 재렌더
    location.reload();
  }
});

/* =========================
   ❤️ 찜 버튼 클릭 (이벤트 위임)
========================= */
document.addEventListener("click", async (e) => {
  const button = e.target.closest(".product-card__like-btn");
  if (!button) return;

  e.preventDefault();
  e.stopPropagation();

  const productId = button.dataset.productId;
  if (!productId) return;

  await toggleLike(productId, button);
});

/* =========================
   ❤️ 찜 토글 로직 (서버 기준)
========================= */
async function toggleLike(productId, button) {
  const icon = button.querySelector(".material-symbols-outlined");
  if (!icon) return;

  const countEl =
    button
      .closest(".product-card, .product-detail, .action-card")
      ?.querySelector(".like-count");

  // 실패 복구용 이전 상태
  const wasLiked = icon.classList.contains("filled");

  try {
    const res = await fetch(`/product/${productId}/like`, {
      method: "POST",
    });

    if (!res.ok) throw new Error("LIKE_FAILED");

    const result = await res.json();
    // { liked: boolean, likeCount: number }

    // ✅ 서버 기준 UI 동기화 (핵심)
    icon.classList.toggle("filled", result.liked);
    if (countEl) {
      countEl.textContent = result.likeCount;
    }

    // 🔔 상세 페이지에서 변경되었음을 메인에 알림
    localStorage.setItem("likeChanged", "true");

  } catch (e) {
    // ❗ 실패 시 원래 상태로 복구
    icon.classList.toggle("filled", wasLiked);

    if (countEl) {
      const current = parseInt(countEl.textContent, 10) || 0;
      countEl.textContent = wasLiked ? current + 1 : current - 1;
    }
  }
}
