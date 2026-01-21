document.addEventListener("DOMContentLoaded", () => {
  const sidebar = document.querySelector(".recent-sidebar");
  const heroWrap = document.querySelector(".hero-wrap");

  if (!sidebar || !heroWrap) return;

  const heroBottom = heroWrap.offsetTop + heroWrap.offsetHeight;

  window.addEventListener("scroll", () => {
    if (window.scrollY > heroBottom - 120) {
      // HERO 지나면 고정
      sidebar.style.position = "fixed";
      sidebar.style.top = "120px";
      sidebar.style.right = "calc(50% - 630px - 220px)";
      sidebar.style.transform = "none";
    } else {
      // HERO 구간에서는 배너 옆
      sidebar.style.position = "absolute";
      sidebar.style.top = "50%";
      sidebar.style.right = "-220px";
      sidebar.style.transform = "translateY(-50%)";
    }
  });
});
