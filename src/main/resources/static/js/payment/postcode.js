/**
 * 카카오 우편번호 검색 및 주소 입력 로직
 */
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 주소 변수 선언
            let addr = ''; 

            // 사용자가 선택한 주소 타입(도로명/지번)에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { 
                addr = data.roadAddress;
            } else { 
                addr = data.jibunAddress;
            }

            // [DOM 매핑] name 속성을 사용하여 결제 페이지의 input에 데이터 삽입
            const zipCodeInput = document.getElementsByName("zipCode")[0];
            const addressInput = document.getElementsByName("address")[0];
            const detailInput = document.getElementsByName("addressDetail")[0];

            if (zipCodeInput) zipCodeInput.value = data.zonecode;
            if (addressInput) addressInput.value = addr;
            
            // 상세주소 필드로 포커스 이동
            if (detailInput) detailInput.focus();
        }
    }).open();
}


document.addEventListener("DOMContentLoaded", function() {
    const deliverySelect = document.getElementById("deliverySelect");
    const directInput = document.getElementById("directInput");
    const checkoutForm = document.querySelector(".checkout-layout");

    // 1. 직접 입력창 토글 로직
    if (deliverySelect && directInput) {
        deliverySelect.addEventListener("change", function() {
            if (this.value === "직접 입력") {
                directInput.style.display = "block";
                directInput.focus();
            } else {
                directInput.style.display = "none";
                directInput.value = "";
            }
        });
    }

    // 2. 폼 전송 시 처리 로직 (반드시 DOMContentLoaded 안에 작성)
    if (checkoutForm) {
        checkoutForm.addEventListener("submit", function(e) {
            if (deliverySelect.value === "직접 입력") {
                if (directInput.value.trim() !== "") {
                    const opt = document.createElement('option');
                    opt.value = directInput.value;
                    opt.selected = true;
                    opt.style.display = 'none';
                    deliverySelect.appendChild(opt);
                }
            }
        });
    }
});