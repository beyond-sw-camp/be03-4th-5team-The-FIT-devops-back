package com.example.TheFit.oauth;

import com.example.TheFit.user.entity.Role;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.member.repository.MemberRepository;
import com.example.TheFit.user.trainer.domain.Trainer;
import com.example.TheFit.user.trainer.repository.TrainerRepository;
import io.jsonwebtoken.impl.Base64UrlCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;


@Service
public class OAuthService {
    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String LOGIN_REDIRECT_URL;

    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final TrainerRepository trainerRepository;

    public OAuthService(MemberRepository memberRepository, TrainerRepository trainerRepository) {
        this.memberRepository = memberRepository;
        this.trainerRepository = trainerRepository;
    }

    public ResponseEntity<String> getGoogleAccessToken(String accessCode) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("code", Collections.singletonList(accessCode));
        params.put("client_id", Collections.singletonList(GOOGLE_CLIENT_ID));
        params.put("client_secret", Collections.singletonList(GOOGLE_CLIENT_SECRET));
        params.put("redirect_uri", Collections.singletonList(LOGIN_REDIRECT_URL));
        params.put("grant_type", Collections.singletonList("authorization_code"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(GOOGLE_TOKEN_URL, HttpMethod.POST, entity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        return null;
    }

    public String decryptBase64UrlToken(String jwtToken) {
        byte[] decode = new Base64UrlCodec().decode(jwtToken);
        return new String(decode, StandardCharsets.UTF_8);
    }

    public String findRole(String email) {
        if(memberRepository.findByEmail(email).isPresent()){
            Member member = memberRepository.findByEmail(email).get();
            return "MEMBER";
        }
        else if(trainerRepository.findByEmail(email).isPresent()){
            Trainer trainer = trainerRepository.findByEmail(email).get();
            return "TRAINER";
        }
        return "ADMIN";
    }
}



