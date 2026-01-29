/* ==========================================================================
   Seller Product Edit JS (EDIT ONLY)
   - Category 초기 선택 반영
   - Existing Image Delete (deleteImageIds)
   - New Image Upload / Preview
   - Total Image Count (existing + new)
   - Representative Image Order (new only)
   - AutoNumeric Price
   - GPT Price Recommend
   - Form Validation
========================================================================== */

let priceAutoNumeric;

$(function () {

  /* ==================================================
     Category (초기 선택 + 변경)
  ================================================== */
  const $mainTabs = $(".spr-category__main .spr-chip");
  const $subBoxes = $(".spr-category__sub-box");
  const $inputCategoryId = $("input[name='categoryId']");

  // 1차
  $mainTabs.on("click", function () {
    const parentId = $(this).data("parentId");

    $mainTabs.removeClass("spr-chip--active");
    $(this).addClass("spr-chip--active");

    $subBoxes.removeClass("is-active");
    $subBoxes.filter(`[data-parent-id="${parentId}"]`).addClass("is-active");

    $inputCategoryId.val("");
    $(".spr-subchip").removeClass("is-active");
  });

  // 2차
  $(".spr-category__sub").on("click", ".spr-subchip", function () {
    $(".spr-subchip").removeClass("is-active");
    $(this).addClass("is-active");
    $inputCategoryId.val($(this).data("categoryId"));
  });

  // 수정 페이지 초기 category 반영
  (function initCategorySelected() {
    const selectedCategoryId = $inputCategoryId.val();
    if (!selectedCategoryId) return;

    const $sub = $(`.spr-subchip[data-category-id="${selectedCategoryId}"]`);
    if (!$sub.length) return;

    $(".spr-subchip").removeClass("is-active");
    $sub.addClass("is-active");

    const $box = $sub.closest(".spr-category__sub-box");
    const parentId = $box.data("parentId");

    $mainTabs.removeClass("spr-chip--active");
    $mainTabs.filter(`[data-parent-id="${parentId}"]`).addClass("spr-chip--active");

    $subBoxes.removeClass("is-active");
    $box.addClass("is-active");
  })();


  /* ==================================================
     Image (EDIT)
  ================================================== */
  const $imageInput = $('#imageInput');
  const $imagePreview = $('#imagePreview');
  const $imageCount = $('#imageCount');
  const $existingImages = $('#existingImages');

  let newImageFiles = [];
  let deleteImageIds = [];

  function getExistingCount() {
    if (!$existingImages.length) return 0;
    return $existingImages.find('[data-image-id]').length;
  }

  function updateImageCount() {
    $imageCount.text(getExistingCount() + newImageFiles.length);
  }

  function renderNewImages() {
    $imagePreview.empty();

    newImageFiles.forEach((file, idx) => {
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

    updateImageCount();
  }

  // 기존 이미지 삭제
  $existingImages.on('click', '.btn-delete-image', function () {
    const imageId = $(this).data('imageId');
    if (!imageId) return;

    deleteImageIds.push(Number(imageId));
  $(this).closest('.spr-image-thumb').remove();
    updateImageCount();
  });

  // 신규 이미지 선택
  $imageInput.on('change', function () {
    const files = Array.from(this.files);
    const totalAfter = getExistingCount() + newImageFiles.length + files.length;

    if (totalAfter > 10) {
      alert('이미지는 최대 10장까지 업로드할 수 있습니다.');
      this.value = '';
      return;
    }

    files.forEach(f => {
      if (f.type && f.type.startsWith('image/')) {
        newImageFiles.push(f);
      }
    });

    this.value = '';
    renderNewImages();
  });

  // 신규 이미지 삭제
  $imagePreview.on('click', '.spr-image-thumb__remove', function () {
    const idx = Number($(this).closest('.spr-image-thumb').data('index'));
    newImageFiles.splice(idx, 1);
    renderNewImages();
  });

  // 신규 이미지 대표 순서 변경
  $imagePreview.on('click', '.spr-image-thumb img', function () {
    const idx = Number($(this).closest('.spr-image-thumb').data('index'));
    if (idx === 0) return;

    const img = newImageFiles.splice(idx, 1)[0];
    newImageFiles.unshift(img);
    renderNewImages();
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
     const description = $('#description').val().trim();
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
     Submit
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

    if (!priceAutoNumeric.getNumber()) {
      alert('가격을 입력해주세요.');
      e.preventDefault(); return;
    }

    if (!$('#description').val().trim()) {
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

    if (getExistingCount() + newImageFiles.length === 0) {
      alert('상품 이미지를 최소 1장 이상 등록해주세요.');
      e.preventDefault(); return;
    }

    const $form = $(this);

    /* =========================
       deleteImageIds 전송
    ========================= */
    $form.find('input[name="deleteImageIds"]').remove();
    deleteImageIds.forEach(id => {
      $('<input>')
        .attr({ type: 'hidden', name: 'deleteImageIds', value: String(id) })
        .appendTo($form);
    });

    /* =========================
       신규 이미지 file input 구성
       - name="newImages"
    ========================= */
    $form.find('input[name="newImages"]').remove();

    newImageFiles.forEach(file => {
      const dt = new DataTransfer();
      dt.items.add(file);

      $('<input>')
        .attr({ type: 'file', name: 'newImages' })
        .prop('files', dt.files)
        .css('display', 'none')
        .appendTo($form);
    });

    priceAutoNumeric.unformat();
  });

});

