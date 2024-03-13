package com.example.TheFit.feedback.dietfeedback.controller;

import com.example.TheFit.common.TheFitResponse;
import com.example.TheFit.feedback.dietfeedback.domain.DietFeedBack;
import com.example.TheFit.feedback.dietfeedback.dto.DietFeedBackReqDto;
import com.example.TheFit.feedback.dietfeedback.dto.DietFeedBackResDto;
import com.example.TheFit.feedback.dietfeedback.service.DietFeedBackService;
import com.example.TheFit.feedback.workoutfeedback.dto.WorkOutFeedBackResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("diet/feedback")
public class DietFeedBackController {

    private final DietFeedBackService dietFeedBackService;

    @Autowired
    public DietFeedBackController(DietFeedBackService dietFeedBackService) {
        this.dietFeedBackService = dietFeedBackService;
    }

    @PostMapping("/create")
    public ResponseEntity<TheFitResponse> create(@Valid @RequestBody DietFeedBackReqDto dietFeedBackReqDto) {
        DietFeedBack dietFeedBack = dietFeedBackService.create(dietFeedBackReqDto);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.CREATED,"success create",dietFeedBack.getId()),HttpStatus.CREATED);
    }
    @GetMapping("/list")
    public ResponseEntity<TheFitResponse> dietFeedBackResDtos(){
        List<DietFeedBackResDto> dietFeedBackResDtos = dietFeedBackService.findAll();
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.CREATED,"success create",dietFeedBackResDtos),HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TheFitResponse> delete(@PathVariable Long id) {
        dietFeedBackService.delete(id);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success delete"),HttpStatus.OK);
    }
    @GetMapping("/member/find")
    public ResponseEntity<TheFitResponse> findByMember(@RequestParam(value ="date") String date){
        System.out.println(date);
       DietFeedBackResDto dietFeedBackResDto = dietFeedBackService.findFeedbackByMember(date);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success find",dietFeedBackResDto),HttpStatus.OK);
    }
    @GetMapping("/trainer/find")
    public ResponseEntity<TheFitResponse> findByTrainer(@RequestParam(value ="date") String date,@RequestParam(value ="memberEmail") String memberEmail){
        System.out.println(date);
        DietFeedBackResDto dietFeedBackResDto = dietFeedBackService.findFeedbackByTrainer(date,memberEmail);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success find",dietFeedBackResDto),HttpStatus.OK);
    }
}