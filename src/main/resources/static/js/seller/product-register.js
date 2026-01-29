/* ==========================================================================
   Seller Product Register JS (REGISTER ONLY)
   - Category Tab
   - Image Upload / Preview (최대 10장)
   - AutoNumeric Price
   - GPT Price Recommend
   - Form Validation
========================================================================== */

let priceAutoNumeric;

$(function () {

  /* ==================================================
     Category (1차 / 2차)
  ================================================== */
  const $mainTabs = $(".spr-category__main .spr-chip");
  const $subBoxes = $(".spr-category__sub-box");
  const $inputCategoryId = $("input[name='categoryId']");

  // 1차 카테고리
  $mainTabs.on("click", function () {
    const parentId = $(this).data("parentId");

    $mainTabs.removeClass("spr-chip--active");
    $(this).addClass("spr-chip--active");

    $subBoxes.removeClass("is-active");
    $subBoxes.filter(`[data-parent-id="${parentId}"]`).addClass("is-active");

    $inputCategoryId.val("");
    $(".spr-subchip").removeClass("is-active");
  });

  // 2차 카테고리
  $(".spr-category__sub").on("click", ".spr-subchip", function () {
    $(".spr-subchip").removeClass("is-active");
    $(this).addClass("is-active");
    $inputCategoryId.val($(this).data("categoryId"));
  });

  /* ==================================================
     Image Upload / Preview (REGISTER ONLY)
  ================================================== */
  const $imageInput = $('#imageInput');
  const $imagePreview = $('#imagePreview');
  const $imageCount = $('#imageCount');

  let imageFiles = [];

  function updateImageCount() {
    $imageCount.text(imageFiles.length);
  }

  function renderImages() {
    $imagePreview.empty();
    updateImageCount();

    imageFiles.forEach((file, idx) => {
      const reader = new FileReader();
      reader.onload = e => {
        const $thumb = $(`
          <div class="spr-image-thumb" data-index="${idx}">
            ${idx === 0 ? `<span class="spr-image-thumb__badge">대표</span>` : ``}
            <img src="${e.target.result}">
            <button type="button" class="spr-image-thumb__remove">×</button>
          </div>
        `);
        $imagePreview.append($thumb);
      };
      reader.readAsDataURL(file);
    });
  }

  // 이미지 선택
  $imageInput.on('change', function () {
    const files = Array.from(this.files);
    const totalAfter = imageFiles.length + files.length;

    if (totalAfter > 10) {
      alert('이미지는 최대 10장까지 업로드할 수 있습니다.');
      this.value = '';
      return;
    }

    files.forEach(f => {
      if (f.type && f.type.startsWith('image/')) {
        imageFiles.push(f);
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

  // 대표 이미지 변경 (클릭 시 앞으로)
  $imagePreview.on('click', '.spr-image-thumb img', function () {
    const idx = Number($(this).closest('.spr-image-thumb').data('index'));
    if (idx === 0) return;

    const img = imageFiles.splice(idx, 1)[0];
    imageFiles.unshift(img);
    renderImages();
  });

  updateImageCount();

  /* ==================================================
     AutoNumeric Price
  ================================================== */
  const priceInput = document.querySelector('input[name="price"]');
  priceAutoNumeric = new AutoNumeric(priceInput, {
    digitGroupSeparator: ',',
    decimalPlaces: 0,
    minimumValue: '0',
    maximumValue: '1000000000',
  });

  /* ==================================================
     GPT 가격 추천
  ================================================== */
  $('.spr-ai-btn').on('click', function () {
    const title = $('#productTitle').val().trim();
    const description = $('#detail').val().trim();
    const conditionStatus = $('input[name="conditionStatus"]:checked').val();
    const categoryName = $('.spr-subchip.is-active').text();

    if (!title || !description) {
      alert('상품명과 상세 설명을 입력해 주세요.');
      return;
    }

    $.ajax({
      url: '/api/gpt/price-recommend',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({ title, description, conditionStatus, categoryName }),

      beforeSend() {
        $('.spr-ai-btn').prop('disabled', true).text('AI 분석 중...');
      },

      success(res) {
        $('.spr-ai-info__price strong')
          .text(`${res.minPrice.toLocaleString()} ~ ${res.maxPrice.toLocaleString()} 원`);

        $('.spr-ai-info__reason').text(res.reason);

        $('.spr-ai-info__apply')
          .data('min', res.minPrice)
          .data('max', res.maxPrice);

        $('.spr-ai-info').fadeIn(150);
      },

      error() {
        alert('AI 추천 가격을 불러오지 못했습니다.');
      },

      complete() {
        $('.spr-ai-btn').prop('disabled', false).text('GPT 가격 추천 받기');
      }
    });
  });

  // 평균가 적용
  $('.spr-ai-info__apply').on('click', function () {
    const min = Number($(this).data('min'));
    const max = Number($(this).data('max'));
    if (!min || !max) return;

    const avg = (min + max) / 2;
    const normalized = Math.round(avg / 1000) * 1000;
    priceAutoNumeric.set(normalized);
  });

  /* ==================================================
     Form Validation + Submit
  ================================================== */
  $('.seller-product-register__form').on('submit', function (e) {

    if (!$('#productTitle').val().trim()) {
      alert('상품 제목을 입력해주세요.');
      e.preventDefault(); return;
    }

    if (!$inputCategoryId.val()) {
      alert('카테고리를 선택해주세요.');
      e.preventDefault(); return;
    }

    if (!$('input[name="conditionStatus"]:checked').length) {
      alert('상품 상태를 선택해주세요.');
      e.preventDefault(); return;
    }

    const rawPrice = priceAutoNumeric.getNumber();
    if (!rawPrice || rawPrice <= 0) {
      alert('가격을 올바르게 입력해주세요.');
      e.preventDefault(); return;
    }

    if (!$('#detail').val().trim()) {
      alert('상품 설명을 입력해주세요.');
      e.preventDefault(); return;
    }

    const tradeType = $('input[name="tradeType"]:checked').val();
    if (!tradeType) {
      alert('거래 방식을 선택해주세요.');
      e.preventDefault(); return;
    }

    if ((tradeType === 'DIRECT' || tradeType === 'ALL') && !$('#regionSidoCode').val()) {
      alert('거래 희망 지역을 선택해주세요.');
      e.preventDefault(); return;
    }

    if (imageFiles.length === 0) {
      alert('상품 이미지를 최소 1장 이상 등록해주세요.');
      e.preventDefault(); return;
    }

    if (imageFiles.length > 10) {
      alert('이미지는 최대 10장까지 업로드할 수 있습니다.');
      e.preventDefault(); return;
    }

    const $form = $(this);

    /* =========================
       이미지 file input 재구성
       - name="images"
    ========================= */
    $form.find('input[name="images"]').remove();

    imageFiles.forEach(file => {
      const dt = new DataTransfer();
      dt.items.add(file);

      $('<input>')
        .attr({ type: 'file', name: 'images' })
        .prop('files', dt.files)
        .css('display', 'none')
        .appendTo($form);
    });

    // AutoNumeric 언포맷
    priceAutoNumeric.unformat();
  });

});
