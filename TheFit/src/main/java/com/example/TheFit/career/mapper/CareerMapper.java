package com.example.TheFit.career.mapper;

import com.example.TheFit.career.domain.Career;
import com.example.TheFit.career.dto.CareerReqDto;
import com.example.TheFit.career.dto.CareerResDto;
import com.example.TheFit.user.trainer.domain.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CareerMapper {
    CareerMapper INSTANCE = Mappers.getMapper(CareerMapper.class);

    default Career toEntity(Trainer trainer, @MappingTarget CareerReqDto careerReqDto){
        if ( careerReqDto == null ) {
            return null;
        }
        Career.CareerBuilder career = Career.builder();
        career.awards( careerReqDto.getAwards() );
        career.license( careerReqDto.getLicense() );
        career.work( careerReqDto.getWork() );
        career.trainer(trainer);
        return career.build();
    }
    @Mapping(source = "id", target = "trainerId")
    CareerResDto toDto(Career career);

}