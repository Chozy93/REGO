package com.itwillbs.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itwillbs.dto.MonthlyCountDTO;
import com.itwillbs.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByPhoneNumber(String phoneNumber);
    
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    long countByCreatedAtBefore(LocalDateTime time);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    // 회원 가입 추이 월별로 가져오기
    @Query("SELECT new com.itwillbs.dto.MonthlyCountDTO(MONTH(u.createdAt), COUNT(u)) " +
            "FROM User u " +
            "WHERE YEAR(u.createdAt) = YEAR(CURRENT_DATE) " + // 올해 데이터만
            "GROUP BY MONTH(u.createdAt) " +
            "ORDER BY MONTH(u.createdAt) ASC")
     List<MonthlyCountDTO> getMonthlyUserCounts();

}
