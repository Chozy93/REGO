package com.itwillbs.entity.enumtype;

public enum DealType {
	DIRECT("직거래"),     // 직거래
    DELIVERY("택배");    // 택배
	  private final String label;

	  DealType(String label) {
	        this.label = label;
	    }

	    public String getLabel() {
	        return label;
	    }
}
