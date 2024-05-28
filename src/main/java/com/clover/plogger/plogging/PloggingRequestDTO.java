package com.clover.plogger.plogging;

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
}