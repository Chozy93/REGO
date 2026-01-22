/* * 페이지 로드 시 HTML input 태그에서 초기 잔액 값을 가져옴 
 */
let initialBalance = 0;

document.addEventListener('DOMContentLoaded', function() {
    // HTML에 숨겨둔 hidden input의 값을 읽어옴
    const balanceInput = document.getElementById('initialBalanceData');
    if (balanceInput) {
        initialBalance = parseInt(balanceInput.value) || 0;
    }
});

// ================= DOM 요소 참조 =================
const accountModal = document.getElementById('accountModal');
const chargeModal = document.getElementById('chargeModal');
const chargeInput = document.getElementById('chargeAmountInput');
const expectedDisplay = document.getElementById('expectedBalanceDisplay');

// ================= 공통: 모달 바깥 클릭 시 닫기 =================
window.onclick = function(e) {
    if (e.target === accountModal) {
        closeAccountModal();
    }
    if (e.target === chargeModal) {
        closeChargeModal();
    }
}

// ================= 1. 계좌 변경 모달 로직 =================
function openAccountModal() {
    accountModal.style.display = 'flex';
    document.body.style.overflow = 'hidden'; // 배경 스크롤 막기
}

function closeAccountModal() {
    accountModal.style.display = 'none';
    document.body.style.overflow = 'auto';
}

document.addEventListener('DOMContentLoaded', function() {
    const deleteButtons = document.querySelectorAll('.btn-delete-account');

    deleteButtons.forEach(button => {
        button.onclick = function(e) {
            e.preventDefault();
            e.stopPropagation();

            const accountId = this.getAttribute('data-id');
            console.log("삭제하려는 계좌 ID:", accountId);

            if (!accountId || accountId === 'null') {
                alert("계좌 ID를 찾을 수 없습니다.");
                return;
            }

            if (confirm('해당 계좌 연동을 해지하시겠습니까?')) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '/pay/delete-account';

                // 1. 계좌 ID 추가
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'accountId';
                input.value = accountId;
                form.appendChild(input);

                // 2. CSRF 토큰 추가 (스프링 시큐리티 대응)
                // 보통 레이아웃(base.html)의 메타 태그에서 가져옵니다.
                const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
                if (csrfToken) {
                    const csrfInput = document.createElement('input');
                    csrfInput.type = 'hidden';
                    csrfInput.name = '_csrf'; // 스프링 시큐리티 기본 설정 파라미터명
                    csrfInput.value = csrfToken;
                    form.appendChild(csrfInput);
                }

                document.body.appendChild(form);
                form.submit();
            }
        };
    });
});



// 이전 코드에서 openModal 함수가 섞여 있어 호환성을 위해 추가 (필요시 삭제 가능)
function openModal() {
    openAccountModal();
}
function closeModal() {
    closeAccountModal();
}

// ================= 2. 충전 모달 로직 =================
function openChargeModal() {
    chargeModal.style.display = 'flex';
    document.body.style.overflow = 'hidden';
    resetChargeAmount(); // 모달 열 때 금액 초기화
}

function closeChargeModal() {
    chargeModal.style.display = 'none';
    document.body.style.overflow = 'auto';
}

// 금액 버튼 클릭 (+1만, +5만 등)
function addChargeAmount(amount) {
    let currentVal = parseInt(chargeInput.value.replace(/,/g, '')) || 0;
    let newVal = currentVal + amount;
    
    // 화면 입력창 업데이트
    chargeInput.value = newVal.toLocaleString();
    // 예상 잔액 업데이트
    updateExpectedBalance(newVal);
}

// 초기화 버튼
function resetChargeAmount() {
    chargeInput.value = "0";
    updateExpectedBalance(0);
}

// 키보드 입력 시 콤마 포맷팅 및 계산
function formatChargeInput(element) {
    let value = element.value.replace(/[^0-9]/g, ''); // 숫자 이외 문자 제거
    if (value === '') value = '0';
    
    let numValue = parseInt(value, 10);
    element.value = numValue.toLocaleString();
    
    updateExpectedBalance(numValue);
}

