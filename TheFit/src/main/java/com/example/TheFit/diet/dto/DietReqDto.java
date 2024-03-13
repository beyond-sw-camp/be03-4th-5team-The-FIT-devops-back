package com.example.TheFit.diet.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class DietReqDto {
    private MultipartFile image;
    private String type;
    private String comment;
    private String dietDate;
}