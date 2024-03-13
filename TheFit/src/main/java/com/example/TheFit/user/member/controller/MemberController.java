package com.example.TheFit.user.member.controller;


import com.example.TheFit.common.TheFitResponse;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.member.dto.MemberReqDto;
import com.example.TheFit.user.member.dto.MemberResDto;
import com.example.TheFit.user.member.service.MemberService;
import com.example.TheFit.security.TokenService;
import com.example.TheFit.user.trainer.dto.TrainerResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    @PostMapping("/create")
    public ResponseEntity<TheFitResponse> create(@Valid @ModelAttribute MemberReqDto memberReqDto){
        Member member = memberService.create(memberReqDto);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.CREATED,"success create",member.getId()),HttpStatus.CREATED);
    }
    @GetMapping("/list")
    public ResponseEntity<TheFitResponse> members(){
        List<MemberResDto> memberResDtos =  memberService.findAll();
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.CREATED,"success check",memberResDtos),HttpStatus.CREATED);
    }
    @PatchMapping("/update")
    public ResponseEntity<TheFitResponse> update(@Valid @ModelAttribute MemberReqDto memberReqDto) {
        Member member = memberService.update(memberReqDto);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.CREATED,"success update",member.getId()),HttpStatus.CREATED);
    }

    @PatchMapping("/update/mytrainer/{trainerId}")
    public ResponseEntity<TheFitResponse> updateMyTrainer(@Valid @PathVariable Long trainerId) {
        System.out.println(trainerId);
        Member member = memberService.updateMyTrainer(trainerId);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.CREATED,"success update",member.getId()),HttpStatus.CREATED);
    }

    @GetMapping("/my/info")
    public ResponseEntity<TheFitResponse> myInfo(){
        MemberResDto memberResDto = memberService.findMyInfo();
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success check",memberResDto),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TheFitResponse> delete(@Valid @PathVariable Long id) {
        memberService.delete(id);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success delete",null),HttpStatus.OK);
    }

    @GetMapping("/my/trainer")
    public ResponseEntity<TheFitResponse> myTrainer(){
        TrainerResDto trainerResDto = memberService.findMyTrainer();
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success find",trainerResDto),HttpStatus.OK);
    }
}


