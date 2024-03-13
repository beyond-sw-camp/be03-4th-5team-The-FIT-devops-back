package com.example.TheFit.diet.mapper;

import com.example.TheFit.career.domain.Career;
import com.example.TheFit.career.dto.CareerReqDto;
import com.example.TheFit.career.dto.CareerResDto;
import com.example.TheFit.diet.domain.Diet;
import com.example.TheFit.diet.dto.DietReqDto;
import com.example.TheFit.diet.dto.DietResDto;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.trainer.domain.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface DietMapper {
    DietMapper INSTANCE = Mappers.getMapper(DietMapper.class);

    default Diet toEntity(Member member,String url,DietReqDto dietReqDto){
        if ( dietReqDto == null ) {
            return null;
        }
        Diet diet = Diet.builder()
                .member(member)
                .imagePath(url)
                .type(dietReqDto.getType())
                .comment(dietReqDto.getComment())
                .dietDate(LocalDate.parse(dietReqDto.getDietDate()))
                .build();
        return diet;
    }

    default DietResDto toDto(Member member,Diet diet){
        if ( diet == null ) {
            return null;
        }
        DietResDto dietResDto = DietResDto.builder()
                .id(diet.getId())
                .memberId(member.getId())
                .imagePath(diet.getImagePath())
                .type(diet.getType())
                .comment(diet.getComment())
                .dietDate(diet.getDietDate())
                .build();
        return dietResDto;
    }

}