package com.clover.plogger.plogging;

import com.clover.plogger.plogging.domain.Plogging;
import com.clover.plogger.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PloggingRepository extends JpaRepository<Plogging, Long> {
    Optional<Plogging> findByDateAndMember(Date date, Member member); // 특정 날짜의 플로깅 기록을 조회하는 메서드
    List<Plogging> findByDateBetween(Date startDate, Date endDate);
}