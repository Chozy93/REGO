/**
 * Chat Room UI (jQuery)
 * - fragment safe
 * - delegated events
 * - WebSocket send / receive helpers
 */


/* ==================================================
   Textarea Auto Height
================================================== */
function resizeChatTextarea($textarea) {
  $textarea.css("height", "auto");
  $textarea.css("height", $textarea.prop("scrollHeight") + "px");
}

/* ==================================================
   Delegated Events (fragment 대응)
================================================== */

// textarea 입력
$(document).on("input", ".chat-room-textarea", function () {
  resizeChatTextarea($(this));
});

// Enter / Shift+Enter
$(document).on("keydown", ".chat-room-textarea", function (e) {
  if (e.key === "Enter" && !e.shiftKey) {
    e.preventDefault();

    const content = $(this).val().trim();
    if (!content) return;

    sendChatMessage(content);

    $(this).val("");
    resizeChatTextarea($(this));
  }
});

// 전송 버튼
$(document).on("click", ".chat-room-send-btn", function () {
  const $textarea = $(".chat-room-textarea");
  const content = $textarea.val().trim();
  if (!content) return;

  sendChatMessage(content);

  $textarea.val("");
  resizeChatTextarea($textarea);
});

/* ==================================================
   WebSocket Message Send
================================================== */
function sendChatMessage(content) {

  console.log("[CHAT] sendChatMessage:", content);

  if (!window.stompClient || !stompClient.connected) {
    console.warn("[CHAT] WebSocket not connected");
    return;
  }

  stompClient.send(
    `/app/chat/${window.currentRoomId}/send`,
    {},
    JSON.stringify({ content })
  );
}

/* ==================================================
   WebSocket Receive Helper
   - chat-list.js에서 connect 후 호출
================================================== */
function subscribeChatRoom(roomId) {

  window.currentRoomId = roomId;

  const $chatScroll = $(".chat-room-messages");

  // 🔥 topic 구독
  window.chatSubscription = stompClient.subscribe(
    `/topic/chat.${roomId}`,
    function (message) {
      const data = JSON.parse(message.body);

      const shouldScroll =
        $chatScroll.scrollTop() + $chatScroll.innerHeight() + 40
        >= $chatScroll.prop("scrollHeight");

      appendMessageToChatUI(data);

      if (shouldScroll) {
        $chatScroll.scrollTop($chatScroll.prop("scrollHeight"));
      }
    }
  );

  // 🔥 서버에 "이 방 보고 있음" 알림
  stompClient.send(
    `/app/chat/${roomId}/view`,
    {},
    JSON.stringify({})
  );
}


function leaveChatRoom() {

  if (!window.currentRoomId) return;

  // 🔥 서버에 "이 방 안 봄" 알림
  if (window.stompClient && stompClient.connected) {
    stompClient.send(
      `/app/chat/${window.currentRoomId}/leave`,
      {},
      JSON.stringify({})
    );
  }

  // 🔥 topic 구독 해제
  if (window.chatSubscription) {
    window.chatSubscription.unsubscribe();
    window.chatSubscription = null;
  }

  window.currentRoomId = null;
}


/* ==================================================
   UI State
================================================== */
function setChatConnectedUI(isConnected) {
  const $room = $(".chat-room");
  $room.toggleClass("is-disconnected", !isConnected);
}

/* ==================================================
   Chat Message Renderer
   - Thymeleaf 렌더 구조와 100% 동일
================================================== */
window.appendMessageToChatUI = function (message) {
	  const loginUserId = $("#loginUserId").text().trim();
	console.log("loginUserId", loginUserId);

	
	// 🔥 isMine 판단 (프론트 기준)
	const isMine =
	 loginUserId!== null &&
	  Number(message.senderUserId) === Number(loginUserId);
	   console.log("센더아이디"+message.senderUserId);
	   console.log("로그인유저 아이디"+loginUserId);
	console.log("isMine 체크"+isMine);
  const $message = $("<div>").addClass("chat-room-message");

  // 내 메시지
  if (isMine) {
    $message.addClass("chat-room-message-me");
  }
  const avatar = window.opponentAvatar;
 console.log("avatar"+avatar);


  // 말풍선
  const $bubble = $("<div>")
    .addClass("chat-room-bubble")
    .addClass(isMine ? "chat-room-bubble-me" : "chat-room-bubble-other");

  $("<p>")
    .text(message.content)
    .appendTo($bubble);

  $("<span>")
    .addClass("chat-room-time")
    .text(message.createdAt)
    .appendTo($bubble);

  $bubble.appendTo($message);

  // 메시지 영역에 추가
  $(".chat-room-messages").append($message);
};

$(window).on("beforeunload", function () {
  leaveChatRoom();
});
