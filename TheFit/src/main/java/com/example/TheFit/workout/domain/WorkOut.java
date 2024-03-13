package com.example.TheFit.workout.domain;

import com.example.TheFit.totalworkouts.domain.TotalWorkOuts;
import com.example.TheFit.workout.dto.WorkOutReqDto;
import com.example.TheFit.workoutlist.domain.WorkOutList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkOut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "totalWorkOuts_id")
    private TotalWorkOuts totalWorkOuts;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workOutList_id")
    private WorkOutList workOutList;
    @Column(nullable = false)
    private int sets;
    @Column(nullable = false)
    private int weight;
    @Column(nullable = false)
    private int reps;
    private String restTime;
    @Column(nullable = false)
    private int performance;
    @Enumerated(EnumType.STRING)
    private WorkOutStatus workOutStatus;
    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime updatedTime;

    public void update(WorkOutReqDto workOutReqDto) {
        this.sets = workOutReqDto.getSets();
        this.weight = workOutReqDto.getWeight();
        this.reps = workOutReqDto.getReps();
        this.restTime = workOutReqDto.getRestTime();
        this.performance = workOutReqDto.getPerformance();
        this.workOutStatus = workOutReqDto.getWorkOutStatus();
    }
}
