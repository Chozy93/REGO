$(function () {

  const $modal = $('#reviewWriteModal');
  const $form  = $('#reviewWriteForm');

  /* ===============================
     리뷰 모달 열기
  =============================== */
  $('.btn-review').on('click', function () {

    const productId    = $(this).data('product-id');
    const sellerId     = $(this).data('seller-id');
    const productTitle = $(this).data('product-title');
    const sellerName   = $(this).data('seller-name');

    // hidden 값 세팅 (ReviewConditionVO)
    $form.find('input[name="productId"]').val(productId);
    $form.find('input[name="sellerId"]').val(sellerId);
    $form.find('input[name="rating"]').val('');

    // 표시용 DOM
    $('.review-product__title').text(productTitle);
    $('.review-product__seller-name').text(sellerName);

    // 초기화
    $('.review-rating__star').removeClass('is-active');
    $form.find('textarea[name="content"]').val('');
    $('.review-textarea__count').text('0 / 1000');

    // 모달 오픈
    $modal.removeClass('is-hidden');
    $('body').css('overflow', 'hidden');
  });

  /* ===============================
     모달 닫기
  =============================== */
  function closeReviewModal() {
    $modal.addClass('is-hidden');
    $('body').css('overflow', '');
  }

  $('.review-modal__back-btn').on('click', closeReviewModal);
  $('.review-modal__backdrop').on('click', closeReviewModal);

  /* ===============================
     별 클릭 → rating (1~5 → 2~10)
  =============================== */
  $('.review-rating__star').on('click', function () {

    const index  = $(this).index();     // 0 ~ 4
    const rating = (index + 1) * 2;     // 2 ~ 10

    $form.find('input[name="rating"]').val(rating);

    $('.review-rating__star').removeClass('is-active');
    $('.review-rating__star').each(function (i) {
      if (i <= index) {
        $(this).addClass('is-active');
      }
    });
  });

  /* ===============================
     textarea 글자 수
  =============================== */
  $form.find('textarea[name="content"]').on('input', function () {
    const len = $(this).val().length;
    $('.review-textarea__count').text(len + ' / 1000');
  });

  /* ===============================
     리뷰 등록 (AJAX)
  =============================== */
  $form.on('submit', function (e) {
    e.preventDefault();

    const data = {
      productId: $form.find('input[name="productId"]').val(),
      sellerId:  $form.find('input[name="sellerId"]').val(),
      rating:    $form.find('input[name="rating"]').val(),
      content:   $form.find('textarea[name="content"]').val()
    };

    // 최소 검증
    if (!data.rating) {
      alert('별점을 선택해주세요.');
      return;
    }

    if (!data.content || data.content.length < 10) {
      alert('리뷰 내용은 최소 10자 이상 입력해주세요.');
      return;
    }

    $.ajax({
      url: '/reviews',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(data),
      success: function () {
        location.reload(); // SSR 기준
      },
      error: function () {
        alert('리뷰 등록 중 오류가 발생했습니다.');
      }
    });
  });

});
