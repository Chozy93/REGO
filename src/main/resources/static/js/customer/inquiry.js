$(function () {

  const $inquiryTypeSelect = $("#inquiryTypeSelect");
  const $orderSelectField = $("#orderSelectField");
  const $orderIdInput = $("#orderId");
  const $selectedOrderPreview = $("#selectedOrderPreview");

  if ($inquiryTypeSelect.length === 0) return;

  $inquiryTypeSelect.on("change", function () {
    const selectedType = $(this).val();

    if (selectedType === "PAYMENT") {
      // 결제 문의 → 주문 선택 영역 노출
      $orderSelectField.removeClass("is-hidden");
    } else {
      // 그 외 → 주문 선택 영역 숨김 + 값 초기화
      $orderSelectField.addClass("is-hidden");

      if ($orderIdInput.length) {
        $orderIdInput.val("");
      }

      if ($selectedOrderPreview.length) {
        $selectedOrderPreview.addClass("is-hidden");
      }
    }
  });

});


function applySelectedOrder(orderId, title, metaText) {

  const $orderIdInput = $("#orderId");
  const $preview = $("#selectedOrderPreview");

  if ($orderIdInput.length === 0 || $preview.length === 0) return;

  $orderIdInput.val(orderId);

  $("#selectedOrderTitle").text(title);
  $("#selectedOrderMeta").text(metaText);

  $preview.removeClass("is-hidden");
}


$(function () {

  const $inquiryTypeSelect = $("#inquiryTypeSelect");
  const $orderSelectField = $("#orderSelectField");
  const $orderIdInput = $("#orderId");

  const $modal = $("#orderSelectModal");
  const $selectedOrderPreview = $("#selectedOrderPreview");
  const $selectedOrderTitle = $("#selectedOrderTitle");
  const $selectedOrderMeta = $("#selectedOrderMeta");

  /* =========================
     문의 유형 변경
  ========================= */
  $inquiryTypeSelect.on("change", function () {
    const type = $(this).val();

    if (type === "PAYMENT") {
      $orderSelectField.removeClass("is-hidden");
    } else {
      $orderSelectField.addClass("is-hidden");
      $orderIdInput.val("");
      $selectedOrderPreview.addClass("is-hidden");
    }
  });

  /* =========================
     모달 열기
  ========================= */
  window.openMyOrderSelectModal = function () {
    $modal.removeClass("is-hidden");
    $("body").css("overflow", "hidden");
  };

  /* =========================
     모달 닫기
  ========================= */
  window.closeOrderSelectModal = function () {
    $modal.addClass("is-hidden");
    $("body").css("overflow", "");
  };

  // backdrop 클릭 시 닫기
  $modal.find(".modal-backdrop").on("click", function () {
    closeOrderSelectModal();
  });

  /* =========================
     주문 선택
  ========================= */
  $modal.on("click", ".select-order-btn", function () {

    const $item = $(this).closest(".order-item");

    const orderId = $item.data("order-id");
    const title = $item.data("title");
    const meta = $item.data("meta");

    // hidden input
    $orderIdInput.val(orderId);

    // 미리보기 영역
    $selectedOrderTitle.text(title);
    $selectedOrderMeta.text(meta);
    $selectedOrderPreview.removeClass("is-hidden");

    closeOrderSelectModal();
  });

});
