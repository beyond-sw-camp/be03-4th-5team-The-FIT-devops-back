package com.example.TheFit.feedback.workoutfeedback.domain;

import com.example.TheFit.feedback.FeedBack;
import com.example.TheFit.feedback.workoutfeedback.dto.WorkOutFeedBackReqDto;
import com.example.TheFit.user.trainer.domain.Trainer;
import com.example.TheFit.workoutlist.domain.WorkOutList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class WorkOutFeedBack extends FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uploadDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workoutList_id")
    private WorkOutList workOutList;
    public void update(WorkOutFeedBackReqDto workOutFeedBackReqDto) {
        this.feedBack = workOutFeedBackReqDto.getFeedBack();
        this.rating = workOutFeedBackReqDto.getRating();
        this.uploadDate = workOutFeedBackReqDto.getUploadDate();
    }
}
