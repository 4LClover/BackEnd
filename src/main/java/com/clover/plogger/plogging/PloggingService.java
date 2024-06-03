package com.clover.plogger.plogging;

import com.clover.plogger.redis.RedisRankingService;
import com.clover.plogger.user.MemberRepository;
import com.clover.plogger.user.config.SecurityUtil;
import com.clover.plogger.user.domain.Member;
import com.clover.plogger.user.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PloggingService {
    private final PloggingRepository ploggingRepository;
    private  final MemberRepository memberRepository;
    private final MemberService memberService;
    private final RedisRankingService redisRankingService;

    private Member getCurrentMember() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    @Transactional
    public CloverResponseDto savePlogging(PloggingRequestDTO requestDTO) {
        Member member = getCurrentMember();
        Plogging plogging = requestDTO.toPlogging(member);
        ploggingRepository.save(plogging);

        // 점수 계산 로직
        int score = calculateClovers(requestDTO.getGoalDistance(), requestDTO.getDistance());

        int clovers;
        if ("fail".equals(requestDTO.getIsSuccessful())) {
            clovers = 0;
        } else {
            clovers = score/10;
        }
        System.out.println(clovers);
        memberService.updateClovers(clovers);

        // 클로버 수를 Redis 랭킹에 업데이트
        redisRankingService.updateUserScore(member.getNickname(), member.getClovers());

        return CloverResponseDto.builder()
                .score(score)
                .clovers(clovers)
                .build();
    }

    private int calculateClovers(Float goalDistance, Float distance) {
        if (distance >= goalDistance) {
            return (int) (50 + Math.min((distance/goalDistance), 1) * 25 + Math.min(distance*5, 25));
        } else {
            return (int) (10 + Math.min((distance/goalDistance), 1) * 25 + Math.min(distance*5, 25));
        }
    }

    public List<PloggingResponseDTO> getPloggingRecordsByMonth(Date date) {
        Member member = getCurrentMember();
        LocalDate localDate = date.toLocalDate();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();

        LocalDate localStartDate = LocalDate.of(year, month, 1);
        Date startDate = Date.valueOf(localStartDate);

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate localEndDate = yearMonth.atEndOfMonth();
        Date endDate = Date.valueOf(localEndDate);

        List<Plogging> ploggingRecords = ploggingRepository.findByMemberAndDateBetween(member, startDate, endDate);

        return ploggingRecords.stream()
                .map(PloggingResponseDTO::of)
                .collect(Collectors.toList());
    }
}