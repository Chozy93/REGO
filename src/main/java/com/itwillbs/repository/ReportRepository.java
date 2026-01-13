package com.itwillbs.repository;

import com.itwillbs.entity.Report;
import com.itwillbs.entity.User;
import com.itwillbs.entity.enumtype.ReportTargetType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

	boolean existsByTargetTypeAndTargetIdAndReporter_UserId(
		    ReportTargetType targetType,
		    Long targetId,
		    Long userId
		);

}
