package com.example.TheFit.user.mapper;

import com.example.TheFit.user.entity.Gender;
import com.example.TheFit.user.entity.Role;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.member.dto.MemberReqDto;
import com.example.TheFit.user.member.dto.MemberResDto;
import com.example.TheFit.user.trainer.domain.Trainer;
import com.example.TheFit.user.trainer.dto.TrainerReqDto;
import com.example.TheFit.user.trainer.dto.TrainerResDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    default Trainer toEntity(String url,TrainerReqDto dto){
        if ( dto == null ) {
            return null;
        }
        Trainer.TrainerBuilder<?, ?> trainer = Trainer.builder();
        trainer.name( dto.getName() );
        trainer.email( dto.getEmail() );
        trainer.password( dto.getPassword() );
        trainer.cmHeight( dto.getCmHeight() );
        trainer.kgWeight( dto.getKgWeight() );
        trainer.phoneNumber( dto.getPhoneNumber() );
        if ( dto.getGender() != null ) {
            trainer.gender( Enum.valueOf( Gender.class, dto.getGender() ) );
        }
        if ( dto.getRole() != null ) {
            trainer.role( Enum.valueOf( Role.class, dto.getRole() ) );
        }
        trainer.profileImage(url);
        return trainer.build();
    }

    default MemberResDto toDto(Member member){
        if ( member == null ) {
            return null;
        }

        MemberResDto.MemberResDtoBuilder memberResDto = MemberResDto.builder();
        memberResDto.id( member.getId() );
        memberResDto.name( member.getName() );
        memberResDto.email( member.getEmail() );
        memberResDto.cmHeight( member.getCmHeight() );
        memberResDto.kgWeight( member.getKgWeight() );
        memberResDto.delYn(member.delYn);
        if(member.getTrainer()!=null){
            memberResDto.trainerId( member.getTrainer().getId() );
        }
        if ( member.getGender() != null ) {
            memberResDto.gender( member.getGender().name() );
        }
        if ( member.getRole() != null ) {
            memberResDto.role( member.getRole().name() );
        }
        memberResDto.profileImage( member.getProfileImage() );
        memberResDto.phoneNumber( member.getPhoneNumber() );

        return memberResDto.build();
    }

    TrainerResDto toDto(Trainer trainer);


//    update(TrainerReqDto dto, @MappingTarget Trainer trainer);

    default Member toEntity(String url,MemberReqDto dto,Trainer trainer){
        if ( dto == null ) {
            return null;
        }

        Member.MemberBuilder<?, ?> member = Member.builder();
        member.name( dto.getName() );
        member.email( dto.getEmail() );
        member.password( dto.getPassword() );
        member.cmHeight( dto.getCmHeight() );
        member.kgWeight( dto.getKgWeight() );
        member.phoneNumber( dto.getPhoneNumber() );
        member.trainer(trainer);
        if ( dto.getGender() != null ) {
            member.gender( Enum.valueOf( Gender.class, dto.getGender() ) );
        }
        if ( dto.getRole() != null ) {
            member.role( Enum.valueOf( Role.class, dto.getRole() ) );
        }
        member.profileImage( url );

        return member.build();
    }
}