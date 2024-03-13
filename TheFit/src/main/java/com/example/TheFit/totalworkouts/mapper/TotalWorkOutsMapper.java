package com.example.TheFit.totalworkouts.mapper;

import com.example.TheFit.totalworkouts.domain.TotalWorkOuts;
import com.example.TheFit.totalworkouts.dto.TotalWorkOutsReqDto;
import com.example.TheFit.totalworkouts.dto.TotalWorkOutsResDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TotalWorkOutsMapper {
    TotalWorkOutsMapper INSTANCE = Mappers.getMapper(TotalWorkOutsMapper.class);

    TotalWorkOuts toEntity(TotalWorkOutsReqDto dto);
    TotalWorkOutsResDto toDto(TotalWorkOuts totalWorkOuts);
}
