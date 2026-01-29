/**
 * 신고 관리 페이지 전용 스크립트
 */

// 1. 신고 처리 완료 (상태 변경)
function completeReport(id) {
    if (!confirm("해당 신고를 '처리 완료' 상태로 변경하시겠습니까?")) return;

    // AJAX 요청을 통해 서버의 @PostMapping("/admin/report-done") 호출
    fetch(`/admin/report-done?id=${id}`, {
        method: 'POST',
        headers: getCsrfHeaders() // 보안을 위한 CSRF 토큰 (아래 함수 참고)
    })
    .then(response => {
        if (response.ok) {
            alert("처리가 완료되었습니다.");
            location.reload(); // 성공 시 리스트 새로고침
        } else {
            alert("처리 중 오류가 발생했습니다.");
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("네트워크 오류가 발생했습니다.");
    });
}

// 2. 검색 및 필터링 기능
function searchReports() {
    const status = document.getElementById('statusFilter').value;
    const keyword = document.getElementById('searchKeyword').value;
    
    // 선택된 상태와 키워드를 쿼리 스트링으로 전달
    location.href = `/admin/reports?status=${status}&keyword=${encodeURIComponent(keyword)}`;
}

// 공통: 시큐리티 CSRF 헤더 가져오기 (layout/base.html에 meta 태그가 있어야 함)
function getCsrfHeaders() {
    const header = document.querySelector('meta[name="_csrf_header"]')?.content;
    const token = document.querySelector('meta[name="_csrf"]')?.content;
    
    const headers = {};
    if (header && token) {
        headers[header] = token;
    }
    return headers;
}

// 전체 선택 기능 (체크박스)
document.getElementById('checkAll')?.addEventListener('change', function() {
    const isChecked = this.checked;
    document.querySelectorAll('input[name="reportCheck"]').forEach(cb => {
        cb.checked = isChecked;
    });
});