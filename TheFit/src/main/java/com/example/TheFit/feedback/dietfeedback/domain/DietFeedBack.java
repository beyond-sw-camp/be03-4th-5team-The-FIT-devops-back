package com.example.TheFit.feedback.dietfeedback.domain;

import com.example.TheFit.feedback.FeedBack;
import com.example.TheFit.feedback.dietfeedback.dto.DietFeedBackReqDto;
import com.example.TheFit.diet.domain.Diet;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.trainer.domain.Trainer;
import com.example.TheFit.workoutlist.domain.WorkOutList;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class DietFeedBack extends FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uploadDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    public void update(DietFeedBackReqDto dietFeedBackReqDto) {
        this.uploadDate = dietFeedBackReqDto.getUploadDate();
        this.feedBack = dietFeedBackReqDto.getFeedBack();
        this.rating = dietFeedBackReqDto.getRating();
    }

}
