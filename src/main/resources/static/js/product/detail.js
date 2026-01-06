document.addEventListener("DOMContentLoaded", () => {
    console.log("DETAIL JS LOADED");

    const likeBtn = document.querySelector(".product-card__like-btn");
    if (!likeBtn) return;

    const icon = likeBtn.querySelector(".material-symbols-outlined");
    const likeCountEl = document.getElementById("likeCount");

    likeBtn.addEventListener("click", async (e) => {
        e.preventDefault();
        e.stopPropagation();

        const productId = likeBtn.dataset.productId;
        if (!productId) return;

        try {
            const response = await fetch(`/product/${productId}/like`, {
                method: "POST"
            });

            // 🔥 로그인 안 돼도 UI는 바뀌게 (지금 단계 핵심)
            if (response.status === 401) {
                alert("로그인이 필요합니다.");

                // ❤️ 하트 토글
                icon.classList.toggle("filled");

                // 🔢 숫자 토글
                if (likeCountEl) {
                    const current = parseInt(likeCountEl.textContent, 10);
                    likeCountEl.textContent =
                        icon.classList.contains("filled")
                            ? current + 1
                            : current - 1;
                }
                return;
            }

            if (!response.ok) {
                throw new Error("Server Error");
            }

            // (로그인 붙은 뒤에만 사용)
            const result = await response.json();

            if (result.liked) {
                icon.classList.add("filled");
            } else {
                icon.classList.remove("filled");
            }

            if (likeCountEl) {
                likeCountEl.textContent = result.likeCount;
            }

        } catch (err) {
            console.error("찜 처리 실패", err);
        }
    });
});
