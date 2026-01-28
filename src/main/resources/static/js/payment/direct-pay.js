/**
 * 결제 로직 통합 관리 함수
 */
function handlePayment(data) {
    const { impCode, productName, price, userEmail, userName } = data;
    
    // 1. 약관 동의 체크
    const termsChecked = document.getElementById('terms').checked;
    if (!termsChecked) {
        alert("주문 내용 확인 및 약관에 동의해주세요.");
        return;
    }

    // 2. 결제 수단 확인
    const paymentType = document.querySelector('input[name="paymentType"]:checked').value;
    const form = document.getElementById('checkoutForm');

    // 3. 결제 수단별 분기
    if (paymentType === 'CARD') {
        // 포트원 초기화 (IMP 변수가 상단에 선언되어 있어야 함)
        if (!window.IMP) return alert("결제 모듈을 불러올 수 없습니다.");
        const IMP = window.IMP;
        IMP.init(impCode);
		console.log(impCode);

        // 💳 카드 결제창 띄우기
        IMP.request_pay({
            pg: "tosspay", 
            pay_method: "card",
            merchant_uid: "order_" + new Date().getTime(),
            name: productName,
            amount: price,
            buyer_email: userEmail,
            buyer_name: userName
        }, function (rsp) {
            if (rsp.success) {
				// 1. impUid 추가 (이미 하신 것)
				                const inputImp = document.createElement('input');
				                inputImp.type = 'hidden';
				                inputImp.name = 'impUid';
				                inputImp.value = rsp.imp_uid;
				                form.appendChild(inputImp);

				                // 2. merchantUid 추가 (🚩 중요: 백엔드 로그 저장용)
				                const inputMerchant = document.createElement('input');
				                inputMerchant.type = 'hidden';
				                inputMerchant.name = 'merchantUid';
				                inputMerchant.value = rsp.merchant_uid;
				                form.appendChild(inputMerchant);

				                form.submit();
            } else {
                alert("결제 취소 또는 실패: " + rsp.error_msg);
            }
        });
    } else {
        // ⚡ RE:PAY(원래 결제)는 그냥 폼 바로 제출
        form.submit();
    }
}