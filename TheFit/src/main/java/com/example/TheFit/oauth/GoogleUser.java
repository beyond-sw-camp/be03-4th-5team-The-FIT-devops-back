package com.example.TheFit.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GoogleUser {
    public String name;
    public String email;
    public String picture;
}
