package com.clover.plogger.plogging;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class PloggingController {
    private final PloggingService ploggingService;

    @PostMapping("/post")
    public ResponseEntity<CloverResponseDto> savePlogging(@RequestBody PloggingRequestDTO requestDTO) {
        return ResponseEntity.ok(ploggingService.savePlogging(requestDTO));
    }

    @PostMapping("/monthly")
    public ResponseEntity<List<PloggingResponseDTO>> getPloggingRecordsByMonth(@RequestBody PloggingDateDTO requestDTO) {
        try {
            Date date = Date.valueOf(requestDTO.getDate());
            List<PloggingResponseDTO> records = ploggingService.getPloggingRecordsByMonth(date);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}