function openFindPasswordModal() {
  document.getElementById("findPasswordModal")
    .classList.remove("is-hidden");
  document.body.style.overflow = "hidden";
}

function closeFindPasswordModal() {
  document.getElementById("findPasswordModal")
    .classList.add("is-hidden");
  document.body.style.overflow = "";
}

/* STEP 1: 이메일 + 본인인증 */
async function checkEmailAndVerify() {
  const email = document.getElementById("targetEmail").value;

  if (!email) {
    alert("이메일을 입력해주세요.");
    return;
  }

  try {
    const identityVerificationId =
      `pw-verify-${Date.now()}`;

    const response =
      await PortOne.requestIdentityVerification({
        storeId: "store-40e995f6-80d5-43c5-812a-babeaca09755",
        channelKey: "channel-key-d4d68a57-122c-4418-81fb-d1ed47cec331",
        identityVerificationId,
        identityVerificationMethod: "SMS"
      });

    if (!response.identityVerificationId) return;

    const res = await fetch("/login/verify_user_for_pw", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        imp_uid: response.identityVerificationId,
        email
      })
    });

    const data = await res.json();

    if (data.success) {
      document.getElementById("pwAuthArea").style.display = "none";
      document.getElementById("pwResetArea")
        .classList.remove("is-hidden");
    } else {
      alert(data.message || "정보가 일치하지 않습니다.");
    }

  } catch (e) {
    alert("본인인증 중 오류가 발생했습니다.");
  }
}

/* STEP 2: 비밀번호 변경 */
async function updatePassword() {
  const email = document.getElementById("targetEmail").value;
  const newPw = document.getElementById("newPassword").value;
  const confirmPw =
    document.getElementById("confirmPassword").value;

  if (!newPw || newPw.length < 4) {
    alert("비밀번호는 4자리 이상 입력해주세요.");
    return;
  }

  if (newPw !== confirmPw) {
    alert("비밀번호가 일치하지 않습니다.");
    return;
  }

  const res = await fetch("/login/update_password", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      email,
      newPassword: newPw
    })
  });

  const data = await res.json();

  if (data.success) {
    alert("비밀번호가 변경되었습니다. 다시 로그인해주세요.");
    closeFindPasswordModal();
    openLoginModal();
  } else {
    alert(data.message);
  }
}
