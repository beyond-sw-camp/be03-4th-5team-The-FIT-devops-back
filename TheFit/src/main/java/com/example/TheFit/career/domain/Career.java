package com.example.TheFit.career.domain;

import com.example.TheFit.career.dto.CareerReqDto;
import com.example.TheFit.user.trainer.domain.Trainer;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Career {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;
    private String awards;
    private String license;
    private String work;

    public void update(CareerReqDto careerReqDto,Trainer trainer) {
        this.awards = careerReqDto.getAwards();
        this.license = careerReqDto.getLicense();
        this.work = careerReqDto.getWork();
        this.trainer = trainer;
    }
}
