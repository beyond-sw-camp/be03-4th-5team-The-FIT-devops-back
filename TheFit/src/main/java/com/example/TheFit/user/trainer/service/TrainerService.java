package com.example.TheFit.user.trainer.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.TheFit.common.ErrorCode;
import com.example.TheFit.common.TheFitBizException;
import com.example.TheFit.user.dto.UserIdPassword;
import com.example.TheFit.user.entity.Role;
import com.example.TheFit.user.mapper.UserMapper;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.member.dto.MemberResDto;
import com.example.TheFit.user.member.repository.MemberRepository;
import com.example.TheFit.user.repo.UserRepository;
import com.example.TheFit.user.trainer.domain.Trainer;
import com.example.TheFit.user.trainer.dto.TrainerReqDto;
import com.example.TheFit.user.trainer.dto.TrainerResDto;
import com.example.TheFit.user.trainer.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final UserRepository userRepository;
    private final AmazonS3Client amazonS3Client;
    private final MemberRepository memberRepository;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository, UserRepository userRepository, AmazonS3Client amazonS3Client, MemberRepository memberRepository) {
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.amazonS3Client = amazonS3Client;
        this.memberRepository = memberRepository;
    }
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public Trainer create(TrainerReqDto trainerReqDto) throws TheFitBizException {
        if(userRepository.findByEmail(trainerReqDto.getEmail()).isPresent()){
            throw new TheFitBizException(ErrorCode.ID_DUPLICATE);
        }
        String fileName =trainerReqDto.getEmail()+"_profile";
        String fileUrl = null;
        try {
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(trainerReqDto.getProfileImage().getContentType());
            metadata.setContentLength(trainerReqDto.getProfileImage().getSize());
            amazonS3Client.putObject(bucket,fileName,trainerReqDto.getProfileImage().getInputStream(),metadata);
            fileUrl = amazonS3Client.getUrl(bucket,fileName).toString();
        } catch (IOException e) {
            throw new TheFitBizException(ErrorCode.S3_SERVER_ERROR);
        }
        Trainer trainer = userMapper.toEntity(fileUrl,trainerReqDto);
        userRepository.save(new UserIdPassword(trainer.email,trainer.password,trainer.name,Role.TRAINER));
        return trainerRepository.save(trainer);
    }

    public List<TrainerResDto> findAll() {
        return trainerRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public Trainer update(TrainerReqDto trainerReqDto) throws TheFitBizException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Trainer trainer = trainerRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_TRAINER));
        String fileName =trainerReqDto.getEmail()+"_profile";
        amazonS3Client.deleteObject(bucket,fileName);
        String fileUrl = null;
        try {
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(trainerReqDto.getProfileImage().getContentType());
            metadata.setContentLength(trainerReqDto.getProfileImage().getSize());
            amazonS3Client.putObject(bucket,fileName,trainerReqDto.getProfileImage().getInputStream(),metadata);
            fileUrl = amazonS3Client.getUrl(bucket,fileName).toString();
        } catch (IOException e) {
            throw new TheFitBizException(ErrorCode.S3_SERVER_ERROR);
        }
        trainer.update(trainerReqDto,fileUrl);
        return trainer;
    }

    public void delete(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_TRAINER));
        trainer.delete();
        UserIdPassword userIdPassword =userRepository.findByEmail(trainer.email).orElseThrow(
                ()->new TheFitBizException(ErrorCode.NOT_FOUND_TRAINER)
        );
        userIdPassword.delete();
    }

    public TrainerResDto findTrainer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().contains((new SimpleGrantedAuthority("ROLE_TRAINER")))){
            Trainer trainer = trainerRepository.findByEmail(authentication.getName()).orElseThrow(
                    ()->new TheFitBizException(ErrorCode.NOT_FOUND_TRAINER)
            );
            return userMapper.toDto(trainer);
        }
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(
                ()->new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
        );
        return userMapper.toDto(member.getTrainer());
    }

    public TrainerResDto findMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Trainer trainer = trainerRepository.findByEmail(authentication.getName()).orElseThrow(()->new TheFitBizException(ErrorCode.NOT_FOUND_TRAINER));
        return userMapper.toDto(trainer);
    }

    public List<MemberResDto> findMyMembers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Trainer trainer = trainerRepository.findByEmail(authentication.getName()).orElseThrow(()->new TheFitBizException(ErrorCode.NOT_FOUND_TRAINER));
        List<Member> members = memberRepository.findAllByTrainerId(trainer.getId());
        List<MemberResDto> memberResDtos = new ArrayList<>();
        for(Member member :members){
            memberResDtos.add(userMapper.toDto(member));
        }
        return memberResDtos;
    }

    public List<TrainerResDto> findAvailable() {
        return trainerRepository.findAll().stream()
                .filter(a->a.delYn.equals("N"))
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}