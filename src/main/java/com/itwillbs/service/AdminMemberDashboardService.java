package com.itwillbs.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.itwillbs.domain.AdminMemberSummaryVO;
import com.itwillbs.dto.AdminMemberSummaryDTO;
import com.itwillbs.repository.UserRepository;

@Service
public class AdminMemberDashboardService {

    private final UserRepository userRepository;

    public AdminMemberDashboardService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AdminMemberSummaryVO getMemberSummary() {

        LocalDate today = LocalDate.now();

        // 총 회원 수
        long totalCount = userRepository.count();

        // 지난달 기준
        LocalDateTime lastMonthStart =
            today.minusMonths(1).atStartOfDay();
        long lastMonthCount =
            userRepository.countByCreatedAtBefore(lastMonthStart);

        // 오늘 / 어제 가입자
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime tomorrowStart = today.plusDays(1).atStartOfDay();
        LocalDateTime yesterdayStart = today.minusDays(1).atStartOfDay();

        long todaySignup =
            userRepository.countByCreatedAtBetween(todayStart, tomorrowStart);

        long yesterdaySignup =
            userRepository.countByCreatedAtBetween(yesterdayStart, todayStart);

        AdminMemberSummaryDTO dto = new AdminMemberSummaryDTO(
            totalCount,
            lastMonthCount,
            todaySignup,
            yesterdaySignup
        );

        return new AdminMemberSummaryVO(dto);
    }
}

