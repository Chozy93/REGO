// /js/product/detail-report.js
document.addEventListener("click", (e) => {

  /* 신고 모달 열기 */
  const openBtn = e.target.closest("[data-action='open-report-modal']");
  if (openBtn) {

    const isLogin = openBtn.dataset.login === "true";
    if (!isLogin) {
      alert("로그인이 필요합니다.");
      location.href = "/login";
      return;
    }

    document.getElementById("reportModal")
      ?.classList.remove("is-hidden");
    return;
  }

  /* 모달 닫기 (X, 취소) */
  const closeBtn = e.target.closest("[data-action='close-report-modal']");
  if (closeBtn) {
    document.getElementById("reportModal")
      ?.classList.add("is-hidden");
    return;
  }

  /* backdrop 클릭 닫기 */
  if (e.target.classList.contains("modal-backdrop")) {
    document.getElementById("reportModal")
      ?.classList.add("is-hidden");
    return;
  }

  /* =========================
     신고 접수 (서버 연동)
  ========================= */
  const submitBtn = e.target.closest("[data-action='submit-report']");
  if (submitBtn) {

    const modal   = document.getElementById("reportModal");
    const openBtn = document.querySelector("[data-action='open-report-modal']");

    const reasonEl = modal.querySelector("input[name='report-reason']:checked");
    if (!reasonEl) {
      alert("신고 사유를 선택해주세요.");
      return;
    }

    const productId = openBtn.dataset.productId;

    fetch(`/product/${productId}/report`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        reasonCode: reasonEl.value
      })
    })
      .then(res => res.json())
      .then(result => {

        if (!result.success) {
          alert(result.message);

          // 실패여도 모달 닫기
          modal.classList.add("is-hidden");

          openBtn.textContent = "신고 완료";
          openBtn.disabled = true;
          openBtn.classList.add("is-disabled");
          return;
        }

        // 정상 신고
        alert("신고가 접수되었습니다.");

        modal.classList.add("is-hidden");

        openBtn.textContent = "신고 완료";
        openBtn.disabled = true;
        openBtn.classList.add("is-disabled");
      })
      .catch(err => {
        console.error(err);
        alert("신고 처리 중 오류가 발생했습니다.");
      });

    return;
  }

});
