package com.itwillbs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.domain.CategoryVO;
import com.itwillbs.entity.Category;
import com.itwillbs.entity.CategoryIcon;
import com.itwillbs.repository.CategoryRepository;
import com.itwillbs.repository.CategoryIconRepository;
import com.itwillbs.view.HeaderCategoryListVO;
import com.itwillbs.view.HeaderCategoryNodeVO;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryIconRepository categoryIconRepository;
    /* =========================
    헤더 카테고리 조회 (UI 전용)
  ========================= */
    @Transactional(readOnly = true)
    public HeaderCategoryListVO getHeaderCategories() {

        System.out.println("========== HEADER CATEGORY DEBUG START ==========");

        // 1. 부모
        List<Category> parents =
                categoryRepository.findByLevelAndIsActiveOrderBySortOrderAsc(1, true);

        System.out.println("[PARENTS] size = " + parents.size());
        for (Category p : parents) {
            System.out.println("  parentId=" + p.getCategoryId()
                    + ", name=" + p.getName()
                    + ", level=" + p.getLevel());
        }

        // 2. 자식 전체 조회
        List<Category> children =
                categoryRepository.findByLevelAndIsActiveOrderBySortOrderAsc(2, true);

        System.out.println("[CHILDREN] size = " + children.size());

        for (Category c : children) {
            System.out.println("  childId=" + c.getCategoryId()
                    + ", name=" + c.getName()
                    + ", level=" + c.getLevel()
                    + ", parent=" + (c.getParent() == null ? "NULL" : c.getParent().getCategoryId()));
        }

        // 3. parentId 기준 그룹핑
        Map<Long, List<CategoryVO>> childrenMap =
                children.stream()
                        .filter(c -> {
                            if (c.getParent() == null) {
                                System.out.println("❌ [DROP] childId=" + c.getCategoryId() + " parent is NULL");
                                return false;
                            }
                            return true;
                        })
                        .collect(Collectors.groupingBy(
                                c -> c.getParent().getCategoryId(),
                                Collectors.mapping(Category::toVO, Collectors.toList())
                        ));

        System.out.println("[CHILD MAP KEYS]");
        for (Long key : childrenMap.keySet()) {
            System.out.println("  parentId=" + key + ", childCount=" + childrenMap.get(key).size());
        }

        // 4. 아이콘
        Map<Long, String> iconMap =
                categoryIconRepository.findAll().stream()
                        .collect(Collectors.toMap(
                                CategoryIcon::getCategoryId,
                                CategoryIcon::getIconCode
                        ));

        // 5. 조립
        List<HeaderCategoryNodeVO> nodes = new ArrayList<>();

        for (Category parent : parents) {

            List<CategoryVO> childList =
                    childrenMap.getOrDefault(parent.getCategoryId(), List.of());

            System.out.println("[ASSEMBLE] parentId=" + parent.getCategoryId()
                    + ", children=" + childList.size());

            nodes.add(new HeaderCategoryNodeVO(
                    parent.toVO(),
                    iconMap.getOrDefault(parent.getCategoryId(), "category"),
                    childList
            ));
        }

        System.out.println("========== HEADER CATEGORY DEBUG END ==========");

        return new HeaderCategoryListVO(nodes);
    }



}
