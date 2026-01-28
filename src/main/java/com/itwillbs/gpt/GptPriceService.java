package com.itwillbs.gpt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GptPriceService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public GptPriceRecommendResponseDTO recommendPrice(GptPriceRecommendRequestDTO req) {

        try {
            boolean isNew = isNewCondition(req.getConditionStatus());
            String userPrompt = buildPrompt(req, isNew);

            Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                    Map.of(
                        "role", "system",
                        "content",
                        """
                        너는 한국 중고 거래 플랫폼의 가격 추천 AI다.
                        - 단일 가격이 아니라 "가격 범위"를 제시한다.
                        - 결과는 반드시 JSON 형식으로만 응답한다.
                        - JSON 외 문장은 절대 포함하지 않는다.
                        """
                    ),
                    Map.of("role", "user", "content", userPrompt)
                ),
                "temperature", 0.4
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions",
                request,
                String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode message = root.path("choices").get(0).path("message");

            if (message.isMissingNode()) {
                return buildFailResponse();
            }

            String content = message.path("content").asText();
            JsonNode json = objectMapper.readTree(content);

            if (json.get("minPrice") == null || json.get("maxPrice") == null) {
                return buildFailResponse();
            }

            int min = json.get("minPrice").asInt();
            int max = json.get("maxPrice").asInt();
            String reason = (json.get("reason") != null) ? json.get("reason").asText() : "";

            // ✅ 서버 안전장치 (NEW/중고 공통)
            int[] normalized = normalizeRange(min, max);

            // ✅ 중고는 상태 감가를 "미세 보정" 정도로만(과보정 방지)
            // NEW는 "다른 시장"이라 서버 배율 보정 금지
            if (!isNew) {
                normalized = adjustUsedByCondition(normalized[0], normalized[1], req.getConditionStatus());
            }

            return new GptPriceRecommendResponseDTO(
                normalized[0],
                normalized[1],
                reason
            );

        } catch (Exception e) {
            return buildFailResponse();
        }
    }

    /* =========================
       NEW 판단 (미사용/미개봉)
    ========================= */
    private boolean isNewCondition(String conditionStatus) {
        return "NEW".equals(conditionStatus);
    }

    /* =========================
       (중고) 상태별 미세 보정
       - GPT가 이미 상태를 반영하므로 과보정 금지
       - GOOD/FAIR만 살짝 내려서 안전하게
    ========================= */
    private int[] adjustUsedByCondition(int min, int max, String conditionStatus) {

        double factor = switch (conditionStatus) {
            case "LIKE_NEW" -> 1.00;
            case "GOOD"     -> 0.97;
            case "FAIR"     -> 0.93;
            default         -> 1.00;
        };

        int adjMin = (int) Math.round(min * factor);
        int adjMax = (int) Math.round(max * factor);

        return normalizeRange(adjMin, adjMax);
    }

    /* =========================
       결과 정규화(서버 안전장치)
       1) min/max 순서 보정
       2) 음수/0 방지
       3) 천원 단위 정규화
       4) 너무 벌어진 범위 컷(상식선)
    ========================= */
    private int[] normalizeRange(int min, int max) {

        // 값이 이상하면 즉시 실패 처리 레벨로 반환(프론트가 분기)
        if (min <= 0 || max <= 0) {
            return new int[]{0, 0};
        }

        int a = Math.min(min, max);
        int b = Math.max(min, max);

        // 천원 단위로 정규화 (네 UX 기준)
        a = roundToThousand(a);
        b = roundToThousand(b);

        // 범위가 너무 넓으면(예: 10만~200만 같은) 상식선 컷
        // (완전 강제 컷이 아니라 "너무 벌어진 값"을 안정화)
        // b가 a의 2.0배 넘으면 상한을 2.0배로 제한
        if (a > 0 && (double) b / (double) a > 2.0) {
            b = roundToThousand(a * 2);
        }

        return new int[]{a, b};
    }

    private int roundToThousand(int value) {
        return (int) (Math.round(value / 1000.0) * 1000);
    }

    /* =========================
       명시적 실패 응답
    ========================= */
    private GptPriceRecommendResponseDTO buildFailResponse() {
        return new GptPriceRecommendResponseDTO(
            0,
            0,
            "AI 가격 추천을 불러오지 못했습니다. 네트워크 상태를 확인해 주세요."
        );
    }

    /* =========================
       Prompt (NEW / USED 분기)
       - NEW: 리셀/미개봉 시장 변동 반영 + 평균 대비 ±3% 가드레일
       - USED: 표준편차(±1.2σ) 기반으로 outlier 제거 명시
    ========================= */
    private String buildPrompt(GptPriceRecommendRequestDTO req, boolean isNew) {

        String conditionGuide;

        if (isNew) {
            conditionGuide = """
            [NEW(미사용/미개봉) 판단 규칙]
            - 본 상품은 미사용/미개봉에 가까운 상태다.
            - 일반 중고 평균가 기준이 아니라, 현재 중고/리셀 시장의 변동성과 거래 관행을 반영한다.
            - 보수적으로 "유사 상품 중고 시세"를 기준으로 하되, 통상적인 프리미엄 범위 내에서 상향을 허용한다.
            - 단, 평균 중고가 대비 과도한 프리미엄은 피한다.
            - 평균 중고가 대비 상향 폭은 일반적으로 ±3% 내외를 기본 가드레일로 삼되,
              실제 시장 변동이 크다고 판단되면 이유를 간단히 적고 합리적인 범위를 제시한다.
            - 가격 분포 관점에서 극단값(outlier)을 제외하기 위해 표준편차 기준 ±1σ 범위를 참고한다.
            """;
        } else {
            conditionGuide = """
            [USED(중고) 판단 규칙]
            - 유사 상품의 실제 중고 거래 시세를 가정한다.
            - 상품 상태(LIKE_NEW/GOOD/FAIR)에 따라 감가를 반영한다.
            - 가격 분포에서 극단적으로 높거나 낮은 값은 실제 거래 가능성이 낮으므로 제외한다.
            - 이를 명확히 하기 위해 표준편차 기준 ±1.2σ 범위를 벗어나는 outlier는 컷하고,
              중앙에 몰리는 현실적인 거래 가능 구간으로 min/max를 잡는다.
            """;
        }

        return """
        다음 상품의 중고 거래 적정 가격 범위를 추천해라.

        [상품 정보]
        - 상품명: %s
        - 상세 설명: %s
        - 상품 상태: %s
        - 카테고리: %s

        %s

        [출력 조건]
        - 가격은 원 단위 정수로만 제시한다
        - 최소가와 최대가의 차이는 과도하게 벌어지지 않게 한다
        - 반드시 JSON 형식으로만 응답한다 (JSON 외 문장 금지)

        {
          "minPrice": 숫자,
          "maxPrice": 숫자,
          "reason": "왜 이 범위가 합리적인지 1~2문장으로 간단히"
        }
        """.formatted(
            safe(req.getTitle()),
            safe(req.getDescription()),
            safe(req.getConditionStatus()),
            safe(req.getCategoryName()),
            conditionGuide
        );
    }

    private String safe(String s) {
        return (s == null) ? "" : s.trim();
    }
}
