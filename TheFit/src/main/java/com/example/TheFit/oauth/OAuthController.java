package com.example.TheFit.oauth;

import com.example.TheFit.security.TokenService;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.member.dto.MemberReqDto;
import com.example.TheFit.user.member.repository.MemberRepository;
import com.example.TheFit.user.member.service.MemberService;
import com.example.TheFit.user.repo.UserRepository;
import com.example.TheFit.user.trainer.repository.TrainerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OAuthController {
    @Autowired
    private final OAuthService oAuthService;
    @Autowired
    private final ObjectMapper objectMapper;
    @Autowired
    private final TokenService tokenService;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final TrainerRepository trainerRepository;

    public OAuthController(OAuthService oAuthService, ObjectMapper objectMapper,
                           TokenService tokenService, MemberRepository memberRepository,
                           TrainerRepository trainerRepository) {
        this.oAuthService = oAuthService;
        this.objectMapper = objectMapper;
        this.tokenService = tokenService;
        this.memberRepository = memberRepository;
        this.trainerRepository = trainerRepository;
    }

    public GoogleUser getUserInfo(String userInfoResponse) throws JsonProcessingException {
        GoogleUser googleUser = objectMapper.readValue(userInfoResponse, GoogleUser.class);
        return googleUser;
    }

    @GetMapping("/auth/google/callback")
    public RedirectView successGoogleLogin(@RequestParam("code") String accessCode) throws ParseException, JsonProcessingException {
        System.out.println("SUCESS!!!!");
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(oAuthService.getGoogleAccessToken(accessCode).getBody());
        String idToken = (String) jsonObject.get("id_token");
        Map<String, Object> oAuthMemberInfo = new HashMap<>();
        GoogleUser googleUser = getUserInfo(oAuthService.decryptBase64UrlToken(idToken.split("\\.")[1]));
        oAuthMemberInfo.put("email", googleUser.getEmail());
        oAuthMemberInfo.put("name", googleUser.getName());
        Boolean memberExists = memberRepository.findByEmail(googleUser.getEmail()).isPresent();
        Boolean trainerExists = trainerRepository.findByEmail(googleUser.getEmail()).isPresent();
        System.out.println(idToken);
        if (!memberExists && !trainerExists) {
            String signupUrl = "http://thefitvue.s3-website.ap-northeast-2.amazonaws.com/signupoauth/?email=" + googleUser.getEmail();
            System.out.println(signupUrl);
            return new RedirectView(signupUrl);
        }
        String role = oAuthService.findRole(googleUser.getEmail());
        String accessToken = tokenService.createAccessToken(googleUser.email, googleUser.getName(), role);
        String refreshToken = tokenService.createRefreshToken(googleUser.email);
        String email = googleUser.email;
        String loginUrl = "http://localhost:8080/loginSuccess/?accessToken=" + accessToken
                + "&refreshToken=" + refreshToken
                + "&role=" + role
                + "&email=" + email;
        return new RedirectView(loginUrl);
    }
}
