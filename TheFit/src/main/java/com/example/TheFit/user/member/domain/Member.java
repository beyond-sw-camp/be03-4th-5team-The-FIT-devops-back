package com.example.TheFit.user.member.domain;

import com.example.TheFit.user.member.dto.MemberReqDto;
import com.example.TheFit.user.trainer.domain.Trainer;
import com.example.TheFit.user.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class Member extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    public void update(MemberReqDto memberReqDto,Trainer trainer,String url) {
        this.name = memberReqDto.getName();
        this.password = memberReqDto.getPassword();
        this.cmHeight = memberReqDto.getCmHeight();
        this.kgWeight = memberReqDto.getKgWeight();
        this.profileImage = url;
        this.phoneNumber = memberReqDto.getPhoneNumber();
        this.trainer = trainer;
    }
    public void updateTrainer(Trainer trainer){
        this.trainer = trainer;
    }

    public void delete() {
        this.delYn = "Y";
    }
}
