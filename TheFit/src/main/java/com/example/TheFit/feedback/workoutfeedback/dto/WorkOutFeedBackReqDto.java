package com.example.TheFit.feedback.workoutfeedback.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkOutFeedBackReqDto {
    private String feedBack;
    private String rating;
    private String uploadDate;
    private String memberEmail;
}