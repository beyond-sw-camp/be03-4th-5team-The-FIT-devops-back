package com.example.TheFit.workout.controller;

import com.example.TheFit.common.TheFitResponse;
import com.example.TheFit.diet.dto.DietResDto;
import com.example.TheFit.workout.domain.WorkOut;
import com.example.TheFit.workout.dto.WorkOutReqDto;
import com.example.TheFit.workout.dto.WorkOutResDto;
import com.example.TheFit.workout.dto.WorkOutUsingMemberResDto;
import com.example.TheFit.workout.service.WorkOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/workout")
public class WorkOutController {
    private final WorkOutService workOutService;

    @Autowired
    public WorkOutController(WorkOutService workOutService) {
        this.workOutService = workOutService;
    }

    @PostMapping("/create")
    public ResponseEntity<TheFitResponse> create(@Valid @RequestBody WorkOutReqDto workOutReqDto) {
        WorkOut workOut = workOutService.create(workOutReqDto);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.CREATED, "success create", workOut.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<TheFitResponse> findAll() {
        List<WorkOutResDto> workOutResDtos = workOutService.findAll();
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK, "success check", workOutResDtos), HttpStatus.OK);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<TheFitResponse> findById(@PathVariable Long id) {
        WorkOutResDto workOutResDto = workOutService.findById(id);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK, "success find", workOutResDto), HttpStatus.OK);
    }

    @GetMapping("/list/member")
    public ResponseEntity<TheFitResponse> findByMemberEmailAndWorkOutDate(@RequestParam("memberEmail") String email,
                                                             @RequestParam("date") String date) {
        List<WorkOutUsingMemberResDto> workOutResDto = workOutService.findByMemberEmailAndWorkOutDate(email, date);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK, "success find", workOutResDto), HttpStatus.OK);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<TheFitResponse> update(@PathVariable Long id, @Valid @RequestBody WorkOutReqDto workOutReqDto) {
        WorkOut workOut = workOutService.update(id, workOutReqDto);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK, "success update", workOut.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TheFitResponse> delete(@PathVariable Long id) {
        workOutService.delete(id);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK, "success delete", null), HttpStatus.OK);
    }
}