package com.itwillbs.domain;

import java.time.Duration;
import java.time.LocalDateTime;

import com.itwillbs.entity.Product;

import lombok.Getter;

@Getter
public class AdminRecentProductItemVO {

    private final String productName;
    private final String imageUrl;
    private final String createdAt; // "~분 전"

    public AdminRecentProductItemVO(Product product) {
        this.productName = product.getProductName();
        this.imageUrl = product.getMainImageUrl();
        this.createdAt = toRelativeTime(product.getCreatedAt());
    }

    private String toRelativeTime(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(time, now);

        long minutes = duration.toMinutes();
        if (minutes < 60) {
            return minutes <= 0 ? "방금 전" : minutes + "분 전";
        }

        long hours = duration.toHours();
        if (hours < 24) {
            return hours + "시간 전";
        }

        long days = duration.toDays();
        if (days < 30) {
            return days + "일 전";
        }

        long months = days / 30;
        return months + "달 전";
    }
}
