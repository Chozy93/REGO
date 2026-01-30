// detail-report.js
$(function () {

  console.log("[REPORT][INIT] detail-report.js loaded");

  /* =========================
     신고 모달 열기
  ========================= */
  $(document).on("click", "[data-action='open-report-modal']", function () {

    console.log("[REPORT][CLICK] open-report-modal");

    const $btn = $(this);
    console.log("[REPORT] login =", $btn.data("login"));
    console.log("[REPORT] productId =", $btn.data("productId"));

    if ($btn.data("login") !== true && $btn.data("login") !== "true") {
      alert("로그인이 필요합니다.");
      location.href = "/login";
      return;
    }

    const $modal = $("#reportModal");
    if ($modal.length === 0) {
      console.log("[REPORT][ERROR] reportModal not found");
      return;
    }

    // 신고 대상 정보 저장
    $modal.data("targetTypeCode", "PRODUCT");
    $modal.data("targetId", $btn.data("productId"));

    console.log("[REPORT] modal.targetTypeCode =", $modal.data("targetTypeCode"));
    console.log("[REPORT] modal.targetId =", $modal.data("targetId"));

    $modal.removeClass("is-hidden");
  });

  /* =========================
     모달 닫기
  ========================= */
  $(document).on(
    "click",
    "[data-action='close-report-modal'], .modal-backdrop",
    function () {
      console.log("[REPORT][CLICK] close modal");
      closeReportModal();
    }
  );

  /* =========================
     신고 접수 (AJAX)
  ========================= */
  $(document).on("click", "[data-action='submit-report']", function () {

    console.log("[REPORT][CLICK] submit-report");

    const $modal = $("#reportModal");
    if ($modal.length === 0) {
      console.log("[REPORT][ERROR] reportModal not found on submit");
      return;
    }

    const $reason = $modal.find("input[name='report-reason']:checked");
    const $detail = $modal.find(".report-textarea");
    const detailValue = $.trim($detail.val());

    console.log("[REPORT] reason =", $reason.val());
    console.log("[REPORT] detail =", detailValue);

    if ($reason.length === 0) {
      alert("신고 사유를 선택해주세요.");
      return;
    }

    if (!detailValue) {
      alert("신고 내용을 입력해주세요.");
      $detail.focus();
      return;
    }

    const payload = {
      targetTypeCode: $modal.data("targetTypeCode"),
      targetId: Number($modal.data("targetId")),
      reasonCode: $reason.val(),
      detail: detailValue
    };

    console.log("[REPORT][AJAX PAYLOAD]", payload);

    $.ajax({
      url: "/product/productReport",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify(payload),
      success: function (result) {
        console.log("[REPORT][AJAX SUCCESS]", result);

        if (!result.success) {
          alert(result.message || "신고 처리에 실패했습니다.");
          return;
        }

        alert("신고가 정상적으로 접수되었습니다.");

        closeReportModal();

        const $openBtn = $("[data-action='open-report-modal']");
        if ($openBtn.length) {
          $openBtn
            .text("신고 완료")
            .prop("disabled", true)
            .addClass("is-disabled");
        }
      },
      error: function (xhr, status, err) {
        console.log("[REPORT][AJAX ERROR]", status, err, xhr.responseText);
        alert("신고 처리 중 오류가 발생했습니다.");
      }
    });
  });

  /* =========================
     버튼 활성화 제어
  ========================= */
  $(document).on(
    "input change",
    "input[name='report-reason'], .report-textarea",
    function () {
      toggleSubmitState();
    }
  );

  /* =========================
     공통 함수
  ========================= */
  function toggleSubmitState() {
    const $modal = $("#reportModal");
    if ($modal.length === 0) return;

    const hasReason =
      $modal.find("input[name='report-reason']:checked").length > 0;
    const hasDetail =
      $.trim($modal.find(".report-textarea").val()).length > 0;

    const $submitBtn =
      $modal.find("[data-action='submit-report']");

    console.log("[REPORT] toggleSubmitState", { hasReason, hasDetail });

    if (hasReason && hasDetail) {
      $submitBtn.prop("disabled", false)
                .removeClass("is-disabled");
    } else {
      $submitBtn.prop("disabled", true)
                .addClass("is-disabled");
    }
  }

  function closeReportModal() {
    console.log("[REPORT] closeReportModal");

    const $modal = $("#reportModal");
    if ($modal.length === 0) return;

    $modal.addClass("is-hidden");

    // 초기화
    $modal.find("input[name='report-reason']")
          .prop("checked", false);

    $modal.find(".report-textarea").val("");

    $modal.find("[data-action='submit-report']")
          .prop("disabled", true)
          .addClass("is-disabled");
  }

});
