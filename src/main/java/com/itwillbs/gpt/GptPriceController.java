package com.itwillbs.gpt;

import com.itwillbs.gpt.GptPriceRecommendRequestDTO;
import com.itwillbs.gpt.GptPriceRecommendResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gpt")
public class GptPriceController {

    private final GptPriceService gptPriceService;

    @PostMapping("/price-recommend")
    public GptPriceRecommendResponseDTO recommendPrice(
            @RequestBody GptPriceRecommendRequestDTO request
    ) {
        return gptPriceService.recommendPrice(request);
    }
}