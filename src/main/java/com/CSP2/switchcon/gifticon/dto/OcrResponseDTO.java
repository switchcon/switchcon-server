package com.CSP2.switchcon.gifticon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@Builder(access = PRIVATE)
public class OcrResponseDTO {

    private final String productName;
    private final String expireDate;
    private final String orderNum;
    private final String storeName;
    private final String category;
    private final String barcodeNum;

}
