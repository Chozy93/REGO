document.addEventListener("DOMContentLoaded", () => {

  const reportModal = document.getElementById("reportModal");
  const reportForm = document.getElementById("reportForm");
  const cancelBtn = document.getElementById("reportCancelBtn");

  if (!reportModal || !reportForm) return;

  /* =========================
     ❌ 닫기 / 취소
  ========================= */
  cancelBtn?.addEventListener("click", () => {
    reportModal.classList.add("is-hidden");
  });

  reportModal.addEventListener("click", (e) => {
    if (e.target.classList.contains("modal-backdrop")) {
      reportModal.classList.add("is-hidden");
    }
  });

  /* =========================
     🚨 신고 submit
  ========================= */
  reportForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    if (reportForm.dataset.login !== "true") {
      alert("로그인이 필요합니다.");
      return;
    }

    const productId = reportForm.dataset.productId;
    const checked = reportForm.querySelector("input[name='reason']:checked");

    if (!checked) {
      alert("신고 사유를 선택해주세요.");
      return;
    }

    try {
      const res = await fetch(`/product/${productId}/report`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          reasonCode: checked.value
        })
      });

      const result = await res.json();

      // ❌ 실패 (이미 신고한 경우 등)
      if (!result.success) {
        alert(result.message);

        reportModal.classList.add("is-hidden");

        const reportBtn = document.getElementById("reportBtn");
        if (reportBtn) {
          reportBtn.textContent = "신고완료";
          reportBtn.classList.add("is-disabled");
          reportBtn.disabled = true;
        }
        return;
      }

      // ✅ 성공
      alert("신고가 접수되었습니다.");

      reportForm.reset();
      reportModal.classList.add("is-hidden");

      const reportBtn = document.getElementById("reportBtn");
      if (reportBtn) {
        reportBtn.textContent = "신고완료";
        reportBtn.classList.add("is-disabled");
        reportBtn.disabled = true;
      }

    } catch (err) {
      console.error(err);
      alert("신고 처리 중 오류가 발생했습니다.");
    }
  });

  });