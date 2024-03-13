package com.example.TheFit.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    ID_DUPLICATE(400,"L1","중복된 이메일입니다"),
    INCORRECT_ID(400,"L2","존재하지 않는 아이디 입니다."),
    INCORRECT_PASSWORD(400,"L3","틀린 비밀번호 입니다."),
    LEAVE_MEMBER(400,"L3","탈퇴한 멤버입니다."),
    NOT_FOUND_MEMBER(400,"D1","해당 멤버가 없습니다."),
    NOT_FOUND_TRAINER(400,"D2","해당 트레이너가 없습니다."),
    NOT_FOUND_CAREER(400,"D3", "해당 커리어가 없습니다."),
    NOT_FOUND_DIET(400,"D4","해당 식단정보가 없습니다"),
    NOT_FOUND_TOTALWORKOUT(400,"D5","해당 TotalWorkout 정보가 없습니다"),
    NOT_FOUND_WORKOUT(400,"D6","해당 WorkOut 정보가 없습니다"),
    NOT_FOUND_WORKOUTLIST(400,"D7","해당 WorkOutList 정보가 없습니다."),
    NOT_FOUND_DIET_FEEDBACK(400,"D8" ,"해당 DietFeedback 정보가 없습니다."),
    NOT_FOUND_WORKOUT_FEEDBACK(400,"D8" ,"해당 WorkoutFeedback 정보가 없습니다." ),
    FEEDBACK_DATE_DUPLICATE(400,"D9","이미 오늘 피드백을 작성했습니다."),
    S3_SERVER_ERROR(500,"S1","S3 서버와 연결 할 수 없습니다.");

    private int status;
    private String code;
    private String message;

    ErrorCode(int status,String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
