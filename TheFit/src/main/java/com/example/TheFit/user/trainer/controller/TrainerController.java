package com.example.TheFit.user.trainer.controller;

import com.example.TheFit.common.TheFitResponse;
import com.example.TheFit.user.member.dto.MemberResDto;
import com.example.TheFit.user.trainer.domain.Trainer;
import com.example.TheFit.user.trainer.dto.TrainerReqDto;
import com.example.TheFit.user.trainer.dto.TrainerResDto;
import com.example.TheFit.user.trainer.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/trainer")
public class TrainerController {
    private final TrainerService trainerService;

    @Autowired
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping("/create")
    public ResponseEntity<TheFitResponse> create(@Valid @ModelAttribute TrainerReqDto trainerReqDto) {
        Trainer trainer = trainerService.create(trainerReqDto);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.CREATED,"success create",trainer.getId()),HttpStatus.CREATED);
    }

    @GetMapping("/find")
    public ResponseEntity<TheFitResponse> find() {
        TrainerResDto trainer= trainerService.findTrainer();
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success find",trainer),HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<TheFitResponse> trainers() {
        List<TrainerResDto> trainerResDtos = trainerService.findAll();
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success check",trainerResDtos),HttpStatus.OK);
    }
    @GetMapping("available/list")
    public ResponseEntity<TheFitResponse> availableTrainers() {
        List<TrainerResDto> trainerResDtos = trainerService.findAvailable();
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success check",trainerResDtos),HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<TheFitResponse> update(@Valid @ModelAttribute TrainerReqDto trainerReqDto) {
        Trainer trainer = trainerService.update(trainerReqDto);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success update",trainer.getId()),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TheFitResponse> delete(@Valid @PathVariable Long id) {
        trainerService.delete(id);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success delete",null),HttpStatus.OK);
    }

    // (트레이너) 내 정보 조회
    @GetMapping("/my/info")
    public ResponseEntity<TheFitResponse> myInfo(){
        TrainerResDto trainerResDto = trainerService.findMyInfo();
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success find",trainerResDto),HttpStatus.OK);
    }

    // (트레이너) 내  멤버들 조회
    @GetMapping("/my/members")
    public ResponseEntity<TheFitResponse> myMembers(){
        List<MemberResDto> trainerResDto = trainerService.findMyMembers();
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success find",trainerResDto),HttpStatus.OK);
    }
}