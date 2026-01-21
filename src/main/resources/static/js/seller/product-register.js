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