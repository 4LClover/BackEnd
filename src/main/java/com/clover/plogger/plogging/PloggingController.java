package com.clover.plogger.plogging;

import com.clover.plogger.plogging.domain.Plogging;
import com.clover.plogger.plogging.PloggingRequestDTO;
import com.clover.plogger.plogging.PloggingService;
import com.clover.plogger.user.config.SecurityUtil;
import com.clover.plogger.user.domain.Member;
import com.clover.plogger.user.MemberRepository;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;


@RestController
public class PloggingController {

    @Autowired
    private PloggingService ploggingService;

    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/post")
    public ResponseEntity<Plogging> savePlogging(@RequestBody PloggingRequestDTO ploggingRequest) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        Plogging plogging = Plogging.builder()
                .member(member)
                .date(ploggingRequest.getDate())
                .time(ploggingRequest.getTime())
                .goalDistance(ploggingRequest.getGoalDistance())
                .distance(ploggingRequest.getDistance())
                .imageURL(ploggingRequest.getImageURL())
                .isSuccessful(ploggingRequest.getIsSuccessful())
                .build();

        Plogging savedPlogging = ploggingService.savePlogging(plogging);
        return new ResponseEntity<>(savedPlogging, HttpStatus.CREATED);
    }


    @GetMapping("/calendar")
    public ResponseEntity<com.clover.plogger.plogging.dto.PloggingResponseDTO> getPloggingByDate(@RequestBody PloggingDateDTO request) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        try {
            com.clover.plogger.plogging.dto.PloggingResponseDTO response = ploggingService.getPloggingByDate(request.getDate(), member);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (java.text.ParseException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/monthly")
    public List<Plogging> getPloggingRecordsByMonth(@RequestBody PloggingDateDTO request) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        // Extract date from request DTO and convert to java.sql.Date
        Date date = Date.valueOf(request.getDate().toLocalDate());

        // Call the service method
        return ploggingService.getPloggingRecordsByMonth(date);
    }
}