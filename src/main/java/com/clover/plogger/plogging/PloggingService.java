package com.clover.plogger.plogging;

import com.clover.plogger.plogging.domain.Plogging;
import com.clover.plogger.user.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
public class PloggingService {

    @Autowired
    private PloggingRepository ploggingRepository;

    public Plogging savePlogging(Plogging plogging) {
        return ploggingRepository.save(plogging);
    }

    public com.clover.plogger.plogging.dto.PloggingResponseDTO getPloggingByDate(Date date, Member member) throws ParseException {
        Optional<Plogging> ploggingOptional = ploggingRepository.findByDateAndMember(date, member);

        if (ploggingOptional.isPresent()) {
            Plogging plogging = ploggingOptional.get();
            return com.clover.plogger.plogging.dto.PloggingResponseDTO.builder()
                    .member(member)
                    .date(date)
                    .time(plogging.getTime())
                    .distance(plogging.getDistance())
                    .goalDistance(plogging.getGoalDistance())
                    .image(plogging.getImageURL())
                    .build();
        } else {
            throw new IllegalArgumentException("No plogging record found for the given date");
        }
    }

    public List<Plogging> getPloggingRecordsByMonth(Date date) {
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