package com.CSP2.switchcon.exchange.service;

import com.CSP2.switchcon.common.exception.BusinessException;
import com.CSP2.switchcon.common.exception.EntityNotFoundException;
import com.CSP2.switchcon.common.exception.ErrorCode;
import com.CSP2.switchcon.common.exception.InvalidValueException;
import com.CSP2.switchcon.exchange.domain.ExchangePost;
import com.CSP2.switchcon.exchange.dto.AddExchangePostReponseDTO;
import com.CSP2.switchcon.exchange.dto.AllExchangePostsResponseDTO;
import com.CSP2.switchcon.exchange.dto.ExchangePostRequestDTO;
import com.CSP2.switchcon.exchange.dto.ExchangePostResponseDTO;
import com.CSP2.switchcon.exchange.repository.ExchangePostRepository;
import com.CSP2.switchcon.gifticon.domain.Gifticon;
import com.CSP2.switchcon.gifticon.repository.GifticonRepository;
import com.CSP2.switchcon.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.CSP2.switchcon.exchange.domain.ExchangeStatus.PROGRESS;

@Service
@RequiredArgsConstructor
public class ExchangePostService {

    private final GifticonRepository gifticonRepository;
    private final ExchangePostRepository exchangePostRepository;

    @Transactional
    public AddExchangePostReponseDTO addExchange(Member member, ExchangePostRequestDTO requestDTO) {
        Gifticon gifticon = gifticonRepository.findByIdAndMember(member, requestDTO.getGifticonId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.GIFTICON_NOT_FOUND));

        if (gifticon.getMember().getExchangeCoin() < 1)
            throw new BusinessException(ErrorCode.TOO_LITTLE_COIN);

        ExchangePost exchangePost = ExchangePost.builder()
                .status(PROGRESS)
                .preference(requestDTO.getPreference())
                .gifticon(gifticon)
                .build();

        ExchangePost saved = exchangePostRepository.save(exchangePost);
        gifticon.updateActive(false);
        gifticon.getMember().minusExchangeCoin();

        return AddExchangePostReponseDTO.from(saved);
    }


    @Transactional
    public ExchangePostResponseDTO getExchange(Member member, long exchangePostId) {

        ExchangePost exchangePost = exchangePostRepository.findById(exchangePostId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.EXCHANGE_POST_NOT_FOUND));

        boolean isMine = false;

        if (exchangePost.getGifticon().getMember().getId().equals(member.getId())) {
            isMine = true;
        }

        return ExchangePostResponseDTO.from(exchangePost.getGifticon(), exchangePost, isMine);
    }

    @Transactional
    public List<AllExchangePostsResponseDTO> getAllExchangePosts(String sortType) {
        List<ExchangePost> posts;

        switch(sortType) {
            case "under10000":
                posts = exchangePostRepository.findAllByPrice(0, 10000);
                break ;
            case "upTo10000":
                posts = exchangePostRepository.findAllByPrice(10000, 30000);
                break ;
            case "upTo30000":
                posts = exchangePostRepository.findAllByPrice(30000, 50000);
                break ;
            case "upTo50000":
                posts = exchangePostRepository.findAllByPrice(50000, 70000);
                break ;
            case "upTo70000":
                posts = exchangePostRepository.findAllByPrice(70000, 100000);
                break ;
            case "upTo100000":
                posts = exchangePostRepository.findAllByPrice(100000, 1000000);
                break ;
            default:
                throw new InvalidValueException(ErrorCode.INVALID_SORT_TYPE);
        }

        return posts.stream()
                .map(ep -> AllExchangePostsResponseDTO.from(ep.getGifticon(), ep))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delExchange(Member member, long exchangePostId) {
        ExchangePost exchangePost = exchangePostRepository.findById(exchangePostId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.EXCHANGE_POST_NOT_FOUND));

        if (!exchangePost.getGifticon().getMember().getId().equals(member.getId()))
            throw new BusinessException(ErrorCode.FORBIDDEN_DELETE_EXCHANGE_POST);

        exchangePostRepository.deleteByIdAndMember(member, exchangePostId);

        exchangePost.getGifticon().updateActive(true);
    }
}
