package com.example.TheFit.feedback;

import com.example.TheFit.diet.domain.Diet;
import com.example.TheFit.feedback.dietfeedback.domain.DietFeedBack;
import com.example.TheFit.feedback.dietfeedback.dto.DietFeedBackReqDto;
import com.example.TheFit.feedback.dietfeedback.dto.DietFeedBackResDto;
import com.example.TheFit.feedback.workoutfeedback.domain.WorkOutFeedBack;
import com.example.TheFit.feedback.workoutfeedback.dto.WorkOutFeedBackReqDto;
import com.example.TheFit.feedback.workoutfeedback.dto.WorkOutFeedBackResDto;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.trainer.domain.Trainer;
import com.example.TheFit.workoutlist.domain.WorkOutList;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface FeedBackMapper {
    FeedBackMapper INSTANCE = Mappers.getMapper(FeedBackMapper.class);

    default DietFeedBack toEntity(Trainer trainer, Member member, DietFeedBackReqDto dto){
        if ( dto == null ) {
            return null;
        }

        return DietFeedBack.builder()
                .member(member)
                .uploadDate(dto.getUploadDate())
                .trainer(trainer)
                .feedBack( dto.getFeedBack() )
                .rating( dto.getRating() )
                .build();
    }

    default DietFeedBackResDto toDto(DietFeedBack dietFeedBack){
        if ( dietFeedBack == null ) {
            return null;
        }

        return DietFeedBackResDto.builder()
                .trainerName(dietFeedBack.getTrainer().name)
                .uploadDate(dietFeedBack.getUploadDate())
                .trainerId(dietFeedBack.getTrainer().getId())
                .id( dietFeedBack.getId() )
                .createdTime(dietFeedBack.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .feedBack( dietFeedBack.getFeedBack() )
                .rating( dietFeedBack.getRating() )
                .build();
    }

    default WorkOutFeedBack toEntity(Trainer trainer,WorkOutList workOutList,WorkOutFeedBackReqDto dto){
        if ( dto == null ) {
            return null;
        }
        return WorkOutFeedBack.builder()
                .workOutList(workOutList)
                .trainer(trainer)
                .feedBack( dto.getFeedBack() )
                .rating( dto.getRating() )
                .uploadDate(dto.getUploadDate())
                .build();
    }

    default WorkOutFeedBackResDto toDto(WorkOutFeedBack workOutFeedBack){
        if ( workOutFeedBack == null ) {
            return null;
        }
        return WorkOutFeedBackResDto.builder()
                .trainerId(workOutFeedBack.getTrainer().getId())
                .id(workOutFeedBack.getId())
                .uploadDate(workOutFeedBack.getUploadDate())
                .feedBack( workOutFeedBack.getFeedBack() )
                .rating( workOutFeedBack.getRating() )
                .build();
    }
}
