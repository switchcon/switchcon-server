package com.CSP2.switchcon.gifticon.service;

import com.CSP2.switchcon.common.exception.BusinessException;
import com.CSP2.switchcon.common.exception.ErrorCode;
import com.CSP2.switchcon.gifticon.domain.Gifticon;
import com.CSP2.switchcon.gifticon.dto.GifticonResponseDTO;
import com.CSP2.switchcon.gifticon.dto.OcrResponseDTO;
import com.CSP2.switchcon.gifticon.repository.GifticonRepository;
import com.CSP2.switchcon.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GifticonService {

    @Value("${ocr.url}")
    private String OCR_SERVER_URL;

    private final GifticonRepository gifticonRepository;

    @Transactional
    public GifticonResponseDTO addGifticon(Member member, String gifticonImg) {
        OcrResponseDTO ocrResponseDTO = getGifticonInfo(gifticonImg);

        LocalDate expireDate = parseDate(ocrResponseDTO.getExpireDate());

        Gifticon gifticon = Gifticon.builder()
                .gifticonImg(gifticonImg)
                .category(ocrResponseDTO.getCategory())
                .store(ocrResponseDTO.getStoreName())
                .product(ocrResponseDTO.getProduct())
                .expireDate(expireDate)
                .barcodeNum(ocrResponseDTO.getBarcodeNum())
                .price(10000)
                .isUsed(false)
                .isActive(true)
                .member(member)
                .build();

        Gifticon saved = gifticonRepository.save(gifticon);
        return GifticonResponseDTO.from(saved);
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
    public OcrResponseDTO getGifticonInfo(String imgUrl) {
        WebClient client = WebClient.builder()
                .baseUrl(OCR_SERVER_URL)
                .build();

        return client.post()
                .uri("/upload")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of("imgUrl", imgUrl)))
                .retrieve()
                .bodyToMono(OcrResponseDTO.class)
                .block();
                //TODO::예외처리(fail 일 때)
    }
}
