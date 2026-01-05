document.addEventListener("DOMContentLoaded", () => {

    const likeButtons = document.querySelectorAll(".product-card__like-btn");

    likeButtons.forEach(button => {

        button.addEventListener("click", async (e) => {
            e.preventDefault();
            e.stopPropagation();

            const productId = button.dataset.productId;
            if (!productId) return;

            const icon = button.querySelector("span");

            // 🔥 1. 현재 상태 저장
            const wasLiked = icon.classList.contains("filled");

            // 🔥 2. UI 먼저 토글 (낙관적 업데이트)
            icon.classList.toggle("filled");

            try {
                const response = await fetch(`/product/${productId}/like`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    }
                });

                if (!response.ok) {
                    throw new Error("서버 응답 실패");
                }

                const result = await response.json();
                // result: { liked: true/false }

                // 🔥 3. 서버 상태와 UI 동기화
                icon.classList.toggle("filled", result.liked);

            } catch (error) {
                console.error("찜 처리 오류:", error);

                // 🔥 4. 실패 시 UI 롤백
                icon.classList.toggle("filled", wasLiked);

                alert("로그인이 필요합니다.");
            }
        });
    });
});
