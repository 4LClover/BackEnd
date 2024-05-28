package com.clover.plogger.plogging;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Date;

@Getter
@Setter
@Builder
public class PloggingResponseDTO {
    private Date date;
    private Time time;
    private Float distance;
    private Float goalDistance;
    private String imageURL;

    public static PloggingResponseDTO of(Plogging plogging) {
        return PloggingResponseDTO.builder()
                .date(plogging.getDate())
                .time(plogging.getTime())
                .distance(plogging.getDistance())
                .goalDistance(plogging.getGoalDistance())
                .imageURL((plogging.getImageURL()))
                .build();
    }
}