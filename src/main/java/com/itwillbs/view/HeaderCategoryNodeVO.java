package com.itwillbs.view;

import java.util.List;

import com.itwillbs.domain.CategoryVO;

import lombok.Getter;

//view
@Getter
public class HeaderCategoryNodeVO {

 private final CategoryVO parent;
 private final List<CategoryVO> children;

 public HeaderCategoryNodeVO(CategoryVO parent, List<CategoryVO> children) {
     this.parent = parent;
     this.children = children;
 }
}
