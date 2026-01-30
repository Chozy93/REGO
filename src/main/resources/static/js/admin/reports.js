function completeReport(id) {
    if(!confirm("해당 신고를 처리 완료하시겠습니까?")) return;

    // 컨트롤러의 @PatchMapping 주소와 형식을 맞춥니다.
    // URL 끝에 ?newStatus=DONE 을 붙여서 파라미터를 전달합니다.
    fetch(`/admin/reports/${id}/status?newStatus=DONE`, {
        method: 'PATCH', // POST가 아니라 PATCH입니다.
        headers: {
            'Content-Type': 'application/json',
            // Spring Security를 사용 중이라면 아래 주석을 해제하세요.
            // 'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]')?.content
        }
    })
    .then(res => {
        if(res.ok) {
            alert("처리 완료되었습니다.");
            location.reload(); 
        } else {
            // 실패 시 서버에서 보낸 에러 메시지를 확인하기 위해 텍스트로 읽어봅니다.
            res.text().then(text => alert("오류 발생: " + text));
        }
    })
    .catch(err => console.error("네트워크 에러:", err));
}


let currentReportId = null;

function openReportModal(reportId) {
  currentReportId = reportId;

  fetch(`/admin/reports/${reportId}`)
    .then(res => res.json())
    .then(data => {
      document.getElementById('modal-reason').innerText = data.reasonLabel;
      document.getElementById('modal-detail').innerText = data.detail;
      document.getElementById('modal-reporter').innerText = data.reporter;
      document.getElementById('modal-target').innerText = data.target;

      document.getElementById('reportModal').classList.remove('is-hidden');
    });
}

function closeReportModal() {
  document.getElementById('reportModal').classList.add('is-hidden');
}
