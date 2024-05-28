package com.clover.plogger.plogging;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class PloggingDateDTO {
    private Date date; // 요청에서 날짜를 String 타입으로 받습니다.
}