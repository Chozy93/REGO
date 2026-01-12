document.addEventListener("DOMContentLoaded", () => {
  const likeBtn = document.querySelector(".product-card__like-btn");
  if (!likeBtn) return;

  const icon = likeBtn.querySelector("span");
  const likeCountEl = document.getElementById("likeCount");

  likeBtn.addEventListener("click", async (e) => {
    e.preventDefault();
    e.stopPropagation();

    const productId = likeBtn.dataset.productId;
    if (!productId) return;

    const res = await fetch(`/product/${productId}/like`, {
      method: "POST"
    });

    if (!res.ok) return;

    const result = await res.json();

    icon.classList.toggle("filled", result.liked);
    if (likeCountEl) {
      likeCountEl.textContent = result.likeCount;
    }

    // ✅ 메인에 알림
    localStorage.setItem("likeChanged", "true");
  });
});
