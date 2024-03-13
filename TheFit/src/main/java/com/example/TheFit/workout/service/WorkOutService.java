package com.example.TheFit.workout.service;

import com.example.TheFit.common.ErrorCode;
import com.example.TheFit.common.TheFitBizException;
import com.example.TheFit.diet.domain.Diet;
import com.example.TheFit.diet.dto.DietResDto;
import com.example.TheFit.sse.SseController;
import com.example.TheFit.totalworkouts.domain.TotalWorkOuts;
import com.example.TheFit.totalworkouts.repository.TotalWorkOutsRepository;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.member.repository.MemberRepository;
import com.example.TheFit.workout.domain.WorkOut;
import com.example.TheFit.workout.dto.WorkOutReqDto;
import com.example.TheFit.workout.dto.WorkOutResDto;
import com.example.TheFit.workout.dto.WorkOutUsingMemberResDto;
import com.example.TheFit.workout.mapper.WorkOutMapper;
import com.example.TheFit.workout.repository.WorkOutRepository;
import com.example.TheFit.workoutlist.domain.WorkOutList;
import com.example.TheFit.workoutlist.repository.WorkOutListRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class WorkOutService {
    private final WorkOutRepository workOutRepository;
    private final WorkOutListRepository workOutListRepository;
    private final TotalWorkOutsRepository totalWorkOutsRepository;
    private final MemberRepository memberRepository;
    private final SseController sseController;
    private final WorkOutMapper workOutMapper = WorkOutMapper.INSTANCE;

    @Autowired
    public WorkOutService(WorkOutRepository workOutRepository, WorkOutListRepository workOutListRepository, TotalWorkOutsRepository totalWorkOutsRepository
            , MemberRepository memberRepository, SseController sseController) {
        this.workOutRepository = workOutRepository;
        this.workOutListRepository = workOutListRepository;
        this.totalWorkOutsRepository = totalWorkOutsRepository;
        this.memberRepository = memberRepository;
        this.sseController = sseController;
    }

    public WorkOut create(WorkOutReqDto workOutReqDto) {
        TotalWorkOuts totalWorkOuts = totalWorkOutsRepository.findById(workOutReqDto.getTotalWorkOutsId()).orElseThrow(
                () -> new TheFitBizException(ErrorCode.NOT_FOUND_TOTALWORKOUT));
        WorkOutList workOutList = workOutListRepository.findById(workOutReqDto.getWorkOutListId()).orElseThrow(
                () -> new TheFitBizException(ErrorCode.NOT_FOUND_WORKOUTLIST));
        WorkOut workOut = workOutMapper.toEntity(totalWorkOuts, workOutList, workOutReqDto);
        return workOutRepository.save(workOut);
    }

    public List<WorkOutResDto> findAll() {
        List<WorkOut> workOuts = workOutRepository.findAll();
        List<WorkOutResDto> workOutResDtos = new ArrayList<>();
        for (WorkOut workOut : workOuts) {
            workOutResDtos.add(workOutMapper.toDto(workOut));
        }
        return workOutResDtos;
    }

    public WorkOutResDto findById(Long id) throws TheFitBizException {
        WorkOut workOut = workOutRepository.findById(id)
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_WORKOUT));
        WorkOutList workOutList = workOutListRepository.findById(workOut.getWorkOutList().getId()).orElseThrow();
        return workOutMapper.toDto(workOut);
    }

    public List<WorkOutUsingMemberResDto> findByMemberEmailAndWorkOutDate(String email, String inputDate) throws TheFitBizException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER));
        LocalDate date = LocalDate.parse(inputDate, DateTimeFormatter.ISO_LOCAL_DATE);
        WorkOutList workOutList = workOutListRepository.findByMemberIdAndWorkOutDate(member.getId(), date)
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_WORKOUTLIST));
        List<WorkOut> workOuts = workOutRepository.findAll();
        List<WorkOutUsingMemberResDto> workOutResDtos = new ArrayList<>();
        for (WorkOut workOut : workOuts) {
            if (workOutList.getId().equals(workOut.getWorkOutList().getId())) {
                TotalWorkOuts totalWorkOuts = totalWorkOutsRepository.findById(workOut.getTotalWorkOuts().getId())
                        .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_TOTALWORKOUT));
                WorkOutUsingMemberResDto workOutUsingMemberResDto = workOutMapper.toDtoUsingMember(workOut);
                workOutUsingMemberResDto.setWorkOutDate(workOutList.getWorkOutDate());
                workOutUsingMemberResDto.setName(totalWorkOuts.getName());
                workOutUsingMemberResDto.setTarget(totalWorkOuts.getTarget());
                workOutResDtos.add(workOutUsingMemberResDto);
            }
        }
        return workOutResDtos;
    }

    public WorkOut update(Long id, WorkOutReqDto workOutReqDto) {
        WorkOut workOutUpdate = workOutRepository.findById(id)
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_WORKOUT));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(
                ()-> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
        );
        workOutUpdate.update(workOutReqDto);
        sseController.sendToTrainer(member.getTrainer(),"운동 완료",workOutReqDto.getUpLoadDate(),member.getName());
        return workOutUpdate;
    }

    public void delete(Long id) {
        workOutRepository.deleteById(id);
    }


}