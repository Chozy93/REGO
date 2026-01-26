package com.itwillbs.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
	/**
     * 주문 생성 및 생성된 order_id를 Map의 keyProperty에 담아 반환
     */
    void insertOrder(Map<String, Object> params);
}
