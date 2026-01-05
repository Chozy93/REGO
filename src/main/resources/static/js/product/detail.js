document.addEventListener("DOMContentLoaded", () => {

    const likeBtn = document.querySelector(".product-like-btn");
    if (!likeBtn) return;

    likeBtn.addEventListener("click", async (e) => {
        e.preventDefault();

        const productId = likeBtn.dataset.productId;
        if (!productId) return;

        try {
            const response = await fetch(`/product/${productId}/like`, {
                method: "POST"
            });

            if (response.status === 401) {
                alert("로그인이 필요합니다.");
                return;
            }

            const result = await response.json();
            // result = { liked: true, likeCount: 25 }

            const icon = document.getElementById("detailLikeIcon");
            const count = document.getElementById("likeCount");

            if (result.liked) {
                icon.classList.add("filled");
            } else {
                icon.classList.remove("filled");
            }

            count.textContent = result.likeCount;

        } catch (e) {
            console.error("찜 처리 실패", e);
        }
    });
});
