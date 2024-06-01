package com.clover.plogger.plogging;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface PloggingRepository extends JpaRepository<Plogging, Long> {
    List<PloggingResponseDTO> findByDateBetween(Date startDate, Date endDate);
}