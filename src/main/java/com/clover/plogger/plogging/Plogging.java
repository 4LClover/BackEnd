package com.clover.plogger.plogging;

import com.clover.plogger.user.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Plogging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Time time;

    @Column(nullable = false)
    private Float distance;

    @Column(nullable = false)
    private Float goalDistance;

    @Column
    private String imageURL;

    @Column
    private String isSuccessful;

    @Builder
    public Plogging(Long id, Member member, Date date, Time time, Float distance, Float goalDistance, String imageURL, String isSuccessful) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.distance = distance;
        this.goalDistance = goalDistance;
        this.imageURL = imageURL;
        this.isSuccessful = isSuccessful;
    }
}