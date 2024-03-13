package com.example.TheFit.workout.dto;

import com.example.TheFit.workout.domain.WorkOutStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkOutUsingMemberResDto {
    private Long id;
    private LocalDate workOutDate;
    private Long workOutListId;
    private Long totalWorkOutsId;
    private String name;
    private String target;
    private int sets;
    private int weight;
    private int reps;
    private String restTime;
    private int performance;
    private WorkOutStatus workOutStatus;
}