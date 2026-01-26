$(function () {

  const $phoneInput = $("#phoneNumber");
  const $submitBtn = $("#submitBtn");
  const $pwInput = $("#newPassword");
  const $confirmInput = $("#confirmPassword");
  const $pwError = $("#pwError");

  let latestVerificationId = null;

  /* =========================
     전화번호: 숫자만 입력
  ========================= */
  $phoneInput.on("input", function () {
    this.value = this.value.replace(/[^0-9]/g, "");
  });

  // 비밀번호 실시간 일치/불일치 메시지 전환
  function checkPasswordMatch() {
    const pw = $("#newPassword").val();
    const confirm = $("#confirmPassword").val();
    const $msg = $("#pwError");

    // 둘 중 하나라도 비어있으면 숨김
    if (!pw || !confirm) {
      $msg.addClass("is-hidden");
      return;
    }

    if (pw === confirm) {
      $msg
        .removeClass("is-hidden error")
        .addClass("success")
        .text("비밀번호가 일치합니다.");
    } else {
      $msg
        .removeClass("is-hidden success")
        .addClass("error")
        .text("비밀번호가 일치하지 않습니다.");
    }
  }

  // 이벤트 바인딩
  $("#newPassword, #confirmPassword").on("input", checkPasswordMatch);

  /* =========================
     본인인증 (PortOne)
  ========================= */
  $("#requestCertBtn").on("click", async function () {
    const phoneNumber = $phoneInput.val();

    if (!phoneNumber) {
      alert("인증받으실 휴대폰 번호를 입력해 주세요.");
      $phoneInput.focus();
      return;
    }

    try {
      latestVerificationId = `id-verify-${Date.now()}`;

      const response = await PortOne.requestIdentityVerification({
        storeId: "store-40e995f6-80d5-43c5-812a-babeaca09755",
        channelKey: "channel-key-d4d68a57-122c-4418-81fb-d1ed47cec331",
        identityVerificationId: latestVerificationId,
        identityVerificationMethod: "SMS"
      });

      if (response.code !== undefined) {
        alert("인증 실패: " + response.message);
        latestVerificationId = null;
        return;
      }

      alert("본인인증이 완료되었습니다.");
      $submitBtn.prop("disabled", false);

    } catch (err) {
      console.error(err);
      alert("본인인증 중 오류가 발생했습니다.");
      latestVerificationId = null;
    }
  });

  /* =========================
     최종 제출
  ========================= */
  $submitBtn.on("click", function () {
    const phoneNumber = $phoneInput.val();
    const password = $pwInput.val();
    const confirm = $confirmInput.val();

    if (!password) {
      alert("비밀번호를 입력해 주세요.");
      $pwInput.focus();
      return;
    }

    if (password !== confirm) {
      $pwError.removeClass("is-hidden");
      $confirmInput.focus();
      return;
    }

    if (!latestVerificationId) {
      alert("휴대폰 본인인증을 먼저 진행해 주세요.");
      return;
    }

    $.ajax({
      url: "/auth/update-phone",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify({
        phoneNumber: phoneNumber,
        password: password,
        verificationId: latestVerificationId
      }),
      success: function (res) {
        if (res === "success") {
          location.href = "/";
        } else {
          alert("정보 업데이트에 실패했습니다.");
        }
      },
      error: function () {
        alert("서버 통신 중 오류가 발생했습니다.");
      }
    });
  });

});
