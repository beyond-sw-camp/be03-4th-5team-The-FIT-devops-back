package com.example.TheFit.feedback.dietfeedback.service;

import com.example.TheFit.common.ErrorCode;
import com.example.TheFit.common.TheFitBizException;
import com.example.TheFit.feedback.FeedBackMapper;
import com.example.TheFit.feedback.dietfeedback.domain.DietFeedBack;
import com.example.TheFit.feedback.dietfeedback.dto.DietFeedBackReqDto;
import com.example.TheFit.feedback.dietfeedback.dto.DietFeedBackResDto;
import com.example.TheFit.feedback.dietfeedback.repository.DietFeedBackRepository;
import com.example.TheFit.sse.SseController;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.member.repository.MemberRepository;
import com.example.TheFit.user.trainer.domain.Trainer;
import com.example.TheFit.user.trainer.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DietFeedBackService {

    private final DietFeedBackRepository dietFeedBackRepository;
    private final FeedBackMapper feedBackMapper = FeedBackMapper.INSTANCE;
    private final TrainerRepository trainerRepository;
    private final MemberRepository memberRepository;
    private final SseController sseController;

    @Autowired
    public DietFeedBackService(DietFeedBackRepository dietFeedBackRepository, TrainerRepository trainerRepository, MemberRepository memberRepository, SseController sseController) {
        this.dietFeedBackRepository = dietFeedBackRepository;
        this.trainerRepository = trainerRepository;
        this.memberRepository = memberRepository;
        this.sseController = sseController;
    }

    @Transactional
    public DietFeedBack create(DietFeedBackReqDto dietFeedBackReqDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(dietFeedBackReqDto.getMemberEmail()).orElseThrow(
                ()->new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
        );
        Trainer trainer = trainerRepository.findByEmail(authentication.getName()).orElseThrow(
                ()->new TheFitBizException(ErrorCode.NOT_FOUND_TRAINER));
        Optional<DietFeedBack> optionalDietFeedBack = dietFeedBackRepository
                .findByUploadDateAndMemberId(dietFeedBackReqDto.getUploadDate(),member.getId());
        if(optionalDietFeedBack.isPresent()){
            sseController.sendToMember(member,"diet",dietFeedBackReqDto.getUploadDate(),trainer.getName());
            return update(optionalDietFeedBack.get(),dietFeedBackReqDto);
        }
        DietFeedBack dietFeedback = feedBackMapper.toEntity(trainer,member,dietFeedBackReqDto);
        sseController.sendToMember(member,"diet",dietFeedBackReqDto.getUploadDate(),trainer.getName());
        return dietFeedBackRepository.save(dietFeedback);
    }
    public List<DietFeedBackResDto> findAll() {
        List<DietFeedBack> dietFeedBacks = dietFeedBackRepository.findAll();
        List<DietFeedBackResDto> dietFeedBackResDtoList = new ArrayList<>();
        for(DietFeedBack dietFeedBack : dietFeedBacks){
            dietFeedBackResDtoList.add(feedBackMapper.toDto(dietFeedBack));
        }
        return dietFeedBackResDtoList;
    }
    public DietFeedBack update(DietFeedBack dietFeedBack, DietFeedBackReqDto dietFeedBackReqDto) {
        dietFeedBack.update(dietFeedBackReqDto);
        return dietFeedBack;
    }

    public void delete(Long id) {
        dietFeedBackRepository.deleteById(id);
    }

    public DietFeedBackResDto findFeedbackByMember(String date) {
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(
                ()->new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
        );
        DietFeedBack dietFeedBack= dietFeedBackRepository.findByUploadDateAndMemberId(date,member.getId()).orElseThrow(
                ()-> new TheFitBizException(ErrorCode.NOT_FOUND_DIET_FEEDBACK)
        );
        return feedBackMapper.toDto(dietFeedBack);
    }
    public DietFeedBackResDto findFeedbackByTrainer(String date,String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(
                ()->new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
        );
        DietFeedBack dietFeedBack= dietFeedBackRepository.findByUploadDateAndMemberId(date,member.getId()).orElseThrow(
                ()-> new TheFitBizException(ErrorCode.NOT_FOUND_DIET_FEEDBACK)
        );
        return feedBackMapper.toDto(dietFeedBack);
    }
}