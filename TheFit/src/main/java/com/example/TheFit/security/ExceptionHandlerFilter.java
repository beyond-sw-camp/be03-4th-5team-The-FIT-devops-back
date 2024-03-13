package com.example.TheFit.security;

import com.example.TheFit.login.TmpResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        }catch (ExpiredJwtException e){
            expiredTokenResponse(response,e);
        }
    }
    // 프론트 개밯 후 develop 이 필요해 보임
    private static void expiredTokenResponse(HttpServletResponse response, Exception e) throws IOException {
        response.setHeader("Content-type","application/json");
        response.setCharacterEncoding("utf-8");
        Map<String,Object> body = new HashMap<>();
        body.put("status",String.valueOf(HttpStatus.SEE_OTHER.value()));
        body.put("error_message",e.getMessage());
        ResponseEntity<TmpResponse> responseResponseEntity = new ResponseEntity<>(
                new TmpResponse(HttpStatus.SEE_OTHER,"토큰 재발급 검증이 필요합니다.",body)
                ,HttpStatus.SEE_OTHER);
        response.getWriter().write(responseResponseEntity.toString());
    }
}
