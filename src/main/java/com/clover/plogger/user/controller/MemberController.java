package com.clover.plogger.user.controller;

import com.clover.plogger.user.domain.Member;
import com.clover.plogger.user.dto.ChangePasswordRequestDto;
import com.clover.plogger.user.dto.MemberRequestDto;
import com.clover.plogger.user.dto.MemberResponseDto;
import com.clover.plogger.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyMemberInfo() {
        MemberResponseDto myInfoBySecurity = memberService.getMyInfoBySecurity();
        return ResponseEntity.ok((myInfoBySecurity));
    }

    @PostMapping("/nickname")
    public ResponseEntity<MemberResponseDto> setMemberNickname(@RequestBody MemberRequestDto request) {
        return ResponseEntity.ok(memberService.changeMemberNickname(request.getNickname()));
    }

    @PostMapping("/password")
    public ResponseEntity<MemberResponseDto> setMemberPassword(@RequestBody ChangePasswordRequestDto request) {
        return ResponseEntity.ok(memberService.changeMemberPassword(request.getExPassword(), request.getNewPassword()));
    }

    @PutMapping("/clovers")
    public ResponseEntity<MemberResponseDto> updateClovers(@RequestParam int clovers) {
        Member updatedMember = memberService.updateClovers(clovers);
        MemberResponseDto responseDto = MemberResponseDto.of(updatedMember);
        return ResponseEntity.ok(responseDto);
    }

    // 사용자 랭킹 조회
    @GetMapping("/rank")
    public ResponseEntity<Long> getUserRank() {
        Long rank = memberService.getUserRank();
        return ResponseEntity.ok(rank);
    }

    // 상위 사용자 조회
    @GetMapping("/top")
    public ResponseEntity<Set<Object>> getTopUsers(@RequestParam int count) {
        Set<Object> topUsers = memberService.getTopUsers(count);
        return ResponseEntity.ok(topUsers);
    }

    // 사용자 점수 조회
    @GetMapping("/score")
    public ResponseEntity<Double> getUserScore() {
        Double score = memberService.getUserScore();
        return ResponseEntity.ok(score);
    }

}
