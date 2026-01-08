package com.itwillbs.view.condition;

public class MainProductSortConditionVO {

    private final String sort;

    public MainProductSortConditionVO(String sort) {
        this.sort = (sort == null || sort.isBlank()) ? "recent" : sort;
    }

    public boolean isRecent() {
        return "recent".equals(sort);
    }

    public boolean isLike() {
        return "like".equals(sort);
    }

    public boolean isView() {
        return "view".equals(sort);
    }

    public String getSort() {
        return sort;
    }
}
