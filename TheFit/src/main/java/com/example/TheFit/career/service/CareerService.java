package com.example.TheFit.career.service;


import com.example.TheFit.career.domain.Career;
import com.example.TheFit.career.dto.CareerReqDto;
import com.example.TheFit.career.dto.CareerResDto;
import com.example.TheFit.career.mapper.CareerMapper;
import com.example.TheFit.career.repository.CareerRepository;
import com.example.TheFit.common.ErrorCode;
import com.example.TheFit.common.TheFitBizException;
import com.example.TheFit.user.trainer.domain.Trainer;
import com.example.TheFit.user.trainer.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CareerService {
    private final CareerRepository careerRepository;
    private final TrainerRepository trainerRepository;
    private final CareerMapper careerMapper = CareerMapper.INSTANCE;

    @Autowired
    public CareerService(CareerRepository careerRepository, TrainerRepository trainerRepository) {
        this.careerRepository = careerRepository;
        this.trainerRepository = trainerRepository;
    }

    public Career create(CareerReqDto careerReqDto) throws TheFitBizException {
        Trainer trainer = trainerRepository.findById(careerReqDto.getTrainerId())
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_TRAINER));
        Career career = careerMapper.toEntity(trainer,careerReqDto);
        return careerRepository.save(career);
    }

    public List<CareerResDto> findAll() {
        trainerRepository.findAll();
        List<Career> careers = careerRepository.findAll();
        return careers.stream()
                .map(careerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Career update(Long id, CareerReqDto careerReqDto) {
        Career career = careerRepository.findById(id)
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_CAREER));
        Trainer trainer = trainerRepository.findById(careerReqDto.getTrainerId())
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_TRAINER));
        career.update(careerReqDto,trainer);
        return career;
    }

    public void delete(Long id) {
        careerRepository.deleteById(id);
    }
}

