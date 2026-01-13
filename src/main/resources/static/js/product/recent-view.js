/**
 * 최근 본 상품 저장 (localStorage)
 * - 로그인 / session / DB 무관
 * - 브라우저 기준
 */
(function () {

  const STORAGE_KEY = "recentProducts";
  const MAX_COUNT = 8;

  document.addEventListener("DOMContentLoaded", () => {

    const productIdElement = document.getElementById("productId");

    if (!productIdElement) {
      return;
    }

    const productId = productIdElement.value;

    if (!productId) {
      return;
    }

    saveRecentProduct(productId);
  });

  function saveRecentProduct(productId) {

    let recentList = [];

    try {
      const stored = localStorage.getItem(STORAGE_KEY);
      if (stored) {
        recentList = JSON.parse(stored);
      }
    } catch (e) {
      recentList = [];
    }

    // 중복 제거
    recentList = recentList.filter(id => id !== productId);

    // 최신 상품을 맨 앞에
    recentList.unshift(productId);

    // 최대 개수 제한
    if (recentList.length > MAX_COUNT) {
      recentList = recentList.slice(0, MAX_COUNT);
    }

    localStorage.setItem(STORAGE_KEY, JSON.stringify(recentList));
  }

})();
