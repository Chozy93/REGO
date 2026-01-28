package com.itwillbs.domain;

public class AdminInquirySummaryVO {

    private final int total;
    private final int waiting;
    private final int done;

    public AdminInquirySummaryVO(int total, int waiting, int done) {
        this.total = total;
        this.waiting = waiting;
        this.done = done;
    }

    public int getTotal() { return total; }
    public int getWaiting() { return waiting; }
    public int getDone() { return done; }
}
