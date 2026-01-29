function changeUserRole(target) {
    // data-user-id 속성에서 ID 추출
    const userId = target.getAttribute('data-user-id');
    // 현재 선택된 값 추출
    const newRole = target.value;

    if(!confirm(`사용자의 권한을 ${newRole}로 변경하시겠습니까?`)) {
        location.reload(); 
        return;
    }

    const token = document.querySelector('meta[name="_csrf"]')?.content;
    const header = document.querySelector('meta[name="_csrf_header"]')?.content;

    fetch(`/admin/users/${userId}/role?role=${newRole}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            [header]: token
        }
    })
    .then(res => {
        if(res.ok) {
            alert("권한이 변경되었습니다.");
        } else {
            alert("변경 실패");
            location.reload();
        }
    });
}


// user 상태 업데이트

function changeUserStatus(target) {
    const userId = target.getAttribute('data-user-id');
    const newStatus = target.value;

    if(!confirm("회원의 상태를 변경하시겠습니까?")) {
        location.reload();
        return;
    }

    const token = document.querySelector('meta[name="_csrf"]')?.content;
    const header = document.querySelector('meta[name="_csrf_header"]')?.content;

    // API 호출 (Controller에서 @RequestParam("status")로 받기로 한 경우)
    fetch(`/admin/users/${userId}/status?status=${newStatus}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            [header]: token
        }
    })
    .then(res => {
        if(res.ok) {
            alert("상태가 성공적으로 변경되었습니다.");
            location.reload(); // 클래스(색상) 갱신을 위해 새로고침
        } else {
            alert("변경에 실패했습니다.");
            location.reload();
        }
    });
}