package com.example.TheFit.feedback.workoutfeedback.service;

import com.example.TheFit.common.ErrorCode;
import com.example.TheFit.common.TheFitBizException;
import com.example.TheFit.feedback.FeedBackMapper;
import com.example.TheFit.feedback.workoutfeedback.repository.WorkOutFeedBackRepository;
import com.example.TheFit.sse.SseController;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.member.repository.MemberRepository;
import com.example.TheFit.user.trainer.domain.Trainer;
import com.example.TheFit.user.trainer.repository.TrainerRepository;
import com.example.TheFit.feedback.workoutfeedback.domain.WorkOutFeedBack;
import com.example.TheFit.feedback.workoutfeedback.dto.WorkOutFeedBackReqDto;
import com.example.TheFit.feedback.workoutfeedback.dto.WorkOutFeedBackResDto;
import com.example.TheFit.workoutlist.domain.WorkOutList;
import com.example.TheFit.workoutlist.repository.WorkOutListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WorkOutFeedBackService {

    private final WorkOutFeedBackRepository workOutFeedBackRepository;
    private final WorkOutListRepository workOutListRepository;
    private final FeedBackMapper feedBackMapper = FeedBackMapper.INSTANCE;
    private final TrainerRepository trainerRepository;
    private final MemberRepository memberRepository;
    private final SseController sseController;

    @Autowired
    public WorkOutFeedBackService(WorkOutFeedBackRepository workOutFeedBackRepository, WorkOutListRepository workOutListRepository, WorkOutListRepository workOutListRepository1, TrainerRepository trainerRepository, MemberRepository memberRepository, SseController sseController) {
        this.workOutFeedBackRepository = workOutFeedBackRepository;
        this.workOutListRepository = workOutListRepository1;
        this.trainerRepository = trainerRepository;
        this.memberRepository = memberRepository;
        this.sseController = sseController;
    }
    public WorkOutFeedBack create(WorkOutFeedBackReqDto workOutFeedBackReqDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Trainer trainer = trainerRepository.findByEmail(authentication.getName())
                .orElseThrow(()->new TheFitBizException(ErrorCode.NOT_FOUND_TRAINER));
        Member member = memberRepository.findByEmail(workOutFeedBackReqDto.getMemberEmail()).orElseThrow(
                ()-> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
        );
        LocalDate date = LocalDate.parse(workOutFeedBackReqDto.getUploadDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        WorkOutList workOutList = workOutListRepository.findByMemberIdAndWorkOutDate(member.getId(),date).orElseThrow(
                ()-> new TheFitBizException(ErrorCode.NOT_FOUND_WORKOUTLIST)
        );
        Optional<WorkOutFeedBack> workOutFeedBack = workOutFeedBackRepository.findByWorkOutListId(workOutList.getId());
        if(workOutFeedBack.isEmpty()){
            WorkOutFeedBack workOutFeedback = feedBackMapper.toEntity(trainer,workOutList,workOutFeedBackReqDto);
            sseController.sendToMember(member,"workout",workOutFeedBackReqDto.getUploadDate(),trainer.getName());
            return workOutFeedBackRepository.save(workOutFeedback);
        }else{
            WorkOutFeedBack nowWorkOutFeedBack = workOutFeedBack.get();
            nowWorkOutFeedBack.update(workOutFeedBackReqDto);
            sseController.sendToMember(member,"workout",workOutFeedBackReqDto.getUploadDate(),trainer.getName());
            return nowWorkOutFeedBack;
        }
    }
    public List<WorkOutFeedBackResDto> findAll() {
        List<WorkOutFeedBack> workOutFeedBacks = workOutFeedBackRepository.findAll();
        List<WorkOutFeedBackResDto> workOutFeedBackDtos = new ArrayList<>();
        for (WorkOutFeedBack workOutFeedBack : workOutFeedBacks) {
            workOutFeedBackDtos.add(feedBackMapper.toDto(workOutFeedBack));
        }
        return workOutFeedBackDtos;
    }
    public WorkOutFeedBack update(Long id, WorkOutFeedBackReqDto workOutFeedBackReqDto) {
        WorkOutFeedBack workOutFeedBack = workOutFeedBackRepository.findById(id)
                .orElseThrow(()->new TheFitBizException(ErrorCode.NOT_FOUND_WORKOUT_FEEDBACK));
        workOutFeedBack.update(workOutFeedBackReqDto);
        return workOutFeedBackRepository.save(workOutFeedBack);
    }
    public void delete(Long id) {workOutFeedBackRepository.deleteById(id);}

    public WorkOutFeedBackResDto findFeedbackByMember(String date) throws TheFitBizException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(
                ()->new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
        );
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        WorkOutList workOutList = workOutListRepository.findByMemberIdAndWorkOutDate(member.getId(),localDate).orElseThrow(
                ()->new TheFitBizException(ErrorCode.NOT_FOUND_WORKOUTLIST)
        );
        WorkOutFeedBack workOutFeedBack= workOutFeedBackRepository.findByWorkOutListId(workOutList.getId()).orElseThrow(
                ()-> new TheFitBizException(ErrorCode.NOT_FOUND_WORKOUT_FEEDBACK)
        );
        return feedBackMapper.toDto(workOutFeedBack);
    }

    public WorkOutFeedBackResDto findFeedbackByTrainer(String date, String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(
                ()->new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
        );
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        WorkOutList workOutList = workOutListRepository.findByMemberIdAndWorkOutDate(member.getId(),localDate).orElseThrow(
                ()->new TheFitBizException(ErrorCode.NOT_FOUND_WORKOUTLIST)
        );
        WorkOutFeedBack workOutFeedBack= workOutFeedBackRepository.findByWorkOutListId(workOutList.getId()).orElseThrow(
                ()-> new TheFitBizException(ErrorCode.NOT_FOUND_WORKOUT_FEEDBACK)
        );
        return feedBackMapper.toDto(workOutFeedBack);
    }
}