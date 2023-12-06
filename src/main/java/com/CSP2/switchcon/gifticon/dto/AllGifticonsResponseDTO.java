package com.CSP2.switchcon.gifticon.dto;

import com.CSP2.switchcon.gifticon.domain.Gifticon;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@Builder(access = PRIVATE)
public class AllGifticonsResponseDTO {

    private final Long gifticonId;

    private final String gifticonImg;

    private final String category;

    private final String store;

    private final String product;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy.MM.dd",
            locale = "Asia/Seoul"
    )
    private final LocalDate expireDate;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private final long price;

    private final boolean isUsed;

    private final boolean isActive;

    public static AllGifticonsResponseDTO from (Gifticon gifticon) {
        return AllGifticonsResponseDTO.builder()
                .gifticonId(gifticon.getId())
                .gifticonImg(gifticon.getGifticonImg())
                .category(gifticon.getCategory())
                .store(gifticon.getStore())
                .product(gifticon.getProduct())
                .expireDate(gifticon.getExpireDate())
                .price(gifticon.getPrice())
                .isUsed(gifticon.isUsed())
                .isActive(gifticon.isActive())
                .build();
    }
}
