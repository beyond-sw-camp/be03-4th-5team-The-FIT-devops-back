package com.example.TheFit.diet.controller;

import com.example.TheFit.common.TheFitResponse;
import com.example.TheFit.diet.domain.Diet;
import com.example.TheFit.diet.dto.DietReqDto;
import com.example.TheFit.diet.dto.DietResDto;
import com.example.TheFit.diet.service.DietService;
import com.example.TheFit.workout.dto.WorkOutUsingMemberResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/diet")
public class DietController {

    private final DietService dietService;

    @Autowired
    public DietController(DietService dietService) {
        this.dietService = dietService;
    }

    @PostMapping("/create")
    public ResponseEntity<TheFitResponse> createDiet(@Valid @ModelAttribute DietReqDto dietReqDto) {
        Diet diet = dietService.create(dietReqDto);
        TheFitResponse theFitResponse = new TheFitResponse(HttpStatus.CREATED,"success create",diet.getId());
        return new ResponseEntity<>(theFitResponse,HttpStatus.CREATED);

    }
    @GetMapping("/{id}")
    public ResponseEntity<TheFitResponse> findById(@PathVariable Long id) {
        DietResDto dietResDto = dietService.findById(id);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success find",dietResDto),HttpStatus.OK);
    }


    @GetMapping("/list")
    public ResponseEntity<TheFitResponse> findById() {
        List<DietResDto> dietResDtos =  dietService.findAll();
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success find",dietResDtos),HttpStatus.OK);
    }

    @GetMapping("/list/{email}")
    public ResponseEntity<TheFitResponse> findByEmail(@PathVariable String email){
        List<DietResDto> dietResDtos = dietService.findByEmail(email);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success create",dietResDtos),HttpStatus.OK);
    }

    @GetMapping("/list/member")
    public ResponseEntity<TheFitResponse> findByMemberEmailAndDietDate(@RequestParam("memberEmail") String email,
                                                                          @RequestParam("date") String date) {
        List<DietResDto> dietResDtos = dietService.findByMemberEmailAndDietDate(email, date);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK, "success find", dietResDtos), HttpStatus.OK);
    }


    @PatchMapping("/update/{id}")
    public ResponseEntity<TheFitResponse> updateDiet(@PathVariable Long id, @Valid @RequestBody DietReqDto dietReqDto) {
        Diet diet = dietService.update(id, dietReqDto);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success update",diet.getId()),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TheFitResponse> delete(@PathVariable Long id){
        dietService.delete(id);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success update",null),HttpStatus.OK);
    }
}