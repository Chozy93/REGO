$(document).ready(function() {
    const savedRegion = localStorage.getItem('lastSelectedRegion');
    
    if (savedRegion) {
        // 1. 모달 열기
        openRegionModal(); 

        // 2. 약간의 시간차를 두고 값 주입 및 검색 실행
        setTimeout(function() {
            const $searchInput = $('.search-input');
            
            // 서버에서 만든 "부산광역시 부산진구" (또는 "서울특별시 강남구")가 그대로 들어감
            $searchInput.val(savedRegion.trim()); 
            
            // 3. 검색 이벤트 발생 -> 아래에 리스트 출력됨
            $searchInput.trigger('input'); 
            
            localStorage.removeItem('lastSelectedRegion');
        }, 150); 
    }
});
// --- 여기서부터 기존의 모달 열기/닫기 및 GPS 코드들이 쭉 이어지면 됩니다 ---

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


/**
 * 현재 위치 GPS 좌표 가져오기
 */
function getCurrentLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            const lat = position.coords.latitude;
            const lon = position.coords.longitude;
			
			console.log("좌표 획득 성공:", lat, lon);

		 // 🚩 체크: kakao.maps.services가 로드되었는지 콘솔에 찍어보세요
			console.log("카카오 서비스 상태:", typeof kakao !== 'undefined' && kakao.maps.services);

            // 🚩 kakao.maps.services가 있는지 확인 (가장 확실한 체크)
            if (typeof kakao !== 'undefined' && kakao.maps && kakao.maps.services) {
                getAddr(lat, lon);
            } else {
                // 만약 아직 안 불러와졌다면 0.5초 뒤에 자동으로 다시 실행
                console.log("지도 엔진 로딩 중... 0.5초 후 재시도");
                setTimeout(function() {
                    getCurrentLocation(); 
                }, 500);
            }
        }, function(error) {
            alert("위치 권한을 허용해주세요.");
        });
    }
}

function getAddr(lat, lon) {
    const geocoder = new kakao.maps.services.Geocoder();
    const coord = new kakao.maps.LatLng(lat, lon);
    
    geocoder.coord2RegionCode(coord.getLng(), coord.getLat(), function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            for (let i = 0; i < result.length; i++) {
                if (result[i].region_type === 'H') { // 행정동 기준
                    const kakaoCode = result[i].code;
                    
                    // 서버로 코드를 보내 DB 정보를 먼저 가져옵니다.
                    fetchRegionFromDB(kakaoCode);
                    break;
                }
            }
        }
    });
}

/**
 * 서버에 선택된 지역 저장 및 화면 반영
 * @param {string} name - 가공된 지역명 (예: '부산 부산진구')
 * @param {string} code - 10자리 지역 코드 (예: '2611000000')
 */

// 카카오에서 받은 주소 10자리 코드를 서버로 보냄
function selectRegion(name, code) {
    $.ajax({
        url: '/api/regions/select',
        method: 'POST',
        // 서버 컨트롤러의 @RequestParam 명칭과 일치해야 합니다.
        data: { 
            regionName: name, 
            regionCode: code 
        },
        success: function(response) {
            // response는 서버에서 보낸 Region 엔티티/DTO 객체입니다.
            // 엔티티의 필드명이 regionName인지 확인하세요!
            const savedName = response.regionName || name;
            
            alert(`${savedName} 지역이 설정되었습니다.`);
            
            // 세션에 저장된 값을 레이아웃(헤더 등)에 반영하기 위해 페이지 새로고침
            location.reload(); 
        },
        error: function(xhr) {
            console.error("지역 저장 실패:", xhr.responseText);
            alert("지역 정보를 저장하지 못했습니다. 다시 시도해주세요.");
        }
    });
}

function getAddr(lat, lon) {
    const geocoder = new kakao.maps.services.Geocoder();
    const coord = new kakao.maps.LatLng(lat, lon);
    
    geocoder.coord2RegionCode(coord.getLng(), coord.getLat(), function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            for (let i = 0; i < result.length; i++) {
                if (result[i].region_type === 'B') { // 법정동 기준
                    const kakaoCode = result[i].code;
                    
                    // 서버로 코드를 보내 DB 정보를 먼저 가져옵니다.
					console.log(kakaoCode);
					fetchRegionFromDB(kakaoCode);
					
                    break;
                }
            }
        }
    });
}

function fetchRegionFromDB(code) {
    $.ajax({
        url: '/api/regions/select',
        method: 'POST',
        data: { regionCode: code },
		success: function(dto) {
		    console.log("받은 DTO 데이터:", dto);

		    // DTO의 필드명 그대로 사용
		    let fullName = dto.fullName;


		    if (confirm(`'${fullName}' 지역이 맞나요?`)) {
				// 🚩 로컬 스토리지에 지역명 저장
				localStorage.setItem('lastSelectedRegion', fullName);
		        alert(fullName + " 지역으로 설정되었습니다.");
		        location.reload();
		    }
		},
        error: function(xhr) {
            console.error("에러 발생:", xhr);
            alert("해당 지역 정보를 DB에서 찾을 수 없습니다.");
        }
    });
}