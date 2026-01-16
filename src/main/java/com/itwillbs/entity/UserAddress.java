package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.UserAddressVO;

@Entity
@Table(name = "user_address")
@Getter
public class UserAddress {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    /* =========================
       사용자 (N:1)
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_user_address_user")
    )
    private User user;

    /* =========================
       배송지 정보
    ========================= */
    @Column(name = "address_name", length = 50, nullable = false)
    private String addressName;

    @Column(name = "receiver_name", length = 50, nullable = false)
    private String receiverName;

    @Column(name = "receiver_phone", length = 20, nullable = false)
    private String receiverPhone;

    @Column(name = "zip_code", length = 10, nullable = false)
    private String zipCode;

    @Column(name = "address", length = 255, nullable = false)
    private String address;

    @Column(name = "address_detail", length = 255)
    private String addressDetail;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    /* =========================
       날짜 정보
    ========================= */
    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected UserAddress() {}

    /* =========================
       생성자 (VO → Entity)
    ========================= */
    public UserAddress(User user, UserAddressVO vo) {
        this.user = user;
        this.addressName = vo.getAddressName();
        this.receiverName = vo.getReceiverName();
        this.receiverPhone = vo.getReceiverPhone();
        this.zipCode = vo.getZipCode();
        this.address = vo.getAddress();
        this.addressDetail = vo.getAddressDetail();
        this.isDefault = vo.isDefault();
        this.lastUsedAt = vo.getLastUsedAt();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /* =========================
       Entity → VO
    ========================= */
    public UserAddressVO toVO() {
        return new UserAddressVO(this);
    }

    /* =========================
       상태 변경
    ========================= */
    public void markAsUsed() {
        this.lastUsedAt = LocalDateTime.now();
    }

    public void updateDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
