function filterCategory(category) {
    // 탭 활성화 스타일 변경
    const tags = document.querySelectorAll('.tag');
    tags.forEach(tag => {
        tag.classList.remove('active');
        if(tag.innerText === category) tag.classList.add('active');
    });

    // 리스트 필터링
    const items = document.querySelectorAll('.faq-item');
    items.forEach(item => {
        const itemCat = item.getAttribute('data-category');
        if(category === '전체' || itemCat === category) {
            item.style.display = 'block';
        } else {
            item.style.display = 'none';
        }
    });
}

function filterFaq() {
    const keyword = document.getElementById('faqSearch').value.toLowerCase();
    const items = document.querySelectorAll('.faq-item');
    
    items.forEach(item => {
        const question = item.querySelector('.question').innerText.toLowerCase();
        if(question.includes(keyword)) {
            item.style.display = 'block';
        } else {
            item.style.display = 'none';
        }
    });
}