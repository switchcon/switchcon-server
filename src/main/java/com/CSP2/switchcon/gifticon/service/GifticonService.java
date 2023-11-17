package com.CSP2.switchcon.gifticon.service;

import com.CSP2.switchcon.common.exception.BusinessException;
import com.CSP2.switchcon.common.exception.EntityNotFoundException;
import com.CSP2.switchcon.common.exception.ErrorCode;
import com.CSP2.switchcon.common.exception.InvalidValueException;
import com.CSP2.switchcon.gifticon.domain.Gifticon;
import com.CSP2.switchcon.gifticon.dto.GifticonRequestDTO;
import com.CSP2.switchcon.gifticon.dto.GifticonResponseDTO;
import com.CSP2.switchcon.gifticon.dto.OcrResponseDTO;
import com.CSP2.switchcon.gifticon.dto.AddGifticonResponseDTO;
import com.CSP2.switchcon.gifticon.repository.GifticonRepository;
import com.CSP2.switchcon.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GifticonService {

    @Value("${ocr.url}")
    private String OCR_SERVER_URL;

    private final GifticonRepository gifticonRepository;

    @Transactional
    public GifticonResponseDTO uploadGifticonImg(String gifticonImg) {
        OcrResponseDTO ocrResponseDTO = getOcrData(gifticonImg);

        LocalDate expireDate = parseDate(ocrResponseDTO.getExpireDate());

        return GifticonResponseDTO.of(gifticonImg, ocrResponseDTO.getCategory(), ocrResponseDTO.getStoreName(),
                ocrResponseDTO.getProductName(), ocrResponseDTO.getBarcodeNum(), ocrResponseDTO.getOrderNum(), expireDate, 10000, false);
    }

    public static LocalDate parseDate(String strDate) {
        List<DateTimeFormatter> formatters = Arrays.asList(
                DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"),
                DateTimeFormatter.ofPattern("yyyy.MM.dd"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );

        for (DateTimeFormatter formatter : formatters) {
            try {
                LocalDate parsedDate = LocalDate.parse(strDate, formatter);
                return parsedDate; // 포맷이 일치하면 반환
            } catch (Exception ignored) {
                // 무시하고 계속 진행
            }
        }

        throw new BusinessException(ErrorCode.INVALID_EXPIRE_DATE_FORMAT);
    }

    @Transactional
    public OcrResponseDTO getOcrData(String imgUrl) {
        WebClient client = WebClient.builder()
                .baseUrl(OCR_SERVER_URL)
                .build();

        return client.post()
                .uri("/upload")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(imgUrl)
                .exchange()
                .flatMap(clientResponse -> {
                    if (clientResponse.statusCode().is4xxClientError()) {
                        return Mono.error(new BusinessException(ErrorCode.IMG_INFO_NOT_FOUND));
                    } else {
                        return clientResponse.bodyToMono(OcrResponseDTO.class);
                    }
                })
                .block();
    }

    @Transactional
    public AddGifticonResponseDTO addGifticon(Member member, GifticonRequestDTO requestDTO) {

        Gifticon gifticon = Gifticon.builder()
                .gifticonImg(requestDTO.getGifticonImg())
                .category(requestDTO.getCategory())
                .store(requestDTO.getStore())
                .product(requestDTO.getProduct())
                .expireDate(requestDTO.getExpireDate())
                .barcodeNum(requestDTO.getBarcodeNum())
                .orderNum(requestDTO.getOrderNum())
                .price(requestDTO.getPrice())
                .isUsed(requestDTO.isUsed())
                .isActive(true)
                .member(member)
                .build();

        Gifticon saved = gifticonRepository.save(gifticon);
        return AddGifticonResponseDTO.from(saved);
    }

    @Transactional
    public GifticonResponseDTO getGifticon(Member member, long gifticonId) {
        Gifticon gifticon = gifticonRepository.findByIdAndMember(member, gifticonId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.GIFTICON_NOT_FOUND));
        return GifticonResponseDTO.from(gifticon);
    }

    @Transactional
    public List<GifticonResponseDTO> getAllGifticons(Member member, String sortType) {
        List<Gifticon> gifticons;
        LocalDate now = LocalDate.now();

        switch(sortType) {
            case "latest":
                gifticons = gifticonRepository.findAllByMemberAndLatest(member);
                break ;
            case "expiringSoon":
                gifticons = gifticonRepository.findAllByMemberAndExpireDate(member, now);
                break ;
            case "highPrice":
                gifticons = gifticonRepository.findAllByMemberAndHighPrice(member);
                break ;
            case "lowPrice":
                gifticons = gifticonRepository.findAllByMemberAndLowPrice(member);
                break ;
            default:
                throw new InvalidValueException(ErrorCode.INVALID_SORT_TYPE);
        }

        return gifticons.stream()
                .map(GifticonResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delGifticon(Member member, long gifticonId) {
        gifticonRepository.deleteByIdAndMember(member, gifticonId);
    }
}
