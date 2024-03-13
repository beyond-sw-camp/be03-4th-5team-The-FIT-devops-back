package com.example.TheFit.totalworkouts.controller;

import com.example.TheFit.common.TheFitResponse;
import com.example.TheFit.totalworkouts.domain.TotalWorkOuts;
import com.example.TheFit.totalworkouts.dto.TotalWorkOutsReqDto;
import com.example.TheFit.totalworkouts.dto.TotalWorkOutsResDto;
import com.example.TheFit.totalworkouts.service.TotalWorkOutsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/totalworkouts")
public class TotalWorkOutsController {

    private final TotalWorkOutsService totalWorkOutsService;

    @Autowired
    public TotalWorkOutsController(TotalWorkOutsService totalWorkOutsService) {
        this.totalWorkOutsService = totalWorkOutsService;
    }

    @PostMapping("/create")
    public ResponseEntity<TheFitResponse> create(@Valid @RequestBody TotalWorkOutsReqDto totalWorkOutsReqDto){
        System.out.println(totalWorkOutsReqDto.getName());
        TotalWorkOuts totalWorkOuts = totalWorkOutsService.create(totalWorkOutsReqDto);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.CREATED,"success create",totalWorkOuts.getId()),HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<TheFitResponse> findAll(){
        List<TotalWorkOutsResDto> totalWorkOutsResDtos = totalWorkOutsService.findAll();
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.CREATED,"success check",totalWorkOutsResDtos),HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<TheFitResponse> update(@PathVariable Long id, @Valid @RequestBody TotalWorkOutsReqDto totalWorkOutsReqDto){
        TotalWorkOuts totalWorkOuts = totalWorkOutsService.update(id, totalWorkOutsReqDto);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success update",totalWorkOuts.getId()),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TheFitResponse> delete(@PathVariable Long id) {
        totalWorkOutsService.delete(id);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success delete",null),HttpStatus.OK);
    }
}