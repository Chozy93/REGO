// 페이지 로드 시 실행
document.addEventListener('DOMContentLoaded', function() {
    const statusFilter = document.getElementById('statusFilter');
    
    if (statusFilter) {
        statusFilter.addEventListener('change', function() {
            const selectedStatus = this.value; // ALL, 답변대기, 답변완료 등
            const rows = document.querySelectorAll('.table-body .table-row');
            let visibleCount = 0;

            rows.forEach(row => {
                // '문의 내역이 없습니다' 메시지 행은 일단 숨김
                if (row.querySelector('p')) {
                    row.style.display = "none";
                    return;
                }

                // 배지 안의 텍스트(답변대기, 답변완료) 가져오기
                const statusText = row.querySelector('.badge').textContent.trim();

                if (selectedStatus === "ALL" || statusText === selectedStatus) {
                    row.style.display = "flex";
                    visibleCount++;
                } else {
                    row.style.display = "none";
                }
            });

            // 만약 필터링 결과가 하나도 없다면 '내역이 없습니다' 메시지 보여주기
            const emptyMsg = document.querySelector('.table-body .table-row p')?.parentElement;
            if (visibleCount === 0 && emptyMsg) {
                emptyMsg.style.display = "flex";
            }
        });
    }
});