package com.itwillbs.entity;

import com.itwillbs.domain.CategoryVO;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "categories")
@Getter
public class Category {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    /* =========================
       부모 카테고리 (자기참조)
       - 대분류(level=1)는 NULL
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "parent_id",
        nullable = true,
        foreignKey = @ForeignKey(name = "fk_categories_parent")
    )
    private Category parent;

    /* =========================
       카테고리 단계
       1 = 대분류
       2 = 소분류
    ========================= */
    @Column(name = "level", nullable = false)
    private Integer level;

    /* =========================
       카테고리 이름
    ========================= */
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    /* =========================
       정렬 순서
    ========================= */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    /* =========================
       사용 여부
    ========================= */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected Category() {}
    
    
    public Category(Category parent, CategoryVO vo) {
        this.parent = parent;
        this.level = vo.getLevel();
        this.name = vo.getName();
        this.sortOrder = vo.getSortOrder();
        this.isActive = vo.getIsActive();
    }
    
    public CategoryVO toVO() {
        return new CategoryVO(this);
    }
}
