package com.example.TheFit.feedback.workoutfeedback.controller;

import com.example.TheFit.common.TheFitResponse;
import com.example.TheFit.feedback.workoutfeedback.domain.WorkOutFeedBack;
import com.example.TheFit.feedback.workoutfeedback.dto.WorkOutFeedBackReqDto;
import com.example.TheFit.feedback.workoutfeedback.dto.WorkOutFeedBackResDto;
import com.example.TheFit.feedback.workoutfeedback.service.WorkOutFeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("workout/feedback")
public class WorkOutFeedBackController {

    private final WorkOutFeedBackService workOutFeedBackService;

    @Autowired
    public WorkOutFeedBackController(WorkOutFeedBackService workOutFeedBackService) {
        this.workOutFeedBackService = workOutFeedBackService;
    }

    @PostMapping("/create")
    public ResponseEntity<TheFitResponse> create(@Valid @RequestBody WorkOutFeedBackReqDto workOutFeedBackReqDto) {
        WorkOutFeedBack workOutFeedBack = workOutFeedBackService.create(workOutFeedBackReqDto);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.CREATED,"success create",workOutFeedBack.getId()),HttpStatus.CREATED);
    }
    @GetMapping("/list")
    public List<WorkOutFeedBackResDto> workOutFeedBacks(){
        return workOutFeedBackService.findAll();
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<TheFitResponse> update(@PathVariable Long id, @Valid @RequestBody WorkOutFeedBackReqDto workOutFeedBackReqDto) {
        WorkOutFeedBack workOutFeedBack = workOutFeedBackService.update(id, workOutFeedBackReqDto);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success update",workOutFeedBack.getId()),HttpStatus.OK);
    }
    @GetMapping("/member/find")
    public ResponseEntity<TheFitResponse> findByMember(@RequestParam(value ="date") String date){
        WorkOutFeedBackResDto workOutFeedBackResDto = workOutFeedBackService.findFeedbackByMember(date);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success find",workOutFeedBackResDto),HttpStatus.OK);
    }
    @GetMapping("/trainer/find")
    public ResponseEntity<TheFitResponse> findByTrainer(@RequestParam(value ="date") String date,@RequestParam(value = "memberEmail")String memberEmail) {
        WorkOutFeedBackResDto workOutFeedBackResDto = workOutFeedBackService.findFeedbackByTrainer(date,memberEmail);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success find",workOutFeedBackResDto),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TheFitResponse> delete(@PathVariable Long id) {
        workOutFeedBackService.delete(id);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success delete"),HttpStatus.OK);
    }
}