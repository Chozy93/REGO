// 0️⃣ bfcache 대응
window.addEventListener("pageshow", function (event) {
  if (event.persisted) {
    window.location.reload();
  }
});

/**
 * 4️⃣ 지역 선택 처리
 * DOMContentLoaded 밖에 선언하여 모달 HTML의 onclick에서 즉시 참조 가능하게 함
 */
window.selectRegion = function(name, code) {
    if (!name) {
        console.error("지역명이 전달되지 않았습니다.");
        return;
    }

    // 현재 페이지의 URL 전체를 가져옴
    const url = new URL(window.location.href);

    // 기존 파라미터는 유지하거나 새로 세팅
    url.searchParams.set('region', name);   // 지역명 추가 (예: 서울특별시 강남구)
    url.searchParams.set('sort', 'recent'); // 정렬은 최신순으로 고정

    console.log("필터링 적용 URL:", url.toString());

    // 페이지 이동 (이 명령어가 실행되면 서버로 요청이 가고 페이지가 새로고침됨)
    window.location.href = url.toString();
};

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
