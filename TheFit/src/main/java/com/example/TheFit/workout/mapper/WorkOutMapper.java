package com.example.TheFit.workout.mapper;

import com.example.TheFit.totalworkouts.domain.TotalWorkOuts;
import com.example.TheFit.workout.domain.WorkOut;
import com.example.TheFit.workout.dto.WorkOutReqDto;
import com.example.TheFit.workout.dto.WorkOutResDto;
import com.example.TheFit.workout.dto.WorkOutUsingMemberResDto;
import com.example.TheFit.workoutlist.domain.WorkOutList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WorkOutMapper {
    WorkOutMapper INSTANCE = Mappers.getMapper(WorkOutMapper.class);

    default WorkOut toEntity(TotalWorkOuts totalWorkOuts, WorkOutList workOutList, WorkOutReqDto dto) {
        if ( dto == null ) {
            return null;
        }
        WorkOut.WorkOutBuilder workOut = WorkOut.builder();
        workOut.totalWorkOuts(totalWorkOuts);
        workOut.workOutList(workOutList);
        workOut.sets( dto.getSets() );
        workOut.weight( dto.getWeight() );
        workOut.reps( dto.getReps() );
        workOut.restTime( dto.getRestTime() );
        workOut.performance( dto.getPerformance() );
        workOut.workOutStatus( dto.getWorkOutStatus() );

        return workOut.build();
    }

    default WorkOutResDto toDto(WorkOut workOut){
        return WorkOutResDto.builder()
                .id(workOut.getId())
                .workOutListId(workOut.getWorkOutList() != null ? workOut.getWorkOutList().getId() : null)
                .totalWorkOutsId(workOut.getTotalWorkOuts() != null ? workOut.getTotalWorkOuts().getId() : null)
                .sets(workOut.getSets())
                .weight(workOut.getWeight())
                .reps(workOut.getReps())
                .restTime(workOut.getRestTime())
                .performance(workOut.getPerformance())
                .workOutStatus(workOut.getWorkOutStatus())
                .build();
    }

    default WorkOutUsingMemberResDto toDtoUsingMember(WorkOut workOut){
        return WorkOutUsingMemberResDto.builder()
                .id(workOut.getId())
                .workOutListId(workOut.getWorkOutList() != null ? workOut.getWorkOutList().getId() : null)
                .totalWorkOutsId(workOut.getTotalWorkOuts() != null ? workOut.getTotalWorkOuts().getId() : null)
                .sets(workOut.getSets())
                .weight(workOut.getWeight())
                .reps(workOut.getReps())
                .restTime(workOut.getRestTime())
                .performance(workOut.getPerformance())
                .workOutStatus(workOut.getWorkOutStatus())
                .build();
    }


    void update(WorkOutReqDto dto, @MappingTarget WorkOut workOut);
}
