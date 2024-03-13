package com.example.TheFit.feedback.dietfeedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietFeedBackResDto {
    private Long id;
    private Long trainerId;
    private String trainerName;
    private String feedBack;
    private String rating;
    private String uploadDate;
    private String createdTime;
}