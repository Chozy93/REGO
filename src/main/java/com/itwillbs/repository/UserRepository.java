package com.itwillbs.repository;

import com.itwillbs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByPhoneNumber(String phoneNumber);
    
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    long countByCreatedAtBefore(LocalDateTime time);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}
