document.addEventListener('DOMContentLoaded', function () {

  const agreeAll = document.getElementById('agreeAll');
  const nextBtn = document.getElementById('nextBtn');

  const itemChecks = document.querySelectorAll('.terms-list .terms-checkbox');
  const requiredChecks = document.querySelectorAll('.terms-list .terms-checkbox.required');

  /* =========================
     전체 동의
  ========================= */
  agreeAll.addEventListener('change', function () {
    itemChecks.forEach(cb => cb.checked = agreeAll.checked);
  });

  /* =========================
     개별 체크 → 전체 동의 상태 동기화
  ========================= */
  itemChecks.forEach(cb => {
    cb.addEventListener('change', function () {
      const allChecked = Array.from(itemChecks).every(x => x.checked);
      agreeAll.checked = allChecked;
    });
  });

  /* =========================
     다음 버튼 검증
  ========================= */
  nextBtn.addEventListener('click', function () {
    const allRequiredChecked = Array.from(requiredChecks).every(x => x.checked);

    if (!allRequiredChecked) {
      alert('필수 약관에 모두 동의해 주셔야 다음 단계로 진행이 가능해요!');
      return;
    }

    location.href = '/signup/step2';
  });

});
