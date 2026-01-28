let currentCategory = 'ALL';

function filterCategory(categoryCode) {
    currentCategory = categoryCode;

    /* =========================
       탭 active 처리
    ========================= */
    $('.tag').removeClass('active');

    $('.tag').each(function () {
        if ($(this).attr('onclick')?.includes(categoryCode)) {
            $(this).addClass('active');
        }
    });

    applyFilters();
}

function filterFaq() {
    applyFilters();
}

/* =========================
   카테고리 + 검색 통합 필터
========================= */
function applyFilters() {
    const keyword = $('#faqSearch').val().toLowerCase();
    let visibleCount = 0;

    $('.faq-item').each(function () {
        const itemCategory = $(this).data('category');
        const questionText = $(this).find('.question').text().toLowerCase();

        const matchCategory =
            currentCategory === 'ALL' || itemCategory === currentCategory;

        const matchKeyword =
            keyword === '' || questionText.includes(keyword);

        if (matchCategory && matchKeyword) {
            $(this).show();
            visibleCount++;
        } else {
            $(this).hide();
        }
    });

    /* =========================
       결과 없음 처리
    ========================= */
    $('#noResult').toggle(visibleCount === 0);
}
