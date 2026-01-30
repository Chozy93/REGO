document.addEventListener('DOMContentLoaded', function () {

  const searchInput = document.querySelector('.search-bar'); // ✔ 수정
  const tags = document.querySelectorAll('.filter-tags .tag');
  const noticeItems = document.querySelectorAll('.notice-item:not(.pinned)');
  const pinnedItems = document.querySelectorAll('.notice-item.pinned');

  /* =========================
     검색 이벤트
  ========================= */
  searchInput.addEventListener('input', function () {
    const keyword = this.value.toLowerCase().trim();
    filterContent(keyword, getActiveCategory());
  });

  /* =========================
     태그 클릭
  ========================= */
  tags.forEach(tag => {
    tag.addEventListener('click', function () {
      tags.forEach(t => t.classList.remove('active'));
      this.classList.add('active');

      const category = this.innerText.trim();
      filterContent(searchInput.value.toLowerCase().trim(), category);
    });
  });

  /* =========================
     통합 필터링
  ========================= */
  function filterContent(keyword, category) {
    let visibleCount = 0;

    noticeItems.forEach(item => {
      const title = item.querySelector('.item-title').innerText.toLowerCase();
      const itemCat = item.querySelector('.badge-gray').innerText.trim();

      const matchesKeyword =
        keyword === "" || title.includes(keyword);

      const matchesCategory =
        category === '전체' || itemCat === category;

      const isVisible = matchesKeyword && matchesCategory;

      item.style.display = isVisible ? 'flex' : 'none';
      if (isVisible) visibleCount++;
    });

    /* =========================
       pinned 처리
    ========================= */
    pinnedItems.forEach(item => {
      const title = item.querySelector('.item-title').innerText.toLowerCase();
      const isVisible =
        (keyword === "" && category === '전체') ||
        (title.includes(keyword));

      item.style.display = isVisible ? 'flex' : 'none';
    });

    toggleEmptyMessage(visibleCount);
  }

  /* =========================
     결과 없음 메시지
  ========================= */
  function toggleEmptyMessage(count) {
    let emptyItem = document.querySelector('.notice-empty');

    if (count === 0) {
      if (!emptyItem) {
        const li = document.createElement('li');
        li.className = 'notice-item notice-empty';
        li.style.textAlign = 'center';
        li.style.padding = '50px';
        li.innerText = '검색 결과가 없습니다.';
        document.querySelector('.notice-list').appendChild(li);
      }
    } else if (emptyItem) {
      emptyItem.remove();
    }
  }

  function getActiveCategory() {
    const activeTag = document.querySelector('.filter-tags .tag.active');
    return activeTag ? activeTag.innerText.trim() : '전체';
  }

});
