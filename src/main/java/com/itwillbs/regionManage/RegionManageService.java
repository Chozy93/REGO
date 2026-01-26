package com.itwillbs.regionManage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.itwillbs.domain.RegionVO;
import com.itwillbs.entity.Region;

@Service
public class RegionManageService {

    private static final String REGION_TXT_PATH = "region/region.txt";

    private final RegionRepository regionRepository;
    private final TransactionTemplate transactionTemplate;

    public RegionManageService(
            RegionRepository regionRepository,
            PlatformTransactionManager transactionManager
    ) {
        this.regionRepository = regionRepository;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    /* ==================================================
       INIT : 최초 행정구역 적재
       - region 테이블 비어있을 때만 수행
       - 단계별 확정 커밋
    ================================================== */
    public void initRegionData() {

        System.out.println("=== [REGION INIT START] ===");

        if (regionRepository.count() > 0) {
            System.out.println("[SKIP] 이미 데이터 존재");
            return;
        }

        List<RegionVO> voList = loadAndParse();
        splitAndSave(voList);

        System.out.println("=== [REGION INIT COMPLETE] ===");
    }

    
    /* ==================================================
    행정구역 UPDATE
    - 기존 데이터 존재 전제
    - 폐지 / 복구 / 명칭 변경 반영
 ================================================== */
 public void updateRegionData() {

     System.out.println("=== [REGION UPDATE START] ===");

     List<String> lines = loadRegionLines();

     /* =========================
        txt → VO 파싱
     ========================= */
     Map<String, RegionVO> voMap = new HashMap<>();

     for (String line : lines) {
         RegionVO vo = parseLine(line);
         if (vo != null) {
             voMap.put(vo.getRegionCode(), vo);
         }
     }

     System.out.println("[UPDATE] parsed VO = " + voMap.size());

     /* =========================
        DB 전체 조회
     ========================= */
     List<Region> regions = regionRepository.findAll();
     System.out.println("[UPDATE] DB region = " + regions.size());

     transactionTemplate.executeWithoutResult(status -> {

         /* =========================
            1️⃣ 기존 데이터 업데이트
         ========================= */
         for (Region region : regions) {

             RegionVO vo = voMap.get(region.getRegionCode());

             if (vo == null) {
                 // txt에서 사라진 경우 → 폐지 처리
                 if (region.isActive()) {
                     System.out.println("[DEPRECATED] " + region.getRegionCode());
                     region.updateFromVO(
                             new RegionVO(
                                     region.getRegionCode(),
                                     region.getRegionName(),
                                     null,
                                     region.getRegionLevel(),
                                     region.getRegionType(),
                                     false
                             )
                     );
                 }
                 continue;
             }

             region.updateFromVO(vo);
         }

         /* =========================
            2️⃣ 신규 지역 추가
         ========================= */
         for (RegionVO vo : voMap.values()) {

             if (regionRepository.existsById(vo.getRegionCode())) {
                 continue;
             }

             Region parent = resolveParentRegion(vo.getRegionCode());

             System.out.println("[NEW REGION] "
                     + vo.getRegionCode()
                     + " parent=" + (parent != null ? parent.getRegionCode() : "null"));

             regionRepository.save(new Region(parent, vo));
         }
     });

     System.out.println("=== [REGION UPDATE COMPLETE] ===");
 }


    /* ==================================================
       공통: txt 로딩 + 파싱
    ================================================== */
    private List<RegionVO> loadAndParse() {

        List<String> lines = loadRegionLines();
        List<RegionVO> voList = new ArrayList<>();

        int lineNo = 1;
        for (String line : lines) {
            try {
                voList.add(parseLine(line));
            } catch (Exception e) {
                throw new IllegalStateException(
                        "[PARSE ERROR] line=" + lineNo + " | " + line, e
                );
            }
            lineNo++;
        }

        System.out.println("[PARSE] count=" + voList.size());
        return voList;
    }

    /* ==================================================
       단계 분리 + 저장
    ================================================== */
    private void splitAndSave(List<RegionVO> voList) {

        List<RegionVO> sido = new ArrayList<>();
        List<RegionVO> sigungu = new ArrayList<>();
        List<RegionVO> dong = new ArrayList<>();

        for (RegionVO vo : voList) {
            if (vo.getRegionLevel() == 1) sido.add(vo);
            else if (vo.getRegionLevel() == 2) sigungu.add(vo);
            else dong.add(vo);
        }

        saveSido(sido);
        saveSigungu(sigungu);
        saveDong(dong);
    }

    /* ==================================================
       SIDO
    ================================================== */
    private void saveSido(List<RegionVO> list) {

        System.out.println("[SAVE SIDO] " + list.size());

        transactionTemplate.executeWithoutResult(status -> {
            for (RegionVO vo : list) {
                regionRepository.save(new Region(null, vo));
            }
        });
    }

    /* ==================================================
       SIGUNGU
    ================================================== */
    private void saveSigungu(List<RegionVO> list) {

        System.out.println("[SAVE SIGUNGU] " + list.size());

        transactionTemplate.executeWithoutResult(status -> {
            for (RegionVO vo : list) {
                Region parent = resolveParentRegion(vo.getRegionCode());
                regionRepository.save(new Region(parent, vo));
            }
        });
    }

    /* ==================================================
       DONG / EUP / MYEON / RI
    ================================================== */
    private void saveDong(List<RegionVO> list) {

        System.out.println("[SAVE DONG] " + list.size());

        transactionTemplate.executeWithoutResult(status -> {
            for (RegionVO vo : list) {
                Region parent = resolveParentRegion(vo.getRegionCode());
                regionRepository.save(new Region(parent, vo));
            }
        });
    }

    /* ==================================================
       부모 결정 (핵심 로직)
       - 있으면 연결
       - 없으면 null
    ================================================== */
    private Region resolveParentRegion(String regionCode) {

        // SIGUNGU
        String sigunguCode = regionCode.substring(0, 4) + "000000";
        Optional<Region> sigungu = regionRepository.findById(sigunguCode);
        if (sigungu.isPresent()) return sigungu.get();

        // SIDO
        String sidoCode = regionCode.substring(0, 2) + "00000000";
        Optional<Region> sido = regionRepository.findById(sidoCode);
        if (sido.isPresent()) return sido.get();

        // 부모 없음 (정상)
        return null;
    }

    /* ==================================================
       region.txt 로딩
    ================================================== */
    private List<String> loadRegionLines() {

        List<String> lines = new ArrayList<>();

        try {
            ClassPathResource resource =
                    new ClassPathResource(REGION_TXT_PATH);

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), "EUC-KR")
            )) {
                String line;
                boolean header = true;

                while ((line = reader.readLine()) != null) {
                    if (header) { header = false; continue; }
                    if (!line.isBlank()) lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("region.txt 읽기 실패", e);
        }

        return lines;
    }

    /* ==================================================
       한 줄 → VO
    ================================================== */
    private RegionVO parseLine(String line) {

        String[] parts = line.split("\t");
        if (parts.length < 3) {
            throw new IllegalArgumentException("컬럼 부족");
        }

        String code = parts[0].trim();
        String fullName = parts[1].trim();
        boolean active = "존재".equals(parts[2].trim());

        String name = extractRegionName(fullName);
        int level = determineRegionLevel(code);
        String type = determineRegionType(code, name);

        return new RegionVO(
                code,
                name,
                null,
                level,
                type,
                active
        );
    }

    /* ==================================================
       단계 판단
    ================================================== */
    private int determineRegionLevel(String code) {
        if (code.endsWith("00000000")) return 1;
        if (code.endsWith("000000")) return 2;
        return 3;
    }

    /* ==================================================
       타입 판단
    ================================================== */
    private String determineRegionType(String code, String name) {

        if (code.endsWith("00000000")) return "SIDO";
        if (code.endsWith("000000")) return "SIGUNGU";

        if (name.endsWith("읍")) return "EUP";
        if (name.endsWith("면")) return "MYEON";
        if (name.endsWith("리")) return "RI";
        return "DONG";
    }

    private String extractRegionName(String fullName) {
        String[] tokens = fullName.split(" ");
        return tokens[tokens.length - 1];
    }
}
