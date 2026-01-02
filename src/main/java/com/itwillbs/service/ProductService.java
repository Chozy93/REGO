package com.itwillbs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.dto.MainProductListDTO;
import com.itwillbs.dto.ProductDetailDTO;

@Service
public class ProductService {

    public List<MainProductListDTO> getMainProductList() {

        List<MainProductListDTO> list = new ArrayList<>();

        list.add(new MainProductListDTO(
            1L,
            "아이패드 미니 6세대",
            450000,
            "서울 마포구",
            "이미지URL"
        ));

        list.add(new MainProductListDTO(
            2L,
            "27인치 게이밍 모니터",
            180000,
            "대구 수성구",
            "이미지URL"
        ));

        return list;
    }

	public ProductDetailDTO getProductDetail(Long id) {
		
		ProductDetailDTO dto = new ProductDetailDTO();
		
		dto.setProductId(id);
	    dto.setProductName("필름 카메라 입문용 팝니다");
	    dto.setPrice(120000);
	    dto.setPriceDisplay("120,000원");
	    dto.setDescription("입문용으로 구매했던 필름 카메라입니다.");

	    dto.setImageUrls(List.of(
	        "https://lh3.googleusercontent.com/aida-public/AB6AXuDYKjjWGnqVvpc14-N9HOE7NbUuwGjTYHULdaqDXo9IMtvlUkp_4Cb7W36LAAhR6_VmL8Y96WuXEUqI55KQFIzv1ACVvHp0HG23agwo1-rKLK9z6Bm79kAN8PG-6jI2FTFEMs1N1uMAoRwrUBeBGpjem2EyhntOSWzfpRrSPuYbUESs__c_UIyE4pciiPBnn1pIjIsevzF_qh4bR0U3o-ZbRdFBe1bsxlgpiGqdCOQFs-WWCzBJhQnjNhmAn2WS0uyWLYaBPbUL1Lg"
	    ));

	    dto.setSellerNickname("카메라매니아");
	    dto.setSellerRegion("서울 강남구");
	    dto.setSellerTemperature(36.5);

	    dto.setConditionLabel("사용감 적음");
	    dto.setViewCount(342);
	    dto.setLikeCount(24);
	    dto.setChatCount(5);
	    dto.setCreatedAtDisplay("2시간 전");
	    
	    dto.setLiked(true); 
		
	
		return dto;
	}
	
	// ⚠️ 더미 상태 (실제 DB 아님)
    private boolean liked = false;
    private int likeCount = 24;

    // ===============================
    // 찜 토글 로직 (더미)
    // ===============================
    public Map<String, Object> toggleLike(Long productId) {

        liked = !liked;

        if (liked) {
            likeCount++;
        } else {
            likeCount--;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("liked", liked);
        result.put("likeCount", likeCount);

        return result;
    }
}

