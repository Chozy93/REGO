document.addEventListener("DOMContentLoaded", () => {
  console.log("🔥 Swiper init start");

  const swipers = document.querySelectorAll(".product-swiper");
  console.log("🔥 found swipers:", swipers.length);

  swipers.forEach((el, index) => {
    console.log("🔥 init swiper", index);

    new Swiper(el, {
      slidesPerView: 5,
      spaceBetween: 24,

      navigation: {
        nextEl: el.querySelector(".swiper-button-next"),
        prevEl: el.querySelector(".swiper-button-prev"),
      },

      breakpoints: {
        0: {
          slidesPerView: 1.2,
        },
        480: {
          slidesPerView: 2,
        },
        768: {
          slidesPerView: 3,
        },
        1024: {
          slidesPerView: 5,
        },
      },
    });
  });
});
