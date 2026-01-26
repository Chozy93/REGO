/* ==========================================================================
   Seller Product Register - Category (jQuery Tab)
   - 1차 카테고리: 탭 전환
   - 2차 카테고리: categoryId(FK) 선택
========================================================================== */

$(function () {

  const $mainTabs = $(".spr-category__main .spr-chip");
  const $subBoxes = $(".spr-category__sub-box");
  const $inputCategoryId = $("input[name='categoryId']");

  /* =========================
     1차 카테고리 탭 클릭
  ========================= */
  $mainTabs.on("click", function () {

    const parentId = $(this).data("parentId");

    /* 탭 active */
    $mainTabs.removeClass("spr-chip--active");
    $(this).addClass("spr-chip--active");

    /* 패널 전환 */
    $subBoxes.removeClass("is-active");
    $subBoxes
      .filter('[data-parent-id="' + parentId + '"]')
      .addClass("is-active");

    /* 2차 선택 초기화 */
    $inputCategoryId.val("");
    $(".spr-subchip").removeClass("is-active");
  });

  /* =========================
     2차 카테고리 선택
  ========================= */
  $(".spr-category__sub").on("click", ".spr-subchip", function () {

    $(".spr-subchip").removeClass("is-active");
    $(this).addClass("is-active");

    /* FK 세팅 */
    $inputCategoryId.val($(this).data("categoryId"));
  });

});


/* ======================================================================
   Seller Product Register JS
   - Image preview / delete / 대표 변경
   - Validation before submit
====================================================================== */

$(function () {

  /* ==================================================
     Image Handling
  ================================================== */
  const $imageInput = $('#imageInput');
  const $imagePreview = $('#imagePreview');
  const $imageCount = $('#imageCount');

  // 실제 전송될 이미지 리스트 (순서 중요)
  let imageFiles = [];

  function renderImages() {
    $imagePreview.empty();
    $imageCount.text(imageFiles.length);

    imageFiles.forEach((file, idx) => {
      const reader = new FileReader();

      reader.onload = function (e) {
        const isMain = idx === 0;

        const $thumb = $(`
          <div class="spr-image-thumb" data-index="${idx}">
            ${isMain ? `<span class="spr-image-thumb__badge">대표</span>` : ``}
            <img src="${e.target.result}" alt="">
            <button type="button" class="spr-image-thumb__remove">×</button>
          </div>
        `);

        $imagePreview.append($thumb);
      };

      reader.readAsDataURL(file);
    });
  }

  // 파일 선택
  $imageInput.on('change', function () {
    const files = Array.from(this.files);

    if (files.length + imageFiles.length > 10) {
      alert('이미지는 최대 10장까지 업로드할 수 있습니다.');
      this.value = '';
      return;
    }

    files.forEach(file => {
      if (file.type.startsWith('image/')) {
        imageFiles.push(file);
      }
    });

    this.value = '';
    renderImages();
  });

  // 이미지 삭제
  $imagePreview.on('click', '.spr-image-thumb__remove', function () {
    const idx = Number($(this).closest('.spr-image-thumb').data('index'));
    imageFiles.splice(idx, 1);
    renderImages();
  });

  // 대표 이미지 변경 (클릭 시 0번으로 이동)
  $imagePreview.on('click', '.spr-image-thumb img', function () {
    const idx = Number($(this).closest('.spr-image-thumb').data('index'));
    if (idx === 0) return;

    const selected = imageFiles.splice(idx, 1)[0];
    imageFiles.unshift(selected);
    renderImages();
  });

  /* ==================================================
     Category Handling
  ================================================== */
  $('.spr-subchip').on('click', function () {
    $('.spr-subchip').removeClass('is-active');
    $(this).addClass('is-active');

    $('input[name="categoryId"]').val($(this).data('category-id'));
  });

  /* ==================================================
     Form Validation
  ================================================== */
  $('.seller-product-register__form').on('submit', function (e) {

    // 제목
    if (!$('#productTitle').val().trim()) {
      alert('상품 제목을 입력해주세요.');
      $('#productTitle').focus();
      e.preventDefault();
      return;
    }

    // 카테고리
    if (!$('input[name="categoryId"]').val()) {
      alert('카테고리를 선택해주세요.');
      e.preventDefault();
      return;
    }

    // 상품 상태
    if (!$('input[name="conditionStatus"]:checked').length) {
      alert('상품 상태를 선택해주세요.');
      e.preventDefault();
      return;
    }

    // 가격
    const price = $('input[name="price"]').val();
    if (!price || Number(price) <= 0) {
      alert('가격을 올바르게 입력해주세요.');
      $('input[name="price"]').focus();
      e.preventDefault();
      return;
    }

    // 설명
    if (!$('#detail').val().trim()) {
      alert('상품 설명을 입력해주세요.');
      $('#detail').focus();
      e.preventDefault();
      return;
    }

    // 거래 방식
    const tradeType = $('input[name="tradeType"]:checked').val();
    if (!tradeType) {
      alert('거래 방식을 선택해주세요.');
      e.preventDefault();
      return;
    }

    // 지역 (직거래 or ALL 일 때 필수)
    if (tradeType === 'DIRECT' || tradeType === 'ALL') {
      if (!$('#regionSidoCode').val()) {
        alert('직거래를 선택한 경우 거래 희망 지역을 설정해주세요.');
        e.preventDefault();
        return;
      }
    }

    /* ==========================================
       Multipart 이미지 재구성
       - 기존 input[name="images"] 제거
       - 순서 유지해서 다시 append
    ========================================== */
    if (imageFiles.length > 0) {
      const $form = $(this);
      $form.find('input[name="images"]').remove();

      imageFiles.forEach(file => {
        const $fileInput = $('<input>')
          .attr('type', 'file')
          .attr('name', 'images')
          .prop('files', createFileList(file))
          .css('display', 'none');

        $form.append($fileInput);
      });
    }
  });

  // FileList 생성 (브라우저 호환)
  function createFileList(file) {
    const dataTransfer = new DataTransfer();
    dataTransfer.items.add(file);
    return dataTransfer.files;
  }

});

document.addEventListener('DOMContentLoaded', function () {

  const priceInput = document.querySelector('input[name="price"]');

  const priceAutoNumeric = new AutoNumeric(priceInput, {
    digitGroupSeparator: ',',
    decimalCharacter: '.',
    decimalPlaces: 0,          // 정수만
    allowDecimalPadding: false,
    minimumValue: '0',
    maximumValue: '1000000000', // 10억 제한
  });

  // submit 시 raw value로 자동 변환
  priceInput.closest('form').addEventListener('submit', function () {
    priceAutoNumeric.unformat();
  });

});

