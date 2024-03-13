package com.example.TheFit.security;

import com.example.TheFit.oauth.OAuth2MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private final JwtAuthFilter jwtAuthFilter;
    @Autowired
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    @Autowired
    private final OAuth2MemberService oAuth2MemberService;
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, ExceptionHandlerFilter exceptionHandlerFilter
    ,OAuth2MemberService oAuth2MemberService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.exceptionHandlerFilter = exceptionHandlerFilter;
        this.oAuth2MemberService = oAuth2MemberService;

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .cors().and()
                .httpBasic().disable()
                .authorizeRequests()
                    .antMatchers("/member/create","/dologin",
                            "/trainer/create","/reCreateAccessToken",
                            "/auth/google/callback", "/trainer/available/list","/notifications/**")
                    .permitAll()
                .anyRequest().authenticated()
                .and().oauth2Login()
                .loginPage("/dologin") //로그인이 필요한데 로그인을 하지 않았다면 이동할 uri 설정 -> 프론트 화면 구성 이후 변경!
                .defaultSuccessUrl("/") //OAuth 구글 로그인이 성공하면 이동할 uri 설정
                .userInfoEndpoint()//로그인 완료 후 회원 정보 받기
                .userService(oAuth2MemberService)
                .and()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(exceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
