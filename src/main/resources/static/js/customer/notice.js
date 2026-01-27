document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.querySelector('.search-input');
    const tags = document.querySelectorAll('.filter-tags .tag');
    const noticeItems = document.querySelectorAll('.notice-item:not(.pinned)'); // 고정글은 제외하고 필터링하거나 포함 가능
    const pinnedItems = document.querySelectorAll('.notice-item.pinned');

    // 1. 검색 기능 (제목 기준)
    searchInput.addEventListener('keyup', function() {
        const keyword = this.value.toLowerCase().trim();
        
        filterContent(keyword, getActiveCategory());
    });

    // 2. 탭 클릭 이벤트 (카테고리 필터링)
    tags.forEach(tag => {
        tag.addEventListener('click', function() {
            // 태그 활성화 스타일 교체
            tags.forEach(t => t.classList.remove('active'));
            this.classList.add('active');

            const category = this.innerText.trim();
            filterContent(searchInput.value.toLowerCase().trim(), category);
        });
    });

    // 3. 통합 필터링 함수
    function filterContent(keyword, category) {
        let visibleCount = 0;

        noticeItems.forEach(item => {
            const title = item.querySelector('.item-title').innerText.toLowerCase();
            const itemCat = item.querySelector('.badge-gray').innerText.trim();
            
            const matchesKeyword = title.includes(keyword);
            const matchesCategory = (category === '전체' || itemCat === category);

            if (matchesKeyword && matchesCategory) {
                item.style.display = 'flex';
                visibleCount++;
            } else {
                item.style.display = 'none';
            }
        });

        // 고정글(pinned) 처리: 검색어가 있을 때는 고정글도 필터링, 없을 땐 항상 노출
        pinnedItems.forEach(item => {
            const title = item.querySelector('.item-title').innerText.toLowerCase();
            if (keyword === "" || title.includes(keyword)) {
                item.style.display = 'flex';
            } else {
                item.style.display = 'none';
            }
        });
    }

    function getActiveCategory() {
        const activeTag = document.querySelector('.filter-tags .tag.active');
        return activeTag ? activeTag.innerText.trim() : '전체';
    }
});