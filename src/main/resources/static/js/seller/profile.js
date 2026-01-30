$(function () {
    $("#sellerTabs").tabs();
  });
  
  
  $(function () {
    $('.review-stars').each(function () {
      const raw = $(this).data('rating');
      if (raw === undefined) return;

      const rating = Math.ceil(parseFloat(raw) * 10) / 10; // 안전하게 한 번 더
      const full = Math.floor(rating);
      const empty = 5 - full;

      let html = '';

      for (let i = 0; i < full; i++) html += '★';
      for (let i = 0; i < empty; i++) html += '✩';

      $(this).html(html);
    });
  });

  
  //프로필 공유
  $(function () {

    $('#btnProfileShare').on('click', function () {
      const url = window.location.origin + window.location.pathname;

      // 최신 브라우저
      if (navigator.clipboard) {
        navigator.clipboard.writeText(url)
          .then(() => {
            alert('프로필 주소가 복사되었습니다!');
          })
          .catch(() => {
            alert('주소 복사에 실패했습니다.');
          });
        return;
      }

      // 구형 브라우저 fallback
      const $temp = $('<input>');
      $('body').append($temp);
      $temp.val(url).select();
      document.execCommand('copy');
      $temp.remove();

      alert('프로필 주소가 복사되었습니다!');
    });

  });
