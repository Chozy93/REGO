// detail.js
// ==================================================
// 상세 페이지 전용 JS
// - 로그인 UX 가드
// - 찜 토글
// - 채팅(거래) 버튼 → 채팅 담당자 영역으로 연결
// ==================================================


/* ==============================
   🔐 공통 로그인 가드 (UX 전용)
============================== */
function requireLogin(target) {
  if (!target) return false;

  const isLogin = target.dataset.login === "true";
  if (!isLogin) {
    alert("로그인이 필요합니다.");
    location.href = "/login";
    return false;
  }
  return true;
}


/* ==============================
   DOM Ready
============================== */
document.addEventListener("DOMContentLoaded", () => {

  console.log("🔥 detail.js loaded");


  /* ==================================================
     ❤️ 찜 버튼 (메인 카드 + 상세 페이지 공통)
     - .product-card__like-btn : 메인
     - .product-like-btn       : 상세
  ================================================== */
  document.addEventListener("click", async (e) => {

    const likeBtn = e.target.closest(
      ".product-card__like-btn, .product-like-btn"
    );
    if (!likeBtn) return;

    e.preventDefault();
    e.stopPropagation();
	
	// 🚫 본인 상품 차단 (JS 2차 방어)
	  if (likeBtn.dataset.mine === "true") {
	    alert("내 상품은 찜할 수 없습니다.");
	    return;
	  }

    // 🔐 로그인 가드
    if (!requireLogin(likeBtn)) return;

    const productId = likeBtn.dataset.productId;
    if (!productId) return;

    const icon = likeBtn.querySelector("span");
    const likeCountEl =
      likeBtn.closest(".product-detail, .product-card")
        ?.querySelector(".like-count");

    try {
      const res = await fetch(`/product/${productId}/like`, {
        method: "POST"
      });

      if (!res.ok) throw new Error("like failed");

      const result = await res.json();
      // { liked: boolean, likeCount: number }

      // ✅ 서버 기준 UI 동기화
      if (icon) {
        icon.classList.toggle("filled", result.liked);
      }

      if (likeCountEl) {
        likeCountEl.textContent = result.likeCount;
      }

    } catch (err) {
      console.error(err);
      alert("찜 처리 중 오류가 발생했습니다.");
    }
  });


  /* =========================
     💬 채팅하기 버튼
     - 채팅 담당자 영역으로 연결만 담당
  ========================= */
  const dealBtn = document.getElementById("dealBtn");

  if (dealBtn) {
    dealBtn.addEventListener("click", (e) => {

      // 1차 차단
      if (dealBtn.disabled) {
        e.preventDefault();
        e.stopPropagation();
        return;
      }

      // 2차 차단: 본인 상품
      if (dealBtn.dataset.mine === "true") {
        e.preventDefault();
        return;
      }

      // 로그인 가드
      if (!requireLogin(e.currentTarget)) return;

      // 🔥🔥🔥 핵심 한 줄 (이게 빠져 있었음)
      const productId = dealBtn.dataset.productId;

      console.log("채팅 시작 productId =", productId);

      // 정상 이동
      location.href = `/chat/start/${productId}`;
    });
  }

  
  (() => {
    const slider = document.getElementById('detailImageSlider');
    if (!slider) return;

    const track = slider.querySelector('.slider-track');
    const dots = slider.querySelectorAll('.dot');
    const total = dots.length;

    if (!track || total === 0) return;

    let index = 0;

    function moveSlide(i) {
      index = (i + total) % total;
      track.style.transform = `translateX(-${index * 100}%)`;

      dots.forEach(d => d.classList.remove('active'));
      dots[index].classList.add('active');
    }

    /* ▶ 좌 / 우 버튼 */
    slider.querySelector('.next')
      ?.addEventListener('click', () => moveSlide(index + 1));

    slider.querySelector('.prev')
      ?.addEventListener('click', () => moveSlide(index - 1));

    /* 🔘 dot 클릭 이동 (추가된 부분) */
    dots.forEach((dot, i) => {
      dot.addEventListener('click', () => {
        moveSlide(i);
      });
    });
  })();

});
