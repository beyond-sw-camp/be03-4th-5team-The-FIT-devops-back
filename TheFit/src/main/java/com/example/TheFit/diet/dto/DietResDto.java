package com.example.TheFit.diet.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietResDto {
    private Long id;
    private Long memberId;
    private String imagePath;
    private String type;
    private String comment;
    private LocalDate dietDate;
}