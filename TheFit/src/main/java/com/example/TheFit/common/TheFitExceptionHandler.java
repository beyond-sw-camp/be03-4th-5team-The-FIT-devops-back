package com.example.TheFit.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.rmi.MarshalException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class TheFitExceptionHandler {

    @ExceptionHandler(TheFitBizException.class)
    public ResponseEntity<Map<String,String>> theFitBizException(TheFitBizException e){
        Map<String,String> body = new HashMap<>();
        body.put("message",e.getMessage());
        body.put("status",String.valueOf(e.getStatus()));
        body.put("code",e.getCode());
        return new ResponseEntity<>(body,HttpStatus.valueOf(e.getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> validException(MethodArgumentNotValidException e){
        Map<String,String> body = new HashMap<>();
        body.put("message",e.getBindingResult().getFieldError().getDefaultMessage());
        body.put("status",HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("code","400");
        return new ResponseEntity<>(body,HttpStatus.valueOf(400));
    }
}
