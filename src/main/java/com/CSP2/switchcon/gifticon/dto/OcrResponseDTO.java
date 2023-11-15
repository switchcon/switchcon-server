package com.CSP2.switchcon.gifticon.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OcrResponseDTO {

    private String productName;

    private String expireDate;

    private String orderNum;

    private String storeName;

    private String category;

    private String barcodeNum;
}