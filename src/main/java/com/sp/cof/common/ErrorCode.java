package com.sp.cof.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_FOUND_HANDLER(90000, "Not Found Handler", "잘못된 요청입니다."),
    ETC(99999, "Unknown Error", "알 수 없는 오류");

    private final int code;
    private final String message;
    private final String desc;
}
