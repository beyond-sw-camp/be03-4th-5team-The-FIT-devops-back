package com.example.TheFit.feedback.dietfeedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietFeedBackReqDto {
    private String feedBack;
    private String rating;
    private String uploadDate;
    private String memberEmail;
}