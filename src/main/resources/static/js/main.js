// =========================================
// MAIN PAGE JS
// =========================================

document.addEventListener("DOMContentLoaded", () => {

  // ===============================
  // 1️⃣ 메인 카드 찜 버튼 처리
  // ===============================
  document.querySelectorAll(".product-card__like-btn").forEach(btn => {

    btn.addEventListener("click", async (e) => {
      e.preventDefault();
      e.stopPropagation();

      const productId = btn.dataset.productId;
      if (!productId) return;

      try {
        const res = await fetch(`/product/${productId}/like`, {
          method: "POST"
        });

        if (!res.ok) return;

        const result = await res.json();
        // result = { productId, likeCount, liked }

        // ❤️ 하트 아이콘
        const icon = btn.querySelector(".material-symbols-outlined");

        // 🔢 찜 개수 (버튼 바깥에 있음)
        const countEl = btn.closest(".product-card")
                           ?.querySelector(".like-count");

        if (icon) {
          icon.classList.toggle("filled", result.liked);
        }

        if (countEl) {
          countEl.textContent = result.likeCount;
        }

        // ✅ 상세 ↔ 메인 UX 동기화용
        localStorage.setItem("likeChanged", "true");

      } catch (err) {
        console.error("찜 처리 실패", err);
      }
    });
  });

  // ===============================
  // 2️⃣ 상세 → 메인 동기화
  // ===============================
  if (localStorage.getItem("likeChanged")) {
    localStorage.removeItem("likeChanged");
    location.reload();
  }

  // ===============================
  // 3️⃣ 정렬 변경
  // ===============================
  const sortSelect = document.getElementById("sortSelect");
  if (sortSelect) {
    sortSelect.addEventListener("change", () => {
      window.location.href = "/?sort=" + sortSelect.value;
    });
  }
});
