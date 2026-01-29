// detail-report.js

document.addEventListener("click", (e) => {

  /* =========================
     신고 모달 열기
  ========================= */
  const openBtn = e.target.closest("[data-action='open-report-modal']");
  if (openBtn) {

    const isLogin = openBtn.dataset.login === "true";
    if (!isLogin) {
      alert("로그인이 필요합니다.");
      location.href = "/login";
      return;
    }

    const modal = document.getElementById("reportModal");
    modal?.classList.remove("is-hidden");

    modal.dataset.productId = openBtn.dataset.productId;
    modal.dataset.triggerBtnId = "report-btn";

    return;
  }

  /* =========================
     모달 닫기
  ========================= */
  const closeBtn = e.target.closest("[data-action='close-report-modal']");
  if (closeBtn) {
    document.getElementById("reportModal")
      ?.classList.add("is-hidden");
    return;
  }

  if (e.target.classList.contains("modal-backdrop")) {
    document.getElementById("reportModal")
      ?.classList.add("is-hidden");
    return;
  }

  /* =========================
     신고 접수
  ========================= */
  const submitBtn = e.target.closest("[data-action='submit-report']");
  if (submitBtn) {

    const modal = document.getElementById("reportModal");

	const reasonEl =
	  modal.querySelector("input[name='report-reason']:checked");

	const detailEl =
	  modal.querySelector(".report-textarea");

	const detailValue = detailEl?.value.trim();

	if (!reasonEl) {
	  alert("신고 사유를 선택해주세요.");
	  return;
	}

	if (!detailValue) {
	  alert("신고 내용을 입력해주세요.");
	  detailEl.focus();
	  return;
	}

    const productId = modal.dataset.productId;

    fetch(`/product/${productId}/report`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ reasonCode: reasonEl.value })
    })
      .then(res => res.json())
      .then(result => {

        alert(result.message || "신고가 접수되었습니다.");

        modal.classList.add("is-hidden");

        const openBtn =
          document.querySelector("[data-action='open-report-modal']");
        if (openBtn) {
          openBtn.textContent = "신고 완료";
          openBtn.disabled = true;
          openBtn.classList.add("is-disabled");
        }
      })
      .catch(() => {
        alert("신고 처리 중 오류가 발생했습니다.");
      });

    return;
  }
});

/* =========================
   신고 사유 선택 시 버튼 활성화
========================= */
document.addEventListener("change", (e) => {
  if (e.target.name === "report-reason") {

    const modal = document.getElementById("reportModal");
    if (!modal) return;

    const submitBtn =
      modal.querySelector("[data-action='submit-report']");
    if (!submitBtn) return;

    submitBtn.disabled = false;
    submitBtn.classList.remove("is-disabled");
  }
});

