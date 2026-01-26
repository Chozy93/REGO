document.addEventListener("DOMContentLoaded", () => {
  const mega = document.getElementById("categoryMega");
  if (!mega) return;

  const toggleBtn = mega.querySelector(".category-mega-toggle");
  const panel = mega.querySelector(".category-mega-panel");

  // 1️⃣ 버튼 클릭 → 드롭다운 열기/닫기
  toggleBtn.addEventListener("click", () => {
    panel.classList.toggle("is-open");
  });

  // 2️⃣ 대분류 클릭 → Ajax 호출
  mega.addEventListener("click", (e) => {
    const parentItem = e.target.closest("[data-parent-id]");
    if (!parentItem) return;

    const parentId = parentItem.dataset.parentId;

    fetch(`/api/products/by-parent/${parentId}`)
      .then(res => res.json())
      .then(data => {
        console.log("응답:", data);

        // 버튼 텍스트 변경
        toggleBtn.querySelector("span").innerText = data.parentCategoryName;

        // 소분류 갱신
        const subGrid = mega.querySelector(".category-mega-sub-grid");
        subGrid.innerHTML = "";

        data.subCategories.forEach(sub => {
          const a = document.createElement("a");
          a.href = `/products?categoryId=${sub.categoryId}`;
          a.innerText = sub.categoryName;
          subGrid.appendChild(a);
        });

        // 상품 카드 갱신
        document.querySelector(".product-grid").innerHTML = data.productHtml;
      });
  });
});

