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


// swiper
document.addEventListener('DOMContentLoaded', function() {
  // Swiper 초기화
  const swiper = new Swiper('.detail-swiper', {
    // 기본 파라미터
    slidesPerView: 1,
    spaceBetween: 0,
    loop: true, // 이미지가 1장일 때는 작동 안 할 수 있으니 주의

    // 페이지네이션 (점)
    pagination: {
      el: '.swiper-pagination',
      clickable: true,
    },

    // 화살표 내비게이션
    navigation: {
      nextEl: '.swiper-button-next',
      prevEl: '.swiper-button-prev',
    },
    
    // 마우스 드래그 허용
    grabCursor: true,
  });
});

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


// 바로결제 이동
document.getElementById("directPayBtn")?.addEventListener("click", function() {
    // 1. 본인 상품인지 체크 (Thymeleaf 변수 활용)
	const isMine = /*[[${page.mine}]]*/ false; 
	const isLogin = /*[[${page.login}]]*/ false;
    if (isMine) {
        alert("본인 상품은 구매할 수 없습니다.");
        return;
    }
	
	if (isMine) {
	        alert("본인 상품은 구매할 수 없습니다.");
	        return;
	    }

    // 2. 상품 ID 가져오기
    const productId = this.dataset.productId;

    // 3. 결제 페이지(GET /direct)로 주소 이동
    // 결과 주소 예시: /direct?productId=123
    location.href = `/direct?productId=${productId}`;
});


// 카카오 맵 표시하기
document.addEventListener("DOMContentLoaded", function() {
    const mapContainer = document.getElementById('map');
    if (!mapContainer) return;

    // 1. DB에서 가져온 지역 텍스트 추출 (예: "부산진구 양정동")
    const regionText = document.getElementById('tradeRegion').innerText;

    const mapOption = {
        center: new kakao.maps.LatLng(35.1595, 129.0602), // 주소 찾기 전 기본 위치 (부산진구)
        level: 3 
    };

    const map = new kakao.maps.Map(mapContainer, mapOption);
    const geocoder = new kakao.maps.services.Geocoder();

    // 2. 주소로 좌표 검색
    geocoder.addressSearch(regionText, function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            const coords = new kakao.maps.LatLng(result[0].y, result[0].x);

            // 마커 표시
            new kakao.maps.Marker({
                map: map,
                position: coords
            });

            // 중심 이동
            map.setCenter(coords);
            
            // 3. 중고거래 특성상 지도는 보기만 하도록 설정 (옵션)
            map.setDraggable(false); 
            map.setZoomable(false);
        } else {
            console.error("지도를 불러올 수 없는 주소입니다.");
            mapContainer.style.display = 'none'; // 주소가 이상하면 지도 숨김
        }
    });
});
