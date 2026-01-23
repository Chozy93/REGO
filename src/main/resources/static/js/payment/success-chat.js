/**
 * 판매자에게 채팅하기 버튼 클릭 시 호출
 */
function goToChat(productId) {
    if (!productId) {
        console.error("상품 ID가 없습니다.");
        return;
    }

    // 1. 서버에 채팅방 생성 요청 (비동기)
    // 컨트롤러에서 redirect 하더라도 fetch는 최종 응답 URL을 response.url로 가져옵니다.
    fetch('/chat/start/' + productId, {
        method: 'GET',
        headers: { 'X-Requested-With': 'XMLHttpRequest' }
    })
    .then(response => {
        // 응답받은 URL (/chat/room/123)에서 roomId 추출
        const parts = response.url.split('/');
        const roomId = parts[parts.length - 1];
        
        // 2. 채팅 리스트 페이지로 이동하며 파라미터 전달
        window.location.href = '/chat/list?openRoom=' + roomId;
    })
    .catch(err => {
        console.error("채팅방 생성 중 오류 발생:", err);
        alert("채팅방을 열 수 없습니다. 다시 시도해주세요.");
    });
}