package com.example.TheFit.career.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerReqDto {
    private Long trainerId;
    private String awards;
    private String license;
    private String work;
}