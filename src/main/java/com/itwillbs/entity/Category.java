package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import com.itwillbs.domain.CategoryVO;

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
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "parent_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_categories_parent")
    )
    private Category parent;

    /* =========================
       카테고리 이름
    ========================= */
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected Category() {}

    /* =========================
       생성자 (VO → Entity)
    ========================= */
    public Category(Category parent, CategoryVO vo) {
        this.parent = parent;
        this.name = vo.getName();
    }

    /* =========================
       Entity → VO
    ========================= */
    public CategoryVO toVO() {
        return new CategoryVO(this);
    }
}
