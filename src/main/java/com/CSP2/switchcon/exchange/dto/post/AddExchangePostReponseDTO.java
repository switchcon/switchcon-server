package com.CSP2.switchcon.exchange.dto.post;

import com.CSP2.switchcon.exchange.domain.ExchangePost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@Builder(access = PRIVATE)
public class AddExchangePostReponseDTO {

    private final Long exchangePostId;

    public static AddExchangePostReponseDTO from (ExchangePost exchangePost) {
        return AddExchangePostReponseDTO.builder()
                .exchangePostId(exchangePost.getId())
                .build();
    }
}
