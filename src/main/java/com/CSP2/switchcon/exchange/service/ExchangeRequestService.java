package com.CSP2.switchcon.exchange.service;

import com.CSP2.switchcon.common.exception.BusinessException;
import com.CSP2.switchcon.common.exception.EntityNotFoundException;
import com.CSP2.switchcon.common.exception.ErrorCode;
import com.CSP2.switchcon.exchange.domain.ExchangePost;
import com.CSP2.switchcon.exchange.domain.ExchangeRequest;
import com.CSP2.switchcon.exchange.dto.request.ExchangeRequestResponseDTO;
import com.CSP2.switchcon.exchange.repository.ExchangePostRepository;
import com.CSP2.switchcon.exchange.repository.ExchangeRequestRepository;
import com.CSP2.switchcon.gifticon.domain.Gifticon;
import com.CSP2.switchcon.gifticon.repository.GifticonRepository;
import com.CSP2.switchcon.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.CSP2.switchcon.exchange.domain.ExchangeStatus.PROGRESS;
import static com.CSP2.switchcon.exchange.domain.ExchangeStatus.WAITING;

@Service
@RequiredArgsConstructor
public class ExchangeRequestService {

    private final GifticonRepository gifticonRepository;
    private final ExchangePostRepository exchangePostRepository;
    private final ExchangeRequestRepository exchangeRequestRepository;

    @Transactional
    public ExchangeRequestResponseDTO addExchangeRequest(Member member, long exchangePostId, long gifticonId) {
        Gifticon gifticon = gifticonRepository.findByIdAndMember(member, gifticonId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.GIFTICON_NOT_FOUND));

        if (gifticon.isActive() == false || gifticon.isUsed() == true)
            throw new BusinessException(ErrorCode.INACTIVE_GIFTION);

        ExchangePost exchangePost = exchangePostRepository.findById(exchangePostId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.EXCHANGE_POST_NOT_FOUND));

        if (exchangePost.getStatus() != PROGRESS)
            throw new BusinessException(ErrorCode.NOT_IN_PROGRESS);

        ExchangeRequest exchangeRequest = ExchangeRequest.builder()
                .status(WAITING)
                .gifticon(gifticon)
                .exchangePost(exchangePost)
                .build();

        ExchangeRequest saved = exchangeRequestRepository.save(exchangeRequest);
        gifticon.updateActive(false);

        return ExchangeRequestResponseDTO.from(saved);
    }
}
