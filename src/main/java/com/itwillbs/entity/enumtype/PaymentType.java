package com.itwillbs.entity.enumtype;

public enum PaymentType {
	REPAY("Repay결제"), 
	CARD("아임포트 결제");
	
    private final String label;

	PaymentType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
