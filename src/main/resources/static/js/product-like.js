document.addEventListener("DOMContentLoaded", () => {

  document.querySelectorAll(".product-card__like-btn, .detail-like-btn")
    .forEach(button => {

      button.addEventListener("click", async (e) => {
        e.preventDefault();
        e.stopPropagation();

        const productId = button.dataset.productId;
        if (!productId) return;

        const icon = button.querySelector(".material-symbols-outlined");
        const countEl =
          button.closest(".product-card, .product-detail")
                ?.querySelector(".like-count");

        const wasLiked = icon.classList.contains("filled");

        // 🔥 1. UI 선반영
        icon.classList.toggle("filled");

        try {
          const res = await fetch(`/product/${productId}/like`, {
            method: "POST"
          });

          if (!res.ok) throw new Error();

          const result = await res.json();
          // { liked, likeCount }

          // 🔥 2. 서버 기준 동기화
          icon.classList.toggle("filled", result.liked);

          if (countEl) {
            countEl.textContent = result.likeCount;
          }

        } catch (e) {
          // 🔥 3. 실패 시 롤백
          icon.classList.toggle("filled", wasLiked);
          alert("로그인이 필요합니다.");
        }
      });
    });
});
