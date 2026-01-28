package com.itwillbs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.domain.AdminProductListPageVO;
import com.itwillbs.domain.AdminProductSearchConditionVO;
import com.itwillbs.dto.AdminProductListDTO;
import com.itwillbs.dto.AdminProductStatusCountDTO;
import com.itwillbs.mapper.AdminProductMapper;

import lombok.RequiredArgsConstructor;


@Service
public class AdminProductService {

    private final AdminProductMapper productMapper;

    public AdminProductService(AdminProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public AdminProductListPageVO getAdminProductList(
            AdminProductSearchConditionVO condition) {

        int totalCount =
                productMapper.countAdminProductByCondition(condition);

        int onSaleCount =
                productMapper.countBySalesStatus("ON_SALE");

        int reservedCount =
                productMapper.countBySalesStatus("RESERVED");

        int soldCount =
                productMapper.countBySalesStatus("SOLD");

        return new AdminProductListPageVO(
                productMapper.selectAdminProductListByCondition(condition),
                totalCount,
                condition.getPage(),
                condition.getPageSize(),
                onSaleCount,
                reservedCount,
                soldCount
        );
    }
}
