// 0️⃣ bfcache 대응
window.addEventListener("pageshow", function (event) {
  if (event.persisted) {
    window.location.reload();
  }
});

document.addEventListener("DOMContentLoaded", () => {

  // 1️⃣ 메인 카드 찜 버튼 처리
  document.querySelectorAll(".product-card__like-btn").forEach(btn => {
    btn.addEventListener("click", async (e) => {
      e.preventDefault();
      e.stopPropagation();

      const productId = btn.dataset.productId;
      if (!productId) return;

      const res = await fetch(`/product/${productId}/like`, {
        method: "POST"
      });

      if (!res.ok) return;

      const result = await res.json();

      const icon = btn.querySelector(".material-symbols-outlined");
      const countEl = btn.closest(".product-card")
                         ?.querySelector(".like-count");
						

						 btn.classList.toggle("liked", result.liked);

						 if (icon) {
						   icon.classList.toggle("filled", result.liked);
						 }

      if (countEl) countEl.textContent = result.likeCount;
    });
  });

  // 3️⃣ 정렬 변경
  const sortSelect = document.getElementById("sortSelect");
  if (sortSelect) {
    sortSelect.addEventListener("change", () => {
      window.location.href = "/?sort=" + sortSelect.value;
    });
  }
});
