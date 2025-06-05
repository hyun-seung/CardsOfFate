package com.sp.cof.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 카드 관련 에러
    INVALID_CURRENT_HAND(10001, "Invalid Current Hand", "현재 손패가 유효하지 않습니다."),
    INVALID_USED_CARDS(10002, "Invalid Used Cards", "사용할 카드가 유효하지 않습니다."),
    CARDS_NOT_IN_HAND(10003, "Cards Not In Hand", "현재 손패에 없는 카드를 사용할 수 없습니다."),

    // 사용자 에러

    // 글로벌 에러
    NOT_FOUND_HANDLER(90000, "Not Found Handler", "잘못된 요청입니다."),
    ETC(99999, "Unknown Error", "알 수 없는 오류");

    private final int code;
    private final String message;
    private final String desc;
}
