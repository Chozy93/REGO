package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.entity.UserAddress;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserAddressVO {

    private Long addressId;
    private Long userId;

    private String addressName;
    private String receiverName;
    private String receiverPhone;
    private String zipCode;
    private String address;
    private String addressDetail;
    private boolean isDefault;

    private LocalDateTime lastUsedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /* =========================
       Entity → VO 생성자
    ========================= */
    public UserAddressVO(UserAddress entity) {
        this.addressId = entity.getAddressId();
        this.userId = entity.getUser().getUserId();
        this.addressName = entity.getAddressName();
        this.receiverName = entity.getReceiverName();
        this.receiverPhone = entity.getReceiverPhone();
        this.zipCode = entity.getZipCode();
        this.address = entity.getAddress();
        this.addressDetail = entity.getAddressDetail();
        this.isDefault = entity.isDefault();
        this.lastUsedAt = entity.getLastUsedAt();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
