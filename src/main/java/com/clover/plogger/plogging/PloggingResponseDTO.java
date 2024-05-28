package com.clover.plogger.plogging.dto;

import com.clover.plogger.user.domain.Member;
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
    private Member member;
    private Time time;
    private Float distance;
    private Float goalDistance;
    private String image;
}