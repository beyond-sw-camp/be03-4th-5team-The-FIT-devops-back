package com.example.TheFit.workoutlist.service;

import com.example.TheFit.common.ErrorCode;
import com.example.TheFit.common.TheFitBizException;
import com.example.TheFit.sse.SseController;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.member.repository.MemberRepository;
import com.example.TheFit.user.trainer.domain.Trainer;
import com.example.TheFit.user.trainer.repository.TrainerRepository;
import com.example.TheFit.workoutlist.domain.WorkOutList;
import com.example.TheFit.workoutlist.dto.WorkOutListReqDto;
import com.example.TheFit.workoutlist.dto.WorkOutListResDto;
import com.example.TheFit.workoutlist.mapper.WorkOutListMapper;
import com.example.TheFit.workoutlist.repository.WorkOutListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkOutListService {
    private final WorkOutListRepository workOutListRepository;
    private final MemberRepository memberRepository;
    private final WorkOutListMapper workOutListMapper;
    private final SseController sseController;
    private final TrainerRepository trainerRepository;
    @Autowired
    public WorkOutListService(WorkOutListRepository workOutListRepository, MemberRepository memberRepository, WorkOutListMapper workOutListMapper, SseController sseController, TrainerRepository trainerRepository) {
        this.workOutListRepository = workOutListRepository;
        this.memberRepository = memberRepository;
        this.workOutListMapper = workOutListMapper;
        this.sseController = sseController;
        this.trainerRepository = trainerRepository;
    }

    public WorkOutList create(WorkOutListReqDto workOutListReqDto) throws TheFitBizException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Trainer trainer = trainerRepository.findByEmail(authentication.getName()).orElseThrow(
                ()-> new TheFitBizException(ErrorCode.NOT_FOUND_TRAINER)
                );
        Member member = memberRepository.findById(workOutListReqDto.getMemberId())
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER));
        WorkOutList workOutList = workOutListMapper.toEntity(member, workOutListReqDto);
        if(workOutListRepository.findByMemberIdAndWorkOutDate(workOutList.getMember().getId(),workOutList.getWorkOutDate()).isEmpty()){
            workOutListRepository.save(workOutList);
        }
        sseController.sendToMember(member,"운동 할당",workOutListReqDto.getWorkOutDate(),trainer.getName());
        return workOutList;
    }

    public void delete(Long id) {
        workOutListRepository.deleteById(id);
    }

    public List<WorkOutListResDto> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = memberRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
        ).getId();
        List<WorkOutList> workOutLists = workOutListRepository.findByMemberId(memberId);
        List<WorkOutListResDto> workOutListResDtos = new ArrayList<>();
        for(WorkOutList workOutList : workOutLists){
            workOutListResDtos.add(workOutListMapper.toDto(workOutList));
        }
        return workOutListResDtos;
    }

    public List<WorkOutListResDto> findByEmail(String email) {
        Long memberId = memberRepository.findByEmail(email).orElseThrow(
                () -> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
        ).getId();
        List<WorkOutList> workOutLists = workOutListRepository.findByMemberId(memberId);
        List<WorkOutListResDto> workOutListResDtos = new ArrayList<>();
        for(WorkOutList workOutList : workOutLists){
            workOutListResDtos.add(workOutListMapper.toDto(workOutList));
        }
        return workOutListResDtos;
    }

    public WorkOutListResDto findByMemberEmailAndWorkOutDate(String email, String inputDate) {
        Long memberId = memberRepository.findByEmail(email).orElseThrow(
                () -> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
        ).getId();
        LocalDate date = LocalDate.parse(inputDate, DateTimeFormatter.ISO_LOCAL_DATE);
        WorkOutList workOutList = workOutListRepository.findByMemberIdAndWorkOutDate(memberId, date)
                .orElseThrow(()-> new TheFitBizException(ErrorCode.NOT_FOUND_WORKOUTLIST));
        return workOutListMapper.toDto(workOutList);
    }
}
