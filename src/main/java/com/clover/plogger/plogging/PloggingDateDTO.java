package com.clover.plogger.plogging;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
public class PloggingDateDTO {
    private LocalDate date;
}