/**
 * 지역 설정 모달 열기
 */
function openRegionModal() {
    // 1. 모달 요소를 찾아서 display를 flex로 변경
    const $modal = $('#regionModal');
    if ($modal.length > 0) {
        $modal.css('display', 'flex').hide().fadeIn(200); // 부드럽게 나타나기
        $('body').css('overflow', 'hidden'); // 뒷배경 스크롤 방지
    }
}

/**
 * 지역 설정 모달 닫기
 */
function closeRegionModal() {
    const $modal = $('#regionModal');
    $modal.fadeOut(200, function() {
        $(this).css('display', 'none');
        $('body').css('overflow', 'auto'); // 스크롤 복구
    });
}

// 문서 로드 시 이벤트 바인딩
$(document).ready(function() {
    // 모달 바깥 영역(어두운 부분) 클릭 시 닫기
    $(document).on('click', '#regionModal', function(e) {
        if ($(e.target).is('#regionModal')) {
            closeRegionModal();
        }
    });

    // ESC 키 누르면 닫기
    $(document).on('keydown', function(e) {
        if (e.key === "Escape") closeRegionModal();
    });
});






// ----------------------- 
// 1. 지역 검색하기
// ... (기존 openRegionModal, closeRegionModal 코드 유지) ...

$(document).ready(function() {
    let searchTimer;

    // 1. 검색 입력 이벤트
    $('.search-input').on('input', function() {
        const keyword = $(this).val().trim();
        const $container = $('#regionDataContainer'); // HTML에 추가한 ID와 일치해야 함

        clearTimeout(searchTimer);

        if (keyword.length < 2) {
            $container.html('<p style="text-align:center; padding:20px; color:#888;">2글자 이상 입력해주세요.</p>');
            return;
        }

        searchTimer = setTimeout(function() {
            $.ajax({
                url: '/api/regions/search',
                method: 'GET',
                data: { keyword: keyword },
                success: function(data) {
                    let html = '';
                    if (data.length === 0) {
                        html = '<p style="text-align:center; padding:20px;">검색 결과가 없습니다.</p>';
                    } else {
						data.forEach(region => {
						    const parentName = region.parent ? region.parent.regionName + ' ' : '';
						    const fullName = parentName + region.regionName; // 예: "서울특별시 강남구"

						    // [핵심] 여기서 DB 형식에 맞게 미리 글자를 깎습니다.
						    const dbFormatName = fullName.replace("서울특별시", "서울")
						                                 .replace("경기도", "경기")
						                                 .replace("인천광역시", "인천")
														 .replace("부산광역시", "부산");  
						                                 

						    html += `
						        <button type="button" class="region-item" 
						                onclick="selectRegion('${dbFormatName}', '${region.regionCode}')">
						            <span class="material-symbols-outlined">location_on</span>
						            <div class="region-info">
						                <span class="region-name">${fullName}</span> </div>
						        </button>`;
						});
                    }
                    $container.html(html);
                }
            });
        }, 300);
    });


});
