package com.itwillbs.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.NotificationSettings;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationSettings, Long> {

}