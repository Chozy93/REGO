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