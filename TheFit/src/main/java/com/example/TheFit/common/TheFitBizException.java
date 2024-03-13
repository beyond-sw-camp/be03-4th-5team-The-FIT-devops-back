package com.example.TheFit.common;

public class TheFitBizException extends RuntimeException {

    private ErrorCode errorCode;

    public TheFitBizException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public String getMessage(){
        return this.errorCode.getMessage();
    }
    public int getStatus(){
        return this.errorCode.getStatus();
    }
    public String getCode(){
        return this.errorCode.getCode();
    }
}
