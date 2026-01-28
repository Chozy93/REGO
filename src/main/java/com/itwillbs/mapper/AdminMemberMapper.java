package com.itwillbs.mapper;

import java.util.List;

import com.itwillbs.dto.AdminMemberListDTO;

public interface AdminMemberMapper {

    List<AdminMemberListDTO> selectAdminMemberList();
}
