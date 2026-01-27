function openFindIdModal() {
  document.getElementById("findIdModal").classList.remove("is-hidden");
  document.body.style.overflow = "hidden";
}

function closeFindIdModal() {
  document.getElementById("findIdModal").classList.add("is-hidden");
  document.body.style.overflow = "";
}

/* 로그인 모달로 복귀 */
function openLoginModalFromFindId() {
  closeFindIdModal();
  openLoginModal();
}

/* 본인인증 */
async function startFindIdCertification() {
  try {
    const identityVerificationId =
      `id-verify-${Date.now()}`;

    const response =
      await PortOne.requestIdentityVerification({
        storeId: "store-40e995f6-80d5-43c5-812a-babeaca09755",
        channelKey: "channel-key-d4d68a57-122c-4418-81fb-d1ed47cec331",
        identityVerificationId,
        identityVerificationMethod: "SMS"
      });

    if (response.code != null) return;

    const res = await fetch("/login/find_id_process", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        imp_uid: response.identityVerificationId
      })
    });

    const data = await res.json();

    document.getElementById("findIdAuthArea").style.display = "none";

    if (data.success) {
      document.getElementById("foundEmail").textContent = data.email;
      document.getElementById("findIdResultArea")
        .classList.remove("is-hidden");
    } else {
      document.getElementById("findIdErrorArea")
        .classList.remove("is-hidden");
    }

  } catch (e) {
    alert("본인인증 중 오류가 발생했습니다.");
  }
}
