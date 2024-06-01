package com.clover.plogger.user.dto;

import com.clover.plogger.user.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingDto {
    private String nickname;
    private int rank;
    private int clovers;
}
