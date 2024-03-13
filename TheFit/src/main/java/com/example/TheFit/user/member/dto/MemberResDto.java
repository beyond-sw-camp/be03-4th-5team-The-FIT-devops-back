package com.example.TheFit.user.member.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResDto{
    private Long id;
    private Long trainerId;
    private String name;
    private String email;
    private int cmHeight;
    private int kgWeight;
    private String gender;
    private String role;
    private String profileImage;
    private String phoneNumber;
    public String delYn;
}
