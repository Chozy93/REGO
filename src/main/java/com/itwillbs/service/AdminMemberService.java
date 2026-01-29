package com.itwillbs.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.domain.AdminMemberListItemVO;
import com.itwillbs.domain.AdminMemberListPageVO;
import com.itwillbs.mapper.AdminMemberMapper;

@Service
public class AdminMemberService {

    private final AdminMemberMapper memberMapper;

    public AdminMemberService(AdminMemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public AdminMemberListPageVO getAdminMemberList() {

        List<AdminMemberListItemVO> members =
            memberMapper.selectAdminMemberList()
                .stream()
                .map(AdminMemberListItemVO::new)
                .collect(Collectors.toList());

        int totalMemberCount = members.size(); // ✅ 총 회원수

        return new AdminMemberListPageVO(members, totalMemberCount);
    }
}
