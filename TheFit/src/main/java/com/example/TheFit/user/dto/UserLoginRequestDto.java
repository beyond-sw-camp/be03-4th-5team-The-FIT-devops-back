package com.example.TheFit.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginRequestDto {
    private String email;
    private String password;
}
