package com.CSP2.switchcon.exchange.dto;

import com.CSP2.switchcon.gifticon.domain.Gifticon;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@Builder(access = PRIVATE)
public class ExchangePostResponseDTO {

    private final String gifticonImg;

    private final String category;

    private final String store;

    private final String product;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd",
            locale = "Asia/Seoul"
    )
    private final LocalDate expireDate;

    @JsonSerialize(using = ToStringSerializer.class)
    private final long price;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd",
            locale = "Asia/Seoul"
    )
    private final LocalDateTime createdAt;

    private final String preference;

    public static ExchangePostResponseDTO from (Gifticon gifticon, String preference, LocalDateTime createdAt) {
        return ExchangePostResponseDTO.builder()
                .gifticonImg(gifticon.getGifticonImg())
                .category(gifticon.getCategory())
                .store(gifticon.getStore())
                .product(gifticon.getProduct())
                .expireDate(gifticon.getExpireDate())
                .price(gifticon.getPrice())
                .preference(preference)
                .createdAt(createdAt)
                .build();
    }

}
