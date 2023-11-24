package com.CSP2.switchcon.gifticon.dto;

import com.CSP2.switchcon.gifticon.domain.Gifticon;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@Builder(access = PRIVATE)
public class GifticonResponseDTO {

    private final long id;

    private final String gifticonImg;

    private final String category;

    private final String store;

    private final String product;

    private final String barcodeNum;

    private final String orderNum;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd",
            locale = "Asia/Seoul"
    )
    private final LocalDate expireDate;

    @JsonSerialize(using = ToStringSerializer.class)
    private final long price;

    private final boolean isUsed;

    private final boolean isActive;

    public static GifticonResponseDTO from (Gifticon gifticon) {
        return GifticonResponseDTO.builder()
                .id(gifticon.getId())
                .gifticonImg(gifticon.getGifticonImg())
                .category(gifticon.getCategory())
                .store(gifticon.getStore())
                .product(gifticon.getProduct())
                .barcodeNum(gifticon.getBarcodeNum())
                .orderNum(gifticon.getOrderNum())
                .expireDate(gifticon.getExpireDate())
                .price(gifticon.getPrice())
                .isUsed(gifticon.isUsed())
                .isActive(gifticon.isActive())
                .build();
    }

    public static GifticonResponseDTO of (String gifticonImg, String category, String store, String product, String barcodeNum,
                                          String orderNum, LocalDate expireDate, long price, boolean isUsed) {
        return GifticonResponseDTO.builder()
                .gifticonImg(gifticonImg)
                .category(category)
                .store(store)
                .product(product)
                .barcodeNum(barcodeNum)
                .orderNum(orderNum)
                .expireDate(expireDate)
                .price(price)
                .isUsed(isUsed)
                .build();
    }

}
