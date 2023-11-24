package com.CSP2.switchcon.exchange.service;

import com.CSP2.switchcon.common.exception.BusinessException;
import com.CSP2.switchcon.common.exception.EntityNotFoundException;
import com.CSP2.switchcon.common.exception.ErrorCode;
import com.CSP2.switchcon.exchange.domain.ExchangePost;
import com.CSP2.switchcon.exchange.dto.AddExchangePostReponseDTO;
import com.CSP2.switchcon.exchange.dto.ExchangePostRequestDTO;
import com.CSP2.switchcon.exchange.dto.ExchangePostResponseDTO;
import com.CSP2.switchcon.exchange.dto.UploadExchangeResponseDTO;
import com.CSP2.switchcon.exchange.repository.ExchangePostRepository;
import com.CSP2.switchcon.gifticon.domain.Gifticon;
import com.CSP2.switchcon.gifticon.dto.AddGifticonResponseDTO;
import com.CSP2.switchcon.gifticon.repository.GifticonRepository;
import com.CSP2.switchcon.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.CSP2.switchcon.exchange.domain.ExchangeStatus.PROGRESS;

@Service
@RequiredArgsConstructor
public class ExchangePostService {

    private final GifticonRepository gifticonRepository;
    private final ExchangePostRepository exchangePostRepository;

    @Transactional
    public UploadExchangeResponseDTO uploadExchange(Member member, long gifticonId) {
        Gifticon gifticon = gifticonRepository.findByIdAndMemberAndActive(member, gifticonId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INACTIVE_GIFTION));

        return UploadExchangeResponseDTO.from(gifticon);
    }

    @Transactional
    public AddExchangePostReponseDTO addExchange(Member member, ExchangePostRequestDTO requestDTO) {
        Gifticon gifticon = gifticonRepository.findByIdAndMember(member, requestDTO.getGifticonId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.GIFTICON_NOT_FOUND));

        ExchangePost exchangePost = ExchangePost.builder()
                .status(PROGRESS)
                .preference(requestDTO.getPreference())
                .gifticon(gifticon)
                .build();

        ExchangePost saved = exchangePostRepository.save(exchangePost);
        return AddExchangePostReponseDTO.from(saved);
    }
}
