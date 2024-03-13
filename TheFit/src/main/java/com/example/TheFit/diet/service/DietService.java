package com.example.TheFit.diet.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.TheFit.common.ErrorCode;
import com.example.TheFit.common.TheFitBizException;
import com.example.TheFit.diet.domain.Diet;
import com.example.TheFit.diet.dto.DietReqDto;
import com.example.TheFit.diet.dto.DietResDto;
import com.example.TheFit.diet.mapper.DietMapper;
import com.example.TheFit.diet.repository.DietRepository;
import com.example.TheFit.sse.SseController;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.member.repository.MemberRepository;
import com.example.TheFit.user.trainer.domain.Trainer;
import com.example.TheFit.user.trainer.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DietService {

    private final TrainerRepository trainerRepository;
    private final DietRepository dietRepository;
    private final MemberRepository memberRepository;
    private final DietMapper dietMapper;
    private final AmazonS3Client amazonS3Client;
    private final SseController sseController;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    public DietService(TrainerRepository trainerRepository, DietRepository dietRepository, MemberRepository memberRepository, DietMapper dietMapper, AmazonS3Client amazonS3Client, SseController sseController) {
        this.trainerRepository = trainerRepository;
        this.dietRepository = dietRepository;
        this.memberRepository = memberRepository;
        this.dietMapper = dietMapper;
        this.amazonS3Client = amazonS3Client;
        this.sseController = sseController;
    }

    public Diet create(DietReqDto dietReqDto) throws TheFitBizException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String fileName = "diet_"+authentication.getName()+"_"+dietReqDto.getDietDate()+"_"+dietReqDto.getType();
        String fileUrl = null;
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(dietReqDto.getImage().getContentType());
            metadata.setContentLength(dietReqDto.getImage().getSize());
            amazonS3Client.putObject(bucket, fileName, dietReqDto.getImage().getInputStream(), metadata);
            fileUrl = amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new TheFitBizException(ErrorCode.S3_SERVER_ERROR);
        }
        Member member = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER));
        Diet diet = dietMapper.toEntity(member, fileUrl, dietReqDto);
        sseController.sendToTrainer(member.getTrainer(),"diet",dietReqDto.getDietDate(),member.name);
        return dietRepository.save(diet);
    }

    public DietResDto findById(Long id) throws TheFitBizException {
        Diet diet = dietRepository.findById(id)
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_DIET));
        Member member = memberRepository.findById(diet.getMember().getId()).orElseThrow();
        return dietMapper.toDto(member, diet);
    }


    public Diet update(Long id, DietReqDto dietReqDto) throws TheFitBizException {
        Diet diet = dietRepository.findById(id)
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_DIET));
        diet.update(dietReqDto);
        return diet;
    }

    public void delete(Long id) {
        dietRepository.deleteById(id);
    }

    public List<DietResDto> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = memberRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
        ).getId();
        List<Diet> diets = dietRepository.findByMemberId(memberId);
        List<DietResDto> dietResDtos = new ArrayList<>();
        for (Diet diet : diets) {
            dietResDtos.add(dietMapper.toDto(diet.getMember(), diet));
        }
        return dietResDtos;
    }

    public List<DietResDto> findByEmail(String email) {
        Long memberId = memberRepository.findByEmail(email).orElseThrow(
                () -> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
        ).getId();
        List<Diet> diets = dietRepository.findByMemberId(memberId);
        List<DietResDto> dietResDtos = new ArrayList<>();
        for (Diet diet : diets) {
            dietResDtos.add(dietMapper.toDto(diet.getMember(), diet));
        }
        return dietResDtos;
    }

    public List<DietResDto> findByMemberEmailAndDietDate(String email, String inputDate) {
        Long memberId = memberRepository.findByEmail(email).orElseThrow(
                () -> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
        ).getId();
        LocalDate date = LocalDate.parse(inputDate, DateTimeFormatter.ISO_LOCAL_DATE);
        List<DietResDto> dietResDtos = new ArrayList<>();
        for (Diet diet : dietRepository.findByMemberIdAndDietDate(memberId, date)) {
            dietResDtos.add(dietMapper.toDto(diet.getMember(), diet));
        }
        return dietResDtos;
    }
}