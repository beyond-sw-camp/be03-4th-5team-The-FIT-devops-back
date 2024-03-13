package com.example.TheFit.workout.dto;

import com.example.TheFit.workout.domain.WorkOutStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkOutResDto {
    private Long id;
    private Long workOutListId;
    private Long totalWorkOutsId;
    private int sets;
    private int weight;
    private int reps;
    private String restTime;
    private int performance;
    private WorkOutStatus workOutStatus;
}