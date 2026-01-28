document.addEventListener("DOMContentLoaded", () => {

  document.querySelectorAll("[data-parent-id]").forEach(btn => {
    btn.addEventListener("click", () => {
      const parentId = btn.dataset.parentId;

      fetch(`/api/products/by-parent/${parentId}`)
        .then(res => res.json())
        .then(products => {
          renderProducts(products);
        });
    });
  });

});

function renderProducts(products) {
  const grid = document.getElementById("productGrid");
  grid.innerHTML = ""; // 🔥 기존 카드 삭제

  products.forEach(p => {
    const card = document.createElement("article");
    card.className = "product-card";

    card.innerHTML = `
      <a href="/product/${p.id}" class="product-card__link">
        <div class="product-card__thumb">
          <img src="${p.img}" alt="">
        </div>
        <div class="product-card__body">
          <h3 class="product-card__title">${p.title}</h3>
          <p class="product-card__price">${p.price.toLocaleString()}원</p>
          <p class="product-card__meta">${p.loc} · ${p.time}</p>
        </div>
      </a>
    `;

    grid.appendChild(card);
  });
}
