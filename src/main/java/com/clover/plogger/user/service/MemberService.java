package com.clover.plogger.user.service;

import com.clover.plogger.redis.RedisRankingService;
import com.clover.plogger.user.MemberRepository;
import com.clover.plogger.user.config.SecurityUtil;
import com.clover.plogger.user.domain.Member;
import com.clover.plogger.user.dto.MemberResponseDto;
import com.clover.plogger.user.dto.RankingDto;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisRankingService redisRankingService;

    @PostConstruct
    public void init() {
        redisRankingService.clearRanking();
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            try {
                redisRankingService.addUserScore(member.getNickname(), member.getClovers());
            } catch (Exception e) {
                System.err.println("Redis 등록 실패 for member: " + member.getNickname());
                e.printStackTrace();
            }
        }
    }

    public MemberResponseDto getMyInfoBySecurity() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    @Transactional
    public MemberResponseDto changeMemberNickname(String nickname) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        String oldNickname = member.getNickname();
        member.setNickname(nickname);
        Member updatedMember = memberRepository.save(member);

        // Update Redis ranking
        redisRankingService.removeUserScore(oldNickname);
        redisRankingService.addUserScore(updatedMember.getNickname(), updatedMember.getClovers());

        return MemberResponseDto.of(updatedMember);
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

    @Transactional
    public Member updateClovers(int clovers) {
        Member member = getCurrentMember();
        member.setClovers(member.getClovers() + clovers);
        memberRepository.save(member);
        // 클로버 수를 Redis 랭킹에 업데이트
        redisRankingService.updateUserScore(member.getNickname(), clovers);
        return member;
    }

    public RankingDto getUserRank() {
        Member member = getCurrentMember();
        Long rank = redisRankingService.getUserRank(member.getNickname());
        return RankingDto.builder()
                .nickname(member.getNickname())
                .clovers(member.getClovers())
                .rank(rank != null ? rank.intValue() + 1 : -1) // 랭킹이 없는 경우 -1로 설정
                .build();
    }

    public List<RankingDto> getTopUsers(int count) {
        Set<ZSetOperations.TypedTuple<String>> topUsersWithScores = redisRankingService.getTopUsersWithScores(count);
        List<RankingDto> topUsers = new ArrayList<>();

        int currentRank = 1;
        int displayRank = 1;
        Double lastScore = null;

        for (ZSetOperations.TypedTuple<String> tuple : topUsersWithScores) {
            String nickname = tuple.getValue();
            Double scoreObj = tuple.getScore();
            int score = scoreObj != null ? scoreObj.intValue() : 0;

            if (lastScore != null && !scoreObj.equals(lastScore)) {
                displayRank = currentRank;
            }

            topUsers.add(
                    RankingDto.builder()
                            .nickname(nickname)
                            .clovers(score)
                            .rank(displayRank)
                            .build()
            );

            lastScore = scoreObj;
            currentRank++;
        }

        return topUsers;
    }
}
