package com.itwillbs.view;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SellerProfilePageViewVO {

    private final SellerProfileViewVO profile;
    private final List<SellerReviewViewVO> reviews;

    public SellerProfilePageViewVO(
            SellerProfileViewVO profile,
            List<SellerReviewViewVO> reviews
    ) {
        this.profile = profile;
        this.reviews = reviews;
    }
}
