package com.example.TheFit.user.member.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.TheFit.common.ErrorCode;
import com.example.TheFit.common.TheFitBizException;
import com.example.TheFit.user.dto.UserIdPassword;
import com.example.TheFit.user.entity.Role;
import com.example.TheFit.user.mapper.UserMapper;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.member.dto.MemberReqDto;
import com.example.TheFit.user.member.dto.MemberResDto;
import com.example.TheFit.user.member.repository.MemberRepository;
import com.example.TheFit.user.repo.UserRepository;
import com.example.TheFit.user.trainer.domain.Trainer;
import com.example.TheFit.user.trainer.dto.TrainerResDto;
import com.example.TheFit.user.trainer.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {
    @Autowired
    private final MemberRepository memberRepository;
    private final UserMapper userMapper;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public MemberService(MemberRepository memberRepository, UserMapper userMapper, TrainerRepository trainerRepository, UserRepository userRepository, AmazonS3Client amazonS3Client) {
        this.memberRepository = memberRepository;
        this.userMapper = userMapper;
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.amazonS3Client = amazonS3Client;
    }

    public Member create(MemberReqDto memberReqDto) throws TheFitBizException {
        if(userRepository.findByEmail(memberReqDto.getEmail()).isPresent()){
            throw new TheFitBizException(ErrorCode.ID_DUPLICATE);
        }
        String fileName = memberReqDto.getEmail()+"_profile";
        String fileUrl = null;
        try {
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(memberReqDto.getProfileImage().getContentType());
            metadata.setContentLength(memberReqDto.getProfileImage().getSize());
            amazonS3Client.putObject(bucket,fileName,memberReqDto.getProfileImage().getInputStream(),metadata);
            fileUrl = amazonS3Client.getUrl(bucket,fileName).toString();
        } catch (IOException e) {
            throw new TheFitBizException(ErrorCode.S3_SERVER_ERROR);
        }
        Trainer trainer = trainerRepository.findById(memberReqDto.getTrainerId())
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_TRAINER));
        Member member = userMapper.toEntity(fileUrl,memberReqDto,trainer);
        Role role = Role.MEMBER;
        if(memberReqDto.getRole().equals("ADMIN")){
            role = Role.ADMIN;
        }
        userRepository.save(new UserIdPassword(memberReqDto.getEmail(),memberReqDto.getPassword(),memberReqDto.getName(),role));
        return  memberRepository.save(member);
    }

    public List<MemberResDto> findAll() {
        trainerRepository.findAll();
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
    public Member update(MemberReqDto memberReqDto) throws TheFitBizException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER));
        String fileName =memberReqDto.getEmail()+"_profile";
        amazonS3Client.deleteObject(bucket,fileName);
        String fileUrl = null;
        try {
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(memberReqDto.getProfileImage().getContentType());
            metadata.setContentLength(memberReqDto.getProfileImage().getSize());
            amazonS3Client.putObject(bucket,fileName,memberReqDto.getProfileImage().getInputStream(),metadata);
            fileUrl = amazonS3Client.getUrl(bucket,fileName).toString();
        } catch (IOException e) {
            throw new TheFitBizException(ErrorCode.S3_SERVER_ERROR);
        }
        Trainer trainer = trainerRepository.findById(member.getTrainer().getId())
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_TRAINER));
        member.update(memberReqDto,trainer,fileUrl);
        return member;
    }

    public void delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER));
        member.delete();
        UserIdPassword userIdPassword = userRepository.findByEmail(member.email).orElseThrow(
                ()-> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
        );
        userIdPassword.delYn = "Y";
    }

    public MemberResDto findMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER));
        return userMapper.toDto(member);
    }

    public TrainerResDto findMyTrainer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(()->new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER));
        Trainer trainer = member.getTrainer();
        return userMapper.toDto(trainer);
    }

    @Transactional
    public Member updateMyTrainer(Long trainerId) {
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(
                ()-> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER)
                );
        Trainer trainer = trainerRepository.findById(trainerId).orElseThrow(
                ()->new TheFitBizException(ErrorCode.NOT_FOUND_TRAINER)
                );
        member.updateTrainer(trainer);
        return member;
    }
}