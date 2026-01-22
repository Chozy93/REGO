package com.itwillbs.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.domain.ProductVO;
import com.itwillbs.domain.user.UserVO;
import com.itwillbs.dto.MyPageDTO;
import com.itwillbs.entity.Product;

import java.util.List;


@Mapper
public interface MypageMapper {
    MyPageDTO getMyPageInfo(@Param("email") String email);
    void updateUser(@Param("userId") Long userId, @Param("vo") UserVO userVO);
    void updateAddress(@Param("userId") Long userId, @Param("address") String address);
    String getUserPassword(@Param("userId") Long userId);
    void updatePassword(@Param("userId") Long userId, @Param("encodedPassword") String encodedPassword);
    void updateProfileImg(@Param("userId") Long userId, @Param("profileImg") String profileImg);
    
    // 마이페이지 내 판매상품
    List<Product> getSalesListByUserId(@Param("userId") Long userId);
    // 마이페이지 내 구매상품
    List<Product> getPurchaseListByUserId(@Param("userId") Long userId);
	List<Product> getLikeListByUserId(@Param("userId") Long userId);
	void updatePhoneNumber(String email, String verifiedPhone);
}