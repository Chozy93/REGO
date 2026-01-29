function toggleStatus(id, status) {
    if(!confirm(status ? "노출 상태로 변경하시겠습니까?" : "비노출 상태로 변경하시겠습니까?")) return;

    fetch(`/admin/notice/toggle-status?id=${id}&status=${status}`, {
        method: 'POST',
        headers: {
            // 스프링 시큐리티를 사용 중이라면 CSRF 토큰이 필요할 수 있습니다.
            'header': document.querySelector('meta[name="_csrf_header"]')?.content,
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]')?.content
        }
    })
    .then(response => {
        if(response.ok) {
            location.reload(); // 성공 시 새로고침하여 바뀐 뱃지 확인
        } else {
            alert("상태 변경에 실패했습니다.");
        }
    });
}