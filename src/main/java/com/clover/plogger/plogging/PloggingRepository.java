package com.clover.plogger.plogging;

import com.clover.plogger.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface PloggingRepository extends JpaRepository<Plogging, Long> {
    List<Plogging> findByMemberAndDateBetween(Member member, Date startDate, Date endDate);
}