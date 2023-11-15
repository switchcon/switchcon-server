package com.CSP2.switchcon.gifticon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@Builder(access = PRIVATE)
public class OcrResponseDTO {

    private final String expireDate;
    private final String orderNum;
    private final String storeName;
    private final String barcodeNum;
    private final String product;
    private final String category;
    //Todo::ocr 서버 업데이터 되면 수정
}
