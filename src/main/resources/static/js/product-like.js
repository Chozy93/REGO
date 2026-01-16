async function toggleLike(productId, button) {
    if (!productId) return;

    const icon = button.querySelector(".material-symbols-outlined");
    const countEl =
        button.closest(".product-card, .product-detail")
              ?.querySelector(".like-count");

    const wasLiked = icon.classList.contains("filled");

    /* =========================
       1️⃣ UI 선반영 (핵심)
    ========================= */
    icon.classList.toggle("filled");

    if (countEl) {
        const current = parseInt(countEl.textContent, 10) || 0;
        countEl.textContent = wasLiked ? current - 1 : current + 1;
    }

    try {
        /* =========================
           2️⃣ 서버 요청
        ========================= */
        const res = await fetch(`/product/${productId}/like`, {
            method: "POST"
        });

        if (!res.ok) throw new Error();

        const result = await res.json();
        // { liked, likeCount }

        /* =========================
           3️⃣ 서버 기준 최종 동기화
        ========================= */
        icon.classList.toggle("filled", result.liked);

        if (countEl) {
            countEl.textContent = result.likeCount;
        }

    } catch (e) {
        /* =========================
           4️⃣ 실패 시 롤백
        ========================= */
        icon.classList.toggle("filled", wasLiked);

        if (countEl) {
            const current = parseInt(countEl.textContent, 10) || 0;
            countEl.textContent = wasLiked ? current + 1 : current - 1;
        }

        // alert("로그인이 필요합니다."); // 로그인 붙일 때 활성화
    }
}
