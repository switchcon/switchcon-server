package com.CSP2.switchcon.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "C_001", "서버에 문제가 발생하였습니다."),
    METHOD_NOT_ALLOWED(405, "C_002", "잘못된 HTTP Request Method 입니다."),
    INVALID_INPUT_VALUE(400, "C_003", "적절하지 않은 요청 값입니다."),
    INVALID_TYPE_VALUE(400, "C_004", "요청 값의 타입이 잘못되었습니다."),
    ENTITY_NOT_FOUND(400, "C_005", "지정한 Entity를 찾을 수 없습니다."),

    MEMBER_NOT_FOUND(404, "M_001", "존재하지 않는 사용자입니다."),
    DUPLICATED_USERNAME(409, "M_002", "이미 존재하는 아이디입니다."),
    INVALID_PASSWORD(401, "M_003", "비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN(401, "M_004", "토큰이 만료되었습니다."),
    TOO_LITTLE_COIN(403, "M_005", "교환 코인이 부족합니다."),

    INVALID_EXPIRE_DATE_FORMAT(401, "G_001", "올바르지 않은 날짜 형식입니다."),
    IMG_INFO_NOT_FOUND(401, "G_002", "올바르지 않은 기프티콘 이미지입니다."),
    GIFTICON_NOT_FOUND(404, "G_003", "기프티콘 정보를 찾을 수 없습니다."),
    INVALID_SORT_TYPE(401, "G_004", "올바르지 않은 기프티콘 정렬 타입입니다."),
    INACTIVE_GIFTION(401, "G_005", "비활성화 상태인 기프티콘입니다."),

    EXCHANGE_POST_NOT_FOUND(404, "EP_001", "교환 게시물을 찾을 수 없습니다."),
    FORBIDDEN_ACCESS_EXCHANGE_POST(403, "EP_002", "교환 게시물에 접근할 권한이 없습니다."),
    NOT_IN_PROGRESS(401, "EP_003", "진행 중이지 않은 교환 게시물입니다."),

    EXCHANGE_REQUEST_NOT_FOUND(404, "ER_001", "교환 요청을 찾을 수 없습니다."),
    FORBIDDEN_DELETE_EXCHANGE_REQUEST(403, "ER_002", "교환 요청을 삭제할 권한이 없습니다."),

    ERROR_IN_GIFTICON_SERVER(400, "GS_001", "기프티콘 서버에서 에러가 발생했습니다.");
    ;

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

}
