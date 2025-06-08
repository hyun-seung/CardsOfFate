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
    INVALID_PLAYER_CARDS(10005, "Invalid Player Cards", "플레이어 카드가 유효하지 않습니다."),

    // 적 전투 관련 에러
    INVALID_GAME_STATE(11001, "Invalid Game State", "게임 상태가 유효하지 않습니다."),
    INVALID_ROUND(11002, "Invalid Round", "유효하지 않은 라운드입니다."),
    INVALID_ENEMY_INFO(11003, "Invalid Enemy Info", "적 정보가 유효하지 않습니다."),

    // 히스토리 관련 에러
    INVALID_GAME_ID(12001, "Invalid Game ID", "게임 ID가 유효하지 않습니다."),
    INVALID_HAND_FOR_HISTORY(12002, "Invalid Hand For History", "히스토리 기록용 손패가 유효하지 않습니다."),
    INVALID_JSON_STRING(12003, "Invalid JSON String", "유효하지 않은 JSON 문자열입니다."),
    JSON_SERIALIZATION_FAILED(12004, "JSON Serialization Failed", "JSON 직렬화에 실패했습니다."),
    JSON_DESERIALIZATION_FAILED(12005, "JSON Deserialization Failed", "JSON 역직렬화에 실패했습니다."),
    INVALID_CARDS_FOR_JSON(12006, "Invalid Cards For JSON", "JSON 변환할 카드가 유효하지 않습니다."),
    HISTORY_NOT_FOUND(12007, "History Not Found", "게임 히스토리를 찾을 수 없습니다."),

    // 글로벌 에러
    NOT_FOUND_HANDLER(90000, "Not Found Handler", "잘못된 요청입니다."),
    ETC(99999, "Unknown Error", "알 수 없는 오류");

    private final int code;
    private final String message;
    private final String desc;
}
