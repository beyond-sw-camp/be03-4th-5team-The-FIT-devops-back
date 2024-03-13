package com.example.TheFit.feedback.workoutfeedback.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkOutFeedBackResDto {
    private Long id;
    private Long workOutListId;
    private Long trainerId;
    private String trainerName;
    private String feedBack;
    private String rating;
    private String uploadDate;
    private String createdTime;
}