package com.clover.plogger.plogging;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class PloggingController {
    private final PloggingService ploggingService;

    @PostMapping("/post")
    public ResponseEntity<PloggingResponseDTO> savePlogging(@RequestBody PloggingRequestDTO requestDTO) {
        return ResponseEntity.ok(ploggingService.savePlogging(requestDTO));
    }

    @GetMapping("/monthly")
    public List<PloggingResponseDTO> getPloggingRecordsByMonth(@RequestBody PloggingDateDTO requestDTO) {
        Date date = Date.valueOf(requestDTO.getDate().toLocalDate());
        return ploggingService.getPloggingRecordsByMonth(date);
    }
}