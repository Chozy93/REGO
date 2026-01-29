/* ==================================================
   Signup Step2 - FINAL
   - Email / Password / Nickname Validation
   - PortOne Identity Verification
   - Identity-based ReadOnly Fields
   - Full Submit Validation
================================================== */

/* =========================
   상태 플래그
========================= */
let isEmailVerified = false;
let isNicknameVerified = false;
let isAuthVerified = false;

/* =========================
   DOM 캐시
========================= */
const signupForm = document.getElementById('signupForm');

const emailInput = signupForm.email;
const emailMessage = document.getElementById('emailMessage');
const checkEmailBtn = document.getElementById('checkEmailBtn');

const passwordInput = signupForm.password;
const passwordConfirm = document.getElementById('passwordConfirm');
const pwMessage = document.getElementById('pwMessage');

const nicknameInput = document.getElementById('nickname');
const nicknameError = document.getElementById('nicknameError');

const nameInput = document.getElementById('username');
const phoneInput = document.getElementById('phoneNumber');
const birthInput = document.getElementById('birthDate');

const sendAuthBtn = document.getElementById('sendAuthBtn');

/* =========================
   공통 유틸
========================= */
function setMessage(el, msg, color) {
  el.textContent = msg;
  el.style.color = color;
}

function formatPhoneNumber(num) {
  if (!num) return '';
  const v = num.replace(/[^0-9]/g, '');
  if (v.length < 11) return v;
  return v.replace(/^(\d{3})(\d{3,4})(\d{4})$/, '$1-$2-$3');
}

/* =========================
   1. 이메일 중복 체크
========================= */
checkEmailBtn.addEventListener('click', async () => {
  const email = emailInput.value.trim();

  if (!email) {
    setMessage(emailMessage, '이메일을 입력해주세요.', 'red');
    return;
  }

  try {
    const res = await fetch(
      `/signup/check-email?email=${encodeURIComponent(email)}`
    );
    const taken = (await res.text()) === 'true';

    isEmailVerified = !taken;

    setMessage(
      emailMessage,
      taken ? '이미 사용 중인 이메일입니다.' : '사용 가능한 이메일입니다.',
      taken ? 'red' : 'green'
    );
  } catch (e) {
    console.error(e);
    isEmailVerified = false;
    setMessage(emailMessage, '이메일 확인 중 오류가 발생했습니다.', 'red');
  }
});

emailInput.addEventListener('input', () => {
  isEmailVerified = false;
  setMessage(
    emailMessage,
    '이메일 변경 시 다시 중복 확인이 필요합니다.',
    'orange'
  );
});

/* =========================
   2. 비밀번호 검증
========================= */
function checkPasswords() {
  const pw = passwordInput.value;
  const pw2 = passwordConfirm.value;

  if (!pw || !pw2) {
    pwMessage.textContent = '';
    return false;
  }

  if (pw.length < 8) {
    setMessage(pwMessage, '비밀번호는 최소 8자 이상이어야 합니다.', 'red');
    return false;
  }

  if (pw !== pw2) {
    setMessage(pwMessage, '비밀번호가 일치하지 않습니다.', 'red');
    return false;
  }

  setMessage(pwMessage, '비밀번호가 일치합니다.', 'green');
  return true;
}

passwordInput.addEventListener('input', checkPasswords);
passwordConfirm.addEventListener('input', checkPasswords);

/* =========================
   3. 닉네임 중복 체크
========================= */
nicknameInput.addEventListener('input', async () => {
  const nickname = nicknameInput.value.trim();

  if (nickname.length < 2) {
    nicknameError.textContent = '';
    isNicknameVerified = false;
    return;
  }

  try {
    const res = await fetch(
      `/signup/check-nickname?nickname=${encodeURIComponent(nickname)}`
    );
    const taken = (await res.text()) === 'true';

    isNicknameVerified = !taken;

    setMessage(
      nicknameError,
      taken ? '이미 사용 중인 닉네임입니다.' : '사용 가능한 닉네임입니다.',
      taken ? 'red' : 'green'
    );
  } catch (e) {
    console.error(e);
    isNicknameVerified = false;
    setMessage(nicknameError, '닉네임 확인 중 오류 발생', 'red');
  }
});