// 예상 잔액 계산 업데이트 함수
function updateExpectedBalance(addAmount) {
    const total = initialBalance + addAmount;
    
    // 예상 잔액 표시
    if (expectedDisplay) {
        expectedDisplay.innerText = total.toLocaleString() + '원';
    }
}



// ================= 3. 출금(Withdraw) 로직 =================
// 1. 출금 입력창과 예상 잔액 표시창 참조 (충전하기와 동일한 방식)
const withdrawInput = document.getElementById('withdrawAmountInput');
const withdrawExpectedDisplay = document.getElementById('expectedWithdrawBalance');

function openWithdrawModal() {
    withdrawModal.style.display = 'flex';
    document.body.style.overflow = 'hidden';
    withdrawInput.value = "0";
    formatWithdrawInput(withdrawInput);
}

function closeWithdrawModal() {
    withdrawModal.style.display = 'none';
    document.body.style.overflow = 'auto';
}

// 칩 버튼 클릭 (+1만 등)
function addWithdrawAmount(amount) {
    let currentVal = parseInt(withdrawInput.value.replace(/,/g, '')) || 0;
    let newVal = currentVal + amount;
    
    // 잔액 초과 방지
    if (newVal > initialBalance) newVal = initialBalance;
    
    withdrawInput.value = newVal.toLocaleString();
    formatWithdrawInput(withdrawInput);
}

// 2. 금액 버튼 클릭 (addChargeAmount 복사)
function addWithdrawAmount(amount) {
    let currentVal = parseInt(withdrawInput.value.replace(/,/g, '')) || 0;
    let newVal = currentVal + amount;
    
    // [추가된 로직] 잔액보다 많이 출금할 수 없게 제한
    if (newVal > initialBalance) newVal = initialBalance;
    
    withdrawInput.value = newVal.toLocaleString();
    updateWithdrawExpectedBalance(newVal); // 예상 잔액 업데이트
}

// 3. 전액 버튼 (resetChargeAmount 변형)
function setFullWithdrawAmount() {
    withdrawInput.value = initialBalance.toLocaleString();
    updateWithdrawExpectedBalance(initialBalance);
}

// 4. 키보드 입력 시 포맷팅 (formatChargeInput 복사)
function formatWithdrawInput(element) {
    let value = element.value.replace(/[^0-9]/g, ''); 
    if (value === '') value = '0';
    
    let numValue = parseInt(value, 10);
    
    // 잔액 초과 방지
    if (numValue > initialBalance) numValue = initialBalance;
    
    element.value = numValue.toLocaleString();
    updateWithdrawExpectedBalance(numValue);
}


// 5. 예상 잔액 계산 (updateExpectedBalance 복사, +를 -로 변경)
function updateWithdrawExpectedBalance(subAmount) {
    const total = initialBalance - subAmount; // 출금이니까 마이너스
    
    if (withdrawExpectedDisplay) {
        // 출금 후 잔액 표시 (내부 span 태그가 있다면 그곳에, 없다면 전체에)
        const valSpan = withdrawExpectedDisplay.querySelector('span');
        if (valSpan) {
            valSpan.innerText = total.toLocaleString();
        } else {
            withdrawExpectedDisplay.innerText = total.toLocaleString() + '원';
        }
    }
}


// 서버 전송
async function submitWithdraw() {
    const amountStr = withdrawInput.value.replace(/,/g, '');
    const amount = parseInt(amountStr);

    if (amount <= 0) {
        alert("출금 금액을 입력해주세요.");
        return;
    }

    if (!confirm(amount.toLocaleString() + "원을 출금하시겠습니까?")) return;

    try {
        const response = await fetch('/api/payment/withdraw', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ amount: amountStr })
        });

        if (response.ok) {
            alert("출금이 완료되었습니다.");
            location.reload();
        } else {
            const errorMsg = await response.text();
            alert("출금 실패: " + errorMsg);
        }
    } catch (error) {
        alert("서버 통신 중 오류가 발생했습니다.");
    }
}