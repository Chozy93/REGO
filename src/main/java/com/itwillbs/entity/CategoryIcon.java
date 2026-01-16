package com.itwillbs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "category_icon")
@Getter
public class CategoryIcon {

    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "icon_code", nullable = false)
    private String iconCode;

    protected CategoryIcon() {
    }

    public CategoryIcon(Long categoryId, String iconCode) {
        this.categoryId = categoryId;
        this.iconCode = iconCode;
    }
}
