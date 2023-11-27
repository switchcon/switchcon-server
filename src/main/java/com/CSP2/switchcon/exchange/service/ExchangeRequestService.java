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

import java.util.List;

import static com.CSP2.switchcon.exchange.domain.ExchangeStatus.*;

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

        if (member.getExchangeCoin() < 1)
            throw new BusinessException(ErrorCode.TOO_LITTLE_COIN);

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

    @Transactional
    public void delExchangeRequest(Member member, long exchangePostId, long exchangeRequestId) {
        ExchangeRequest exchangeRequest = exchangeRequestRepository.findById(exchangeRequestId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.EXCHANGE_REQUEST_NOT_FOUND));

        if (!exchangeRequest.getGifticon().getMember().getId().equals(member.getId()))
            throw new BusinessException(ErrorCode.FORBIDDEN_DELETE_EXCHANGE_REQUEST);

        ExchangePost exchangePost = exchangePostRepository.findById(exchangePostId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.EXCHANGE_POST_NOT_FOUND));

        if (!exchangePost.getId().equals(exchangeRequest.getExchangePost().getId()))
            throw new BusinessException(ErrorCode.EXCHANGE_POST_NOT_FOUND);

        if (exchangeRequest.getStatus() == ACCEPTED)
            throw new BusinessException(ErrorCode.FORBIDDEN_DELETE_EXCHANGE_REQUEST);

        exchangeRequest.getGifticon().updateActive(true);
        exchangeRequestRepository.delete(exchangeRequest);
    }

    @Transactional
    public void acceptExchangeRequest(Member member, long exchangePostId, long exchangeRequestId) {
        ExchangePost exchangePost = exchangePostRepository.findById(exchangePostId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.EXCHANGE_POST_NOT_FOUND));

        if (!exchangePost.getGifticon().getMember().getId().equals(member.getId()))
            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS_EXCHANGE_POST);

        ExchangeRequest exchangeRequest = exchangeRequestRepository.findById(exchangeRequestId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.EXCHANGE_REQUEST_NOT_FOUND));

        if (!exchangeRequest.getExchangePost().getId().equals(exchangePostId))
            throw new EntityNotFoundException(ErrorCode.EXCHANGE_REQUEST_NOT_FOUND);

        Gifticon gifticonToReceive = Gifticon.builder()
                .gifticonImg(exchangeRequest.getGifticon().getGifticonImg())
                .category(exchangeRequest.getGifticon().getCategory())
                .store(exchangeRequest.getGifticon().getStore())
                .product(exchangeRequest.getGifticon().getProduct())
                .expireDate(exchangeRequest.getGifticon().getExpireDate())
                .barcodeNum(exchangeRequest.getGifticon().getBarcodeNum())
                .orderNum(exchangeRequest.getGifticon().getOrderNum())
                .price(exchangeRequest.getGifticon().getPrice())
                .isUsed(exchangeRequest.getGifticon().isUsed())
                .isActive(true)
                .member(member)
                .build();
        gifticonRepository.save(gifticonToReceive);

        Gifticon gifticonToGive = Gifticon.builder()
                .gifticonImg(exchangePost.getGifticon().getGifticonImg())
                .category(exchangePost.getGifticon().getCategory())
                .store(exchangePost.getGifticon().getStore())
                .product(exchangePost.getGifticon().getProduct())
                .expireDate(exchangePost.getGifticon().getExpireDate())
                .barcodeNum(exchangePost.getGifticon().getBarcodeNum())
                .orderNum(exchangePost.getGifticon().getOrderNum())
                .price(exchangePost.getGifticon().getPrice())
                .isUsed(exchangePost.getGifticon().isUsed())
                .isActive(true)
                .member(exchangeRequest.getGifticon().getMember())
                .build();
        gifticonRepository.save(gifticonToGive);

        exchangePost.updateStatus(COMPLETE);
        exchangeRequest.updateStatus(ACCEPTED);
        List<ExchangeRequest> otherRequestList = exchangeRequestRepository.findAllByPostIdAndReqId(exchangePostId, exchangeRequestId);
        for (ExchangeRequest request : otherRequestList) {
            request.updateStatus(REJECTED);
            request.getGifticon().updateActive(true);
        }

        if (member.getExchangeCoin() < 1 || exchangeRequest.getGifticon().getMember().getExchangeCoin() < 1)
            throw new BusinessException(ErrorCode.TOO_LITTLE_COIN);
        member.minusExchangeCoin();
        exchangeRequest.getGifticon().getMember().minusExchangeCoin();
    }
}
