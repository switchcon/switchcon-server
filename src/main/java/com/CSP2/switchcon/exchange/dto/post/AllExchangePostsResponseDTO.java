package com.CSP2.switchcon.exchange.dto.post;

import com.CSP2.switchcon.exchange.domain.ExchangePost;
import com.CSP2.switchcon.exchange.domain.ExchangeStatus;
import com.CSP2.switchcon.gifticon.domain.Gifticon;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@Builder(access = PRIVATE)
public class AllExchangePostsResponseDTO {

    private final long exchangePostId;

    private final String gifticonImg;

    private final String category;

    private final String store;

    private final String product;

    @JsonSerialize(using = ToStringSerializer.class)
    private final long price;

    private final ExchangeStatus status;

    private final int requestCnt;

    public static AllExchangePostsResponseDTO from (Gifticon gifticon, ExchangePost exchangePost, int requestCnt) {
        return AllExchangePostsResponseDTO.builder()
                .exchangePostId(exchangePost.getId())
                .gifticonImg(gifticon.getGifticonImg())
                .category(gifticon.getCategory())
                .store(gifticon.getStore())
                .product(gifticon.getProduct())
                .price(gifticon.getPrice())
                .status(exchangePost.getStatus())
                .requestCnt(requestCnt)
                .build();
    }
}
