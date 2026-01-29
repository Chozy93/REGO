function openInquiryModal(element) {
    const id = element.getAttribute('data-id');
    const title = element.getAttribute('data-title');
    const content = element.getAttribute('data-content');
    const status = element.getAttribute('data-status');
    const answer = element.getAttribute('data-answer');

    // 1. 데이터 채우기
    document.getElementById('modalTitle').innerText = title;
    document.getElementById('modalInquiryContent').innerText = content;
    
    // 2. Form 및 관련 요소 가져오기
    const form = document.getElementById('modalForm');
    const answerInput = document.getElementById('answerContent'); // ✅ 여기서 변수 정의!
    const submitBtn = document.getElementById('submitBtn');
    
    form.action = `/admin/inquiries/${id}/answer`;

    // 3. 상태에 따른 처리
    if (status === 'DONE') {
        // 답변 완료 상태: 답변 내용을 보여주고 수정 불가 처리
        answerInput.value = answer;
        answerInput.readOnly = true; 
        submitBtn.style.display = 'none'; // 등록 버튼 숨김
    } else {
        // 대기 상태: 입력창 비우고 수정 가능 처리
        answerInput.value = '';
        answerInput.readOnly = false;
        submitBtn.style.display = 'block'; // 등록 버튼 표시
    }

    // 4. 모달 표시
    document.getElementById('inquiryModal').style.display = 'flex';
}

// 답변 등록 버튼 클릭 시 실행될 함수
function submitAnswer() {
    const form = document.getElementById('modalForm');
    const content = document.getElementById('answerContent').value;

    if (!content.trim()) {
        alert("답변 내용을 입력해주세요.");
        return;
    }

    if (confirm("답변을 등록하시겠습니까?")) {
        form.submit(); // 컨트롤러로 데이터 전송!
    }
}



function closeModal() {
    document.getElementById('inquiryModal').style.display = 'none';
    document.getElementById('answerContent').value = ''; // 폼 초기화
}