package com.CSP2.switchcon.exchange.domain;

public enum ExchangeStatus {
    PROGRESS,   // 교환게시물 진행 중
    COMPLETE,   // 교환게시물 완료
    CANCEL,     // 교환게시물 취소
    WAITING,    // 교환요청 대기 중
    ACCEPTED,   // 교환요청 수락 됨
    REJECTED    // 교환요청 거절 됨
}