/* =========================
   4. PortOne 본인인증
========================= */
sendAuthBtn.addEventListener('click', async () => {
  try {
    const identityVerificationId = `signup-${Date.now()}`;

    const response = await PortOne.requestIdentityVerification({
      storeId: 'store-40e995f6-80d5-43c5-812a-babeaca09755',
      channelKey: 'channel-key-d4d68a57-122c-4418-81fb-d1ed47cec331',
      identityVerificationId,
      identityVerificationMethod: 'SMS'
    });

    if (response.code) {
      alert(response.message || '본인인증에 실패했습니다.');
      return;
    }

    const res = await fetch('/signup/verify-identity', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ identityVerificationId })
    });

    const data = await res.json();
    if (!data.success) {
      alert(data.message || '본인인증 검증에 실패했습니다.');
      return;
    }

    /* ===== 인증 정보 반영 ===== */
    nameInput.value = data.name || '';
    phoneInput.value = formatPhoneNumber(data.phone || '');
    birthInput.value = data.birthDate
      ? String(data.birthDate).replace(/-/g, '')
      : '';

    nameInput.readOnly = true;
    phoneInput.readOnly = true;
    birthInput.readOnly = true;

    /* 성별: MALE/FEMALE → M/F */
    if (data.gender) {
      const genderValue = data.gender === 'MALE' ? 'M' : 'F';
      const radio = signupForm.querySelector(
        `input[name="gender"][value="${genderValue}"]`
      );
      if (radio) {
        radio.disabled = false; // submit 전달 보장
        radio.checked = true;
      }
    }

    isAuthVerified = true;

    sendAuthBtn.textContent = '인증 완료';
    sendAuthBtn.disabled = true;
    sendAuthBtn.classList.add('is-disabled');

    alert('본인인증이 완료되었습니다.');
  } catch (e) {
    console.error(e);
    alert('본인인증 중 오류가 발생했습니다.');
  }
});

/* =========================
   5. 최종 제출 검증 (FULL CHECK)
========================= */
signupForm.addEventListener('submit', (e) => {
  const errors = [];
  const genderSelected = signupForm.querySelector(
    'input[name="gender"]:checked'
  );

  const birthReg =
    /^(19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])$/;

  /* 이메일 */
  if (!emailInput.value.trim()) {
    errors.push({ message: '이메일을 입력해주세요.', el: emailInput });
  } else if (!isEmailVerified) {
    errors.push({
      message: '이메일 중복 확인을 완료해주세요.',
      el: checkEmailBtn
    });
  }

  /* 비밀번호 */
  if (!passwordInput.value) {
    errors.push({ message: '비밀번호를 입력해주세요.', el: passwordInput });
  } else if (!passwordConfirm.value) {
    errors.push({
      message: '비밀번호 확인을 입력해주세요.',
      el: passwordConfirm
    });
  } else if (!checkPasswords()) {
    errors.push({
      message: '비밀번호가 일치하지 않거나 조건을 만족하지 않습니다.',
      el: passwordInput
    });
  }

  /* 닉네임 */
  if (!nicknameInput.value.trim()) {
    errors.push({ message: '닉네임을 입력해주세요.', el: nicknameInput });
  }

  /* 본인인증 */
  if (!isAuthVerified) {
    errors.push({
      message: '휴대폰 본인인증을 완료해주세요.',
      el: sendAuthBtn
    });
  }

  /* 성별 */
  if (!genderSelected) {
    errors.push({
      message: '성별을 선택해주세요.',
      el: signupForm.querySelector('.gender-select')
    });
  }

  /* 생년월일 */
  if (!birthInput.value.trim()) {
    errors.push({
      message: '생년월일을 입력해주세요.',
      el: birthInput
    });
  } else if (!birthReg.test(birthInput.value)) {
    errors.push({
      message: '생년월일은 8자리 숫자로 입력해주세요. (예: 19990101)',
      el: birthInput
    });
  }

  /* 에러 존재 시 차단 */
  if (errors.length > 0) {
    e.preventDefault();
    const first = errors[0];
    alert(first.message);
    first.el?.focus?.();
    return;
  }
  
  /* 🌟 추가: 전부 통과했다면 전송 직전에 휴대폰 번호 하이픈 제거 */
    const purePhone = phoneInput.value.replace(/[^0-9]/g, '');
    phoneInput.value = purePhone;
  /* 전부 통과 → 정상 submit */
});
