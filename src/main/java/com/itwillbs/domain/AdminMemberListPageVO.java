package com.itwillbs.domain;

import java.util.List;

public class AdminMemberListPageVO {

    private final List<AdminMemberListItemVO> members;
    private final int totalMemberCount;

    public AdminMemberListPageVO(
        List<AdminMemberListItemVO> members,
        int totalMemberCount
    ) {
        this.members = members;
        this.totalMemberCount = totalMemberCount;
    }

    public List<AdminMemberListItemVO> getMembers() {
        return members;
    }

    public int getTotalMemberCount() {
        return totalMemberCount;
    }
}
