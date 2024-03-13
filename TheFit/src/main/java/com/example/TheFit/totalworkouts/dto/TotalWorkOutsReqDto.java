package com.example.TheFit.totalworkouts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotalWorkOutsReqDto {
    @NotNull(message = "운동 이름을 입력해주세요")
    private String name;
    @NotNull(message = "운동 부위을 입력해주세요")
    private String target;
}