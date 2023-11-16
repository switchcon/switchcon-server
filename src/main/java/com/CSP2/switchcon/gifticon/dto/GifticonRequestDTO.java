package com.CSP2.switchcon.gifticon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GifticonRequestDTO {

    private String gifticonImg;

    private String category;

    private String store;

    private String product;

    private String barcodeNum;

    private String orderNum;

    private LocalDate expireDate;

    private long price;

    private boolean isUsed;
}
