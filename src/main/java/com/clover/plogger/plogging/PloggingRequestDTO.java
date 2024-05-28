package com.clover.plogger.plogging;

import com.clover.plogger.user.domain.Member;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Date;

@Getter
@Setter
public class PloggingRequestDTO {
    private Date date;
    private Time time;
    private Float goalDistance;
    private Float distance;
    private String imageURL;
    private String isSuccessful;

    public Plogging toPlogging(Member member) {
        return Plogging.builder()
                .member(member)
                .date(date)
                .time(time)
                .goalDistance(goalDistance)
                .distance(distance)
                .imageURL(imageURL)
                .isSuccessful(isSuccessful)
                .build();
    }
}