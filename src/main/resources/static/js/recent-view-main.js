(function () {
  const STORAGE_KEY = "recentProducts";

  document.addEventListener("DOMContentLoaded", () => {

    const ids = getRecentProductIds();
    if (ids.length === 0) return;

    const params = new URLSearchParams(window.location.search);
    const paramValue = params.get("recentIds"); // ex: "24"
    const storageValue = ids.join(",");         // ex: "27,25,24,26"

    // ✅ URL과 localStorage가 같으면 submit 안 함
    if (paramValue === storageValue) {
      return;
    }

    const input = document.getElementById("recentProductIds");
    if (!input) return;

    input.value = storageValue;
    input.form.submit();
  });

  function getRecentProductIds() {
    try {
      const stored = localStorage.getItem(STORAGE_KEY);
      return stored ? JSON.parse(stored) : [];
    } catch (e) {
      return [];
    }
  }
})();
