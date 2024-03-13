package com.example.TheFit.career.controller;

import com.example.TheFit.career.domain.Career;
import com.example.TheFit.career.dto.CareerReqDto;
import com.example.TheFit.career.dto.CareerResDto;
import com.example.TheFit.career.service.CareerService;
import com.example.TheFit.common.TheFitResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("career")
public class CareerController {
    private final CareerService careerService;
    @Autowired
    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }

    @PostMapping("/create")
    public ResponseEntity<TheFitResponse> create(@Valid @RequestBody CareerReqDto careerReqDto){
        Career career= careerService.create(careerReqDto);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.CREATED,"success create",career.getId()),HttpStatus.CREATED);
    }
    @GetMapping("/list")
    public ResponseEntity<TheFitResponse> Career(){
        List<CareerResDto> careerResDtos =  careerService.findAll();
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success check",careerResDtos),HttpStatus.OK);
    }
    @PatchMapping("/update/{id}")
    public  ResponseEntity<TheFitResponse> update(@PathVariable Long id, @Valid @RequestBody CareerReqDto careerReqDto){
        Career career = careerService.update(id, careerReqDto);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success update",career.getId()),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TheFitResponse> delete(@PathVariable Long id) {
        careerService.delete(id);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success delete",null),HttpStatus.OK);
    }
}
