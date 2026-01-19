/**
 * ==================================================
 * Chat List Page JS
 *
 * 역할 요약
 * --------------------------------------------------
 * 1. 채팅방 리스트 클릭 처리
 * 2. 채팅방 fragment AJAX 로드
 * 3. 채팅방 전역 컨텍스트(chatContext) 관리
 * 4. WebSocket 연결/해제 라이프사이클 관리
 *
 * 핵심 설계 원칙
 * --------------------------------------------------
 * - DOM은 fragment load 콜백에서만 읽는다
 * - WebSocket 메시지 처리 로직은 DOM에 의존하지 않는다
 * - 채팅방 단위 고정 정보는 전역 상태로 관리한다
 * ==================================================
 */

/* ==================================================
   Chat Global Context (전역 상태)
   --------------------------------------------------
   - 현재 열려 있는 채팅방의 상태 정보
   - appendMessageToChatUI 등에서 DOM 대신 참조
================================================== */

$(function () {

  /* ==================================================
     Dropdown (채팅방 리스트 옵션 드롭다운)
     --------------------------------------------------
     - 이벤트 위임 방식 사용
     - 여러 드롭다운 중 하나만 열리도록 제어
  ================================================== */
  $(document).on("click", ".chat-dropdown-btn", function (e) {
    e.stopPropagation(); // 바깥 클릭 이벤트 전파 방지

    const $dropdown = $(this).closest(".chat-dropdown");

    // 다른 드롭다운은 모두 닫기
    $(".chat-dropdown").not($dropdown).removeClass("is-open");

    // 현재 드롭다운 토글
    $dropdown.toggleClass("is-open");
  });

  // 문서 바깥 클릭 시 모든 드롭다운 닫기
  $(document).on("click", function () {
    $(".chat-dropdown").removeClass("is-open");
  });

  /* ==================================================
     Chat Room Enter (AJAX Fragment 로딩)
     --------------------------------------------------
     - 채팅방 클릭 시 fragment 로드
     - DOM 생성이 보장되는 유일한 시점
     - 이 시점에서만 DOM을 읽어 전역 상태 세팅
  ================================================== */
  $(document).on("click", ".chat-room-item", function (e) {
    e.preventDefault();

    // 클릭한 채팅방의 roomId 추출
    const roomId = $(this).data("room-id");
    if (!roomId) return;

    // 빈 상태 UI 숨기고 채팅방 영역 표시
    $("#chatEmptyState").hide();
    $("#chatRoomArea").show();

    // 이전 채팅방 정리 (WebSocket 해제 + 상태 초기화)
    leaveChatRoom();

    // 채팅방 fragment 로드
    $("#chatRoomArea").load("/chat/room/" + roomId, function () {

      /* ============================================
         전역 컨텍스트 세팅 (DOM 존재 보장 구간)
         --------------------------------------------
         - fragment가 DOM에 삽입된 직후
         - chat-room-avatar 접근 가능한 유일한 위치
      ============================================ */



      // 상대방 아바타 src 저장 (append에서는 DOM 접근 금지)
    
      // (선택) 현재 방 ID 별도 전역 보관
      window.currentRoomId = roomId;
	
      /* ============================================
         WebSocket 연결
         --------------------------------------------
         - 반드시 전역 상태 세팅 이후에 실행
         - 메시지 수신 시 appendMessageToChatUI 호출
      ============================================ */
      connectChatSocket(roomId);
    });
  });

}); // $(function () {}) 종료


/* ==================================================
   Chat Room Lifecycle (WebSocket 관리)
================================================== */

/**
 * WebSocket 연결 함수
 * --------------------------------------------------
 * @param roomId 현재 채팅방 ID
 */
function connectChatSocket(roomId) {

  // 기존 WebSocket 연결이 있으면 해제
  if (window.stompClient && stompClient.connected) {
    stompClient.disconnect();
    console.log("[CHAT] 이전 WebSocket disconnect");
  }

  // SockJS + STOMP 초기화
  const socket = new SockJS("/ws/chat");
  window.stompClient = Stomp.over(socket);

  // WebSocket 연결
  stompClient.connect({}, function () {
    console.log("[CHAT] WebSocket connected:", roomId);

    // UI 연결 상태 표시 (존재할 경우)
    if (typeof setChatConnectedUI === "function") {
      setChatConnectedUI(true);
    }

    // 채팅방 구독 (메시지 수신 시작)
    subscribeChatRoom(roomId);
  });
}

/**
 * 채팅방 나가기 처리
 * --------------------------------------------------
 * - WebSocket 연결 해제
 * - 전역 채팅 컨텍스트 초기화
 * - 방 전환 시 반드시 호출
 */
function leaveChatRoom() {

  // WebSocket 연결 해제
  if (window.stompClient && stompClient.connected) {
    stompClient.disconnect();
    console.log("[CHAT] WebSocket disconnect (leave room)");
  }

  // 전역 상태 초기화

  window.currentRoomId = null;
}
