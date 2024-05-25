package com.clover.plogger.user.service;

import com.clover.plogger.redis.RedisRankingService;
import com.clover.plogger.user.MemberRepository;
import com.clover.plogger.user.config.SecurityUtil;
import com.clover.plogger.user.domain.Member;
import com.clover.plogger.user.dto.MemberResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisRankingService redisRankingService;

    public MemberResponseDto getMyInfoBySecurity() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    @Transactional
    public MemberResponseDto changeMemberNickname(String nickname) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        member.setNickname(nickname);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    @Transactional
    public MemberResponseDto changeMemberPassword(String exPassword, String newPassword) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        if (!passwordEncoder.matches(exPassword, member.getPassword())) {
            throw new RuntimeException("비밀번호가 맞지 않습니다");
        }
        member.setPassword(passwordEncoder.encode((newPassword)));
        return MemberResponseDto.of(memberRepository.save(member));
    }

    private Member getCurrentMember() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    public Member updateClovers(int clovers) {
        Member member = getCurrentMember();
        member.setClovers(member.getClovers() + clovers);
        memberRepository.save(member);
        // 클로버 수를 Redis 랭킹에 업데이트
        redisRankingService.updateUserScore(member.getNickname(), clovers);
        return member;
    }

    public Set<Object> getTopUsers(int count) {
        return redisRankingService.getTopUsers(count);
    }

    public Long getUserRank() {
        Member member = getCurrentMember();
        return redisRankingService.getUserRank(member.getNickname());
    }

    public Double getUserScore() {
        Member member = getCurrentMember();
        return redisRankingService.getUserScore(member.getNickname());
    }
}
