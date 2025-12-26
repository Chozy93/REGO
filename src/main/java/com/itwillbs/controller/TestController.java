package com.itwillbs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

	@GetMapping("/")
	public String testMain(Model model) {

		/*
		 * ========================================= 1) HERO 더미 (메인 상단 배너) - hero 이미지도
		 * "URL 그대로" 내려줌 - 템플릿에서 th:style 로 background-image 또는 <img>로 사용
		 * =========================================
		 */
		Map<String, Object> hero = new LinkedHashMap<>();
		hero.put("title", "동네 이웃들과\n따뜻한 거래를 시작하세요");
		hero.put("desc", "가까운 이웃과 함께하는 중고거래, RE: GO에서 지금 바로 만나보세요.");
		hero.put("ctaText", "지금 시작하기");
		hero.put("ctaLink", "#");

		/*
		 * ========================================= 2) 인기 검색어 더미 - 헤더에서 th:each로 뿌리면 됨
		 * - 하드코딩 금지: 컨트롤러에서 내려줌 =========================================
		 */
		List<Map<String, Object>> popularKeywords = new ArrayList<>();

		popularKeywords.add(keyword("자전거", "#"));
		popularKeywords.add(keyword("아이패드", "#"));
		popularKeywords.add(keyword("의자", "#"));
		popularKeywords.add(keyword("스타벅스", "#"));
		popularKeywords.add(keyword("냉장고", "#"));

		model.addAttribute("popularKeywords", popularKeywords);

		/*
		 * ========================================= 3) 상품 더미 (카드에서 공통으로 쓰는 필드) - id,
		 * title, price, loc, time, img, status, likeCount, sellerName - status: null 이면
		 * 뱃지 숨김 / "예약중" 같은 값이면 표시 - ✅ 이미지 URL 전부 네가 준 HTML에서 "그대로" 사용
		 * =========================================
		 */
		List<Map<String, Object>> products = new ArrayList<>();

		// === AI 맞춤 상품 6개 ===
		products.add(product(101, "VR 헤드셋 팝니다", 350000, "서울 강남구", "10분 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuAXl5b90fkJeGLsBXn3c6YmHTAPdID1eBi9OL135RLFG-X3k1yk41_StHYhFQSpzLZyjnBYU7kds-q7m7D6rNpKH8cwPGYhQ--QHIiI-8StiFPnWjfwJM7g-X6_pk6vfpO66CzHBvqpYMIMnROAImm2Z3XQmjY7bddzSXtIUDKS6aXc0jwAO_wij2bXu0NZfZ91A7XvNG6MCbHfBBU-XNZA2xekMvwgxCPHCx2aG70N-_h-4zPgwGwVzecNPMy55HGyodRlIVGxJhw",
				"예약중", 12, "고래상점", true));

		products.add(product(102, "소니 헤드폰 팝니다", 250000, "인천 송도동", "5시간 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuBkN_h-DI37qptyTXDVKZcPRU6wAo2Fp0S5e3c7om8aTeFfONQ5QvqT-LWLzxf0dkFQdiTOxv0Oi5RhuPcBo4sxJelTYiO7q5kS2w77my0aotJAo3w1kL88ZNoOk2koU9x3hpv2FX-Jh5rrKmEYb3jv1l1s7f6Wp1jMg2zc74cOBtTQpqjt0E1tw4DmAX-Oau01118HKu_aHgPI9LdJtNcxoj436VFM05LhKuVHICFAYO0fbDO-TO0003HaUgGT3abiGffkFpGQQNY",
				null, 31, "리웨일마켓", false));

		products.add(product(103, "1인용 인테리어 의자", 80000, "경기 성남시", "1일 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuDS8ICft3Nskz6h3UX1yI-JY0Jb3MLtvBaUlw8jcvw_RyNl8X--EpnQ3y-qao2AY-rgiuemBrDsZ7VHaQmZMo2OXDDEScP265Sxc58XVR5L3h0ZDpQ7Cx5_Cj1H77Tn6kCQbkGxdpH4BwhtdZyU5zwlLrQ6lAI4ZXir2FtGLwIxoZKym83wOAjhhI-KmZC5p_0q8vYr9GCpCpySopEKq5Aey8yORBUb6Hlns-HdpvQVIy4ZxyJJcmGvDn8nAsY9dM4LzkDKbOtZaek",
				null, 8, "초록상회", false));

		products.add(product(104, "27인치 게이밍 모니터", 180000, "대구 수성구", "3일 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuDYKjjWGnqVvpc14-N9HOE7NbUuwGjTYHULdaqDXo9IMtvlUkp_4Cb7W36LAAhR6_VmL8Y96WuXEUqI55KQFIzv1ACVvHp0HG23agwo1-rKLK9z6Bm79kAN8PG-6jI2FTFEMs1N1uMAoRwrUBeBGpjem2EyhntOSWzfpRrSPuYbUESs__c_UIyE4pciiPBnn1pIjIsevzF_qh4bR0U3o-ZbRdFBe1bsxlgpiGqdCOQFs-WWCzBJhQnjNhmAn2WS0uyWLYaBPbUL1Lg",
				"예약중", 22, "동네전자", true));

		products.add(product(105, "아이패드 미니 6세대", 450000, "서울 마포구", "2일 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuDYiK_dFhHWrKqocnDx_PMbBsDfkPSi7GeAy2YJtTxV9CSNtss93z7CPXv1ce6QCX_Ad1viEXW5wKxEDkoyIKVGNt4HquvLn8u2SD0hZ_9bQ-S63N5sA-2NyNyZ42kC5c39791HwLO8RLNbjVx09DTd37XLxhG9uFkO7qJNMVEQDN8YtTEPYdVoyJ9lPcZPImcCNuZUTsDJdgFJ2F3qa68YIzTxsOsbUqsG7vbgKGMMbHfas8RvwJyM2ANjV5hSQUaRaRtOUiSf7g0",
				null, 15, "마포구민", false));
		products.add(product(106, "네스프레소 커피머신", 85000, "경기 분당구", "4일 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuDSFHBqGhnsdLKbQFtkQiWunVEP-_x_KVmLqQooaYeTZdEH13NF0eRcl_jbHkZTU-QJy7zmvSzd2K6F9FbwdpBFVWpP0CvN5u9SO4siadRi-1x5E8PfPh6HA6hSHLi-r7ym1dypV1pWYO6fsNajtYLqQWVsSwYHk2tWAe3f1xePea9rsCKScVOXNPMr5roYKlgPerDlhCurQq9CXTDKt6o1FU5ZPsIKWkHb97bIueVbraU5ztyz7h4QSdqFOlChJ2HH3MxgDb2Msjo",
				null, 9, "분당맘", false));

		// === 인기 상품 6개 ===
		products.add(product(201, "나이키 운동화 270", 90000, "광주 서구", "방금 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuBkN_h-DI37qptyTXDVKZcPRU6wAo2Fp0S5e3c7om8aTeFfONQ5QvqT-LWLzxf0dkFQdiTOxv0Oi5RhuPcBo4sxJelTYiO7q5kS2w77my0aotJAo3w1kL88ZNoOk2koU9x3hpv2FX-Jh5rrKmEYb3jv1l1s7f6Wp1jMg2zc74cOBtTQpqjt0E1tw4DmAX-Oau01118HKu_aHgPI9LdJtNcxoj436VFM05LhKuVHICFAYO0fbDO-TO0003HaUgGT3abiGffkFpGQQNY",
				null, 54, "광주러너", false));

		products.add(product(202, "하이브리드 자전거", 150000, "대전 유성구", "10분 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuDS8ICft3Nskz6h3UX1yI-JY0Jb3MLtvBaUlw8jcvw_RyNl8X--EpnQ3y-qao2AY-rgiuemBrDsZ7VHaQmZMo2OXDDEScP265Sxc58XVR5L3h0ZDpQ7Cx5_Cj1H77Tn6kCQbkGxdpH4BwhtdZyU5zwlLrQ6lAI4ZXir2FtGLwIxoZKym83wOAjhhI-KmZC5p_0q8vYr9GCpCpySopEKq5Aey8yORBUb6Hlns-HdpvQVIy4ZxyJJcmGvDn8nAsY9dM4LzkDKbOtZaek",
				"예약중", 61, "자전거형", false));

		products.add(product(203, "이케아 보조 테이블", 15000, "부산 진구", "30분 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuDYKjjWGnqVvpc14-N9HOE7NbUuwGjTYHULdaqDXo9IMtvlUkp_4Cb7W36LAAhR6_VmL8Y96WuXEUqI55KQFIzv1ACVvHp0HG23agwo1-rKLK9z6Bm79kAN8PG-6jI2FTFEMs1N1uMAoRwrUBeBGpjem2EyhntOSWzfpRrSPuYbUESs__c_UIyE4pciiPBnn1pIjIsevzF_qh4bR0U3o-ZbRdFBe1bsxlgpiGqdCOQFs-WWCzBJhQnjNhmAn2WS0uyWLYaBPbUL1Lg",
				null, 18, "부산살이", false));

		products.add(product(204, "아이패드 에어 4세대", 500000, "인천 부평구", "1시간 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuBkN_h-DI37qptyTXDVKZcPRU6wAo2Fp0S5e3c7om8aTeFfONQ5QvqT-LWLzxf0dkFQdiTOxv0Oi5RhuPcBo4sxJelTYiO7q5kS2w77my0aotJAo3w1kL88ZNoOk2koU9x3hpv2FX-Jh5rrKmEYb3jv1l1s7f6Wp1jMg2zc74cOBtTQpqjt0E1tw4DmAX-Oau01118HKu_aHgPI9LdJtNcxoj436VFM05LhKuVHICFAYO0fbDO-TO0003HaUgGT3abiGffkFpGQQNY",
				"예약중", 40, "부평전자", false));

		products.add(product(205, "필름 카메라 (미놀타)", 120000, "서울 종로구", "2시간 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuCIQGDnj8GK6eV3SLkS8z1f9HzTY3izer_CarGnw8AdAwzU_oKw4TDXqTy7mr9XbJKOmnM33inApr5m7XwzwId7JKigbbwjPvemlluN32qKZISIpvkR_vWnVNK5CnqzI6ir-vZ9Xlyy_MpESkxlEU3FZR3LqvoD23IPrG90vaYQSi8y-iWeL4vyFtlbnVZqhu6nwziTea7jkp9RsJxhnG46yaStWGyCwEpgOAioWHJZFRuMFHvS88p18dWD4_GEEjaWyczi9JPMM6w",
				null, 27, "종로사진관", false));

		products.add(product(206, "펜더 일렉기타", 850000, "경기 일산동구", "3시간 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuA7QUK0UhDOP1L9vRyZDOgtqbFa424ZNdY6DJ8JSvXJ3ioTcU10Hv206D8D51tJkQt9F-q09Qldwb0rwhZ4W2vPQJHhY-95lKh5NccCAEuinAbQKTz3VywIGigGnAoQmJ5Qz3g1OhW8Ged5UqFZxq4wq3zDAFFlIEYZiIyiavyuxPFVXqXoRnFAK4N8bK-3ye7MrpVwOR44kYi7p8x7MyvswmPiGOHRAJKHWCDS0qxAaHnB4n8NXSPNgLn0-G3k5xbepum3LrV3dSQ",
				null, 33, "일산뮤지션", false));

		// === 최근에 올라온 상품 6개 ===
		products.add(product(301, "맥북 프로 16인치", 1200000, "서울 용산구", "방금 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuDYKjjWGnqVvpc14-N9HOE7NbUuwGjTYHULdaqDXo9IMtvlUkp_4Cb7W36LAAhR6_VmL8Y96WuXEUqI55KQFIzv1ACVvHp0HG23agwo1-rKLK9z6Bm79kAN8PG-6jI2FTFEMs1N1uMAoRwrUBeBGpjem2EyhntOSWzfpRrSPuYbUESs__c_UIyE4pciiPBnn1pIjIsevzF_qh4bR0U3o-ZbRdFBe1bsxlgpiGqdCOQFs-WWCzBJhQnjNhmAn2WS0uyWLYaBPbUL1Lg",
				null, 5, "용산중고", false));

		products.add(product(302, "갤럭시 워치 4", 130000, "서울 강서구", "1분 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuAXl5b90fkJeGLsBXn3c6YmHTAPdID1eBi9OL135RLFG-X3k1yk41_StHYhFQSpzLZyjnBYU7kds-q7m7D6rNpKH8cwPGYhQ--QHIiI-8StiFPnWjfwJM7g-X6_pk6vfpO66CzHBvqpYMIMnROAImm2Z3XQmjY7bddzSXtIUDKS6aXc0jwAO_wij2bXu0NZfZ91A7XvNG6MCbHfBBU-XNZA2xekMvwgxCPHCx2aG70N-_h-4zPgwGwVzecNPMy55HGyodRlIVGxJhw",
				"예약중", 11, "강서러", false));

		products.add(product(303, "2인용 패브릭 소파", 70000, "경기 용인시", "5분 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuDS8ICft3Nskz6h3UX1yI-JY0Jb3MLtvBaUlw8jcvw_RyNl8X--EpnQ3y-qao2AY-rgiuemBrDsZ7VHaQmZMo2OXDDEScP265Sxc58XVR5L3h0ZDpQ7Cx5_Cj1H77Tn6kCQbkGxdpH4BwhtdZyU5zwlLrQ6lAI4ZXir2FtGLwIxoZKym83wOAjhhI-KmZC5p_0q8vYr9GCpCpySopEKq5Aey8yORBUb6Hlns-HdpvQVIy4ZxyJJcmGvDn8nAsY9dM4LzkDKbOtZaek",
				null, 3, "용인집", false));

		products.add(product(304, "로지텍 버티컬 마우스", 55000, "부산 남구", "12분 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuDYKjjWGnqVvpc14-N9HOE7NbUuwGjTYHULdaqDXo9IMtvlUkp_4Cb7W36LAAhR6_VmL8Y96WuXEUqI55KQFIzv1ACVvHp0HG23agwo1-rKLK9z6Bm79kAN8PG-6jI2FTFEMs1N1uMAoRwrUBeBGpjem2EyhntOSWzfpRrSPuYbUESs__c_UIyE4pciiPBnn1pIjIsevzF_qh4bR0U3o-ZbRdFBe1bsxlgpiGqdCOQFs-WWCzBJhQnjNhmAn2WS0uyWLYaBPbUL1Lg",
				null, 7, "부산회사원", false));

		products.add(product(305, "원목 책상 1200", 60000, "인천 서구", "20분 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuAXl5b90fkJeGLsBXn3c6YmHTAPdID1eBi9OL135RLFG-X3k1yk41_StHYhFQSpzLZyjnBYU7kds-q7m7D6rNpKH8cwPGYhQ--QHIiI-8StiFPnWjfwJM7g-X6_pk6vfpO66CzHBvqpYMIMnROAImm2Z3XQmjY7bddzSXtIUDKS6aXc0jwAO_wij2bXu0NZfZ91A7XvNG6MCbHfBBU-XNZA2xekMvwgxCPHCx2aG70N-_h-4zPgwGwVzecNPMy55HGyodRlIVGxJhw",
				"예약중", 2, "서구원목", false));

		products.add(product(306, "LG 공기청정기 퓨리케어", 210000, "경기 수원시", "25분 전",
				"https://lh3.googleusercontent.com/aida-public/AB6AXuC3OczGvA5HNeQ8MMBPvc4IHHvn4vlmKMC_wxR1o4qT4ZklHxewwTzqocqNnyYxWDDN7V6St48QAclHokw8py5r_HYFvwsEqZ2yt04MPm3wzMqpwjaY-BC811Eig8byJg9mvib19P053mKWkh2Eq--SSP8XiLXjgTSLHDkYo-d2bNRQpIHMl-xaWFZPcQenHAkKuJbmnAg0YOLqLRCjd1obmXQI7u84KDcSGxatQORVXYaNImQgMlDmKO_91A2YKXIY8L9BVnvmKqQ",
				null, 4, "수원살이", true));

		/*
		 * ========================================= 4) 섹션별로 나눠서 내려주고 싶으면 (선택) - 템플릿에서
		 * 0~5 잘라 쓰는 대신 - 여기서 ai/hot/recent를 따로 내려줄 수도 있음
		 * =========================================
		 */
		model.addAttribute("aiProducts", products.subList(0, 6));
		model.addAttribute("hotProducts", products.subList(6, 12));
		model.addAttribute("recentProducts", products.subList(12, 18));

		return "main/main"; // templates/main/main.html
	}

	/*
	 * ========================================================= 아래는 "Map 생성" 헬퍼 메서드
	 * - 초보들도 보기 쉽게 필드 고정 - LinkedHashMap: 넣은 순서대로 유지(디버깅할 때 편함)
	 * =========================================================
	 */

	private static Map<String, Object> product(int id, String title, int price, String loc, String time, String img,
			String reserveText, // ← 기존 그대로 유지 (안 씀)
			int like, String seller, boolean reserved // ← 🔥 마지막에 추가
	) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("title", title);
		map.put("price", price);
		map.put("loc", loc);
		map.put("time", time);
		map.put("img", img);
		map.put("like", like);
		map.put("seller", seller);
		map.put("reserved", reserved); // ← boolean
		return map;
	}

	private static Map<String, Object> keyword(String text, String link) {
		Map<String, Object> map = new HashMap<>();
		map.put("text", text);
		map.put("link", link);
		return map;
	}

	
}
