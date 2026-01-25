$(document).ready(function () {
    let searchTimer;

    /* =========================
       상품 등록용 지역 모달 열기 / 닫기
    ========================= */
    window.openProductRegionModal = function () {
        const $modal = $('#productRegionModal');
        if ($modal.length) {
            $modal.css('display', 'flex').hide().fadeIn(200);
            $('body').css('overflow', 'hidden');
        }
    };

    window.closeProductRegionModal = function () {
        $('#productRegionModal').fadeOut(200, function () {
            $(this).css('display', 'none');
            $('body').css('overflow', 'auto');
        });
    };

    // 배경 클릭 시 닫기
    $(document).on('click', '#productRegionModal', function (e) {
        if ($(e.target).is('#productRegionModal')) {
            closeProductRegionModal();
        }
    });

    // ESC 닫기
    $(document).on('keydown', function (e) {
        if (e.key === 'Escape') closeProductRegionModal();
    });

    /* =========================
       지역 검색 (DB 기반 / GET만 사용)
    ========================= */
    $('.product-region-search-input').on('input', function () {
        const keyword = $(this).val().trim();
        const $container = $('#productRegionDataContainer');

        clearTimeout(searchTimer);

        if (keyword.length < 2) {
            $container.html(
                '<p style="text-align:center; padding:20px; color:#888;">2글자 이상 입력해주세요.</p>'
            );
            return;
        }

        searchTimer = setTimeout(function () {
            $.ajax({
                url: '/seller/product/regions/search',
                method: 'GET',
                data: { keyword },
                success: function (data) {
                    if (!data || data.length === 0) {
                        $container.html(
                            '<p style="text-align:center; padding:20px;">검색 결과가 없습니다.</p>'
                        );
                        return;
                    }

                    let html = '';
                    data.forEach(region => {
                        html += `
                            <button type="button"
                                    class="product-region-item"
                                    data-full-name="${region.displayName}"
                                    data-sido="${region.sidoCode || ''}"
                                    data-sigungu="${region.sigunguCode || ''}"
                                    data-eupmyeon="${region.eupmyeondongCode || ''}">
                                <span class="material-symbols-outlined">location_on</span>
                                <div class="product-region-info">
                                    <span class="product-region-name">${region.displayName}</span>
                                </div>
                            </button>
                        `;
                    });

                    $container.html(html);
                }
            });
        }, 300);
    });

    /* =========================
       검색 결과 클릭 → 값 세팅
    ========================= */
    $(document).on('click', '.product-region-item', function () {
        const fullName = $(this).data('full-name');

        // 화면 표시
        $('#selectedRegionLabel').text(fullName);

        // hidden input 세팅 (Product 테이블 그대로 대응)
        $('#regionSidoCode').val($(this).data('sido'));
        $('#regionSigunguCode').val($(this).data('sigungu'));
        $('#regionEupmyeondongCode').val($(this).data('eupmyeon'));
        $('#regionDisplayName').val(fullName);

        closeProductRegionModal();
    });
});
