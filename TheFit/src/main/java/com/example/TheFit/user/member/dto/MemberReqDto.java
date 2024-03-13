package com.example.TheFit.user.member.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
public class MemberReqDto {
    private Long trainerId;
    private String name;
    private String email;
    private String password;
    private int cmHeight;
    private int kgWeight;
    private String gender;
    private String role;
    private MultipartFile profileImage;
    private String phoneNumber;
}
