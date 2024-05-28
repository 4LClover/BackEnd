package com.clover.plogger.plogging;

import com.clover.plogger.user.MemberRepository;
import com.clover.plogger.user.config.SecurityUtil;
import com.clover.plogger.user.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class PloggingService {
    private final PloggingRepository ploggingRepository;
    private  final MemberRepository memberRepository;

    private Member getCurrentMember() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    @Transactional
    public PloggingResponseDTO savePlogging(PloggingRequestDTO requestDTO) {
        Member member = getCurrentMember();
        Plogging plogging = requestDTO.toPlogging(member);
        ploggingRepository.save(plogging);
        return PloggingResponseDTO.of(plogging);
    }

    public List<PloggingResponseDTO> getPloggingRecordsByMonth(Date date) {
        // 전달받은 Date 객체를 LocalDate로 변환
        LocalDate localDate = date.toLocalDate();

        // 연도와 월을 추출
        int year = localDate.getYear();
        int month = localDate.getMonthValue();

        // 지정된 연도와 월의 첫째 날을 나타내는 LocalDate 객체 생성
        LocalDate localStartDate = LocalDate.of(year, month, 1);
        Date startDate = Date.valueOf(localStartDate);

        // 해당 월의 마지막 날을 찾기 위해 YearMonth 객체 사용
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate localEndDate = yearMonth.atEndOfMonth();
        Date endDate = Date.valueOf(localEndDate);

        return ploggingRepository.findByDateBetween(startDate, endDate);
    }
}