package com.itwillbs.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.domain.AdminProductSearchConditionVO;
import com.itwillbs.dto.AdminProductListDTO;

@Mapper
public interface AdminProductMapper {

    List<AdminProductListDTO> selectAdminProductListByCondition(
            AdminProductSearchConditionVO condition
    );

    int countAdminProductByCondition(
            AdminProductSearchConditionVO condition
    );

    int countBySalesStatus(String salesStatus);
}
