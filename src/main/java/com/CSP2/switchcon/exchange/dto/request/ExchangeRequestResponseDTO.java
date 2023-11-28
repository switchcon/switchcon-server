package com.CSP2.switchcon.exchange.dto.request;

import com.CSP2.switchcon.exchange.domain.ExchangePost;
import com.CSP2.switchcon.exchange.domain.ExchangeRequest;
import com.CSP2.switchcon.exchange.dto.post.AddExchangePostReponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@Builder(access = PRIVATE)
public class ExchangeRequestResponseDTO {

    private final Long exchangePostId;

    public static ExchangeRequestResponseDTO from (ExchangeRequest exchangeRequest) {
        return ExchangeRequestResponseDTO.builder()
                .exchangePostId(exchangeRequest.getExchangePost().getId())
                .build();
    }
}
