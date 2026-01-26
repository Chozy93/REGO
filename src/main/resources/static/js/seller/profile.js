$(function () {
    $("#sellerTabs").tabs();
  });
  
  
  $(function () {
    $('.review-stars').each(function () {
      const raw = $(this).data('rating');
      if (raw === undefined) return;

      const rating = parseFloat(raw); // 1 ~ 10 (avg는 double 가능)
      const starValue = rating / 2;   // 0 ~ 5

      const full = Math.floor(starValue);
      const half = starValue % 1 >= 0.5;
      const empty = 5 - full - (half ? 1 : 0);

      let html = '';

      for (let i = 0; i < full; i++) html += '★';
      if (half) html += '☆';
      for (let i = 0; i < empty; i++) html += '✩';

      $(this).html(html);
    });
  });
