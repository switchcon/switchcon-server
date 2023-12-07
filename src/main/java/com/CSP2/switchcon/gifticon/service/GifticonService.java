package com.CSP2.switchcon.gifticon.service;

import com.CSP2.switchcon.common.exception.BusinessException;
import com.CSP2.switchcon.common.exception.EntityNotFoundException;
import com.CSP2.switchcon.common.exception.ErrorCode;
import com.CSP2.switchcon.common.exception.InvalidValueException;
import com.CSP2.switchcon.gifticon.domain.Gifticon;
import com.CSP2.switchcon.gifticon.dto.*;
import com.CSP2.switchcon.gifticon.repository.GifticonRepository;
import com.CSP2.switchcon.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.CSP2.switchcon.gifticon.auth.SwitchconHMACAuthenticator.getAuthStringQRUse;
import static com.CSP2.switchcon.gifticon.auth.SwitchconHMACAuthenticator.getAuthStringRegister;

@Service
@RequiredArgsConstructor
public class GifticonService {

    @Value("${ocr.url}")
    private String OCR_SERVER_URL;

    @Value("${gifticonServer.url}")
    private String GIFTICON_SERVER_URL;

    private static byte[] keyBytes = {
            (byte)0x01,(byte)0x01,(byte)0x01,(byte)0x01,
            (byte)0x02,(byte)0x02,(byte)0x02,(byte)0x02,
            (byte)0x03,(byte)0x03,(byte)0x03,(byte)0x03,
            (byte)0x04,(byte)0x04,(byte)0x04,(byte)0x04,
            (byte)0x05,(byte)0x05,(byte)0x05,(byte)0x05,
            (byte)0x06,(byte)0x06,(byte)0x06,(byte)0x06,
            (byte)0x07,(byte)0x07,(byte)0x07,(byte)0x07,
            (byte)0x08,(byte)0x08,(byte)0x08,(byte)0x08,
    };

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
    public AddGifticonResponseDTO addGifticon(Member member, GifticonRequestDTO requestDTO) throws NoSuchAlgorithmException, IOException, InvalidKeyException {

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

        SendAddItem(gifticon);
        SendRegisterReq(requestDTO.getBarcodeNum());
        Gifticon saved = gifticonRepository.save(gifticon);

        return AddGifticonResponseDTO.from(saved);
    }

    @Transactional
    public void SendAddItem(Gifticon gifticon) {

        WebClient client = WebClient.builder()
                .baseUrl(GIFTICON_SERVER_URL)
                .build();

        String requestUrl = String.format("/gifticon/additem?barcodenum=%s&productname=%s&category=%s&price=%d&used=%s&expiredate=%s",
                gifticon.getBarcodeNum(), gifticon.getProduct(), gifticon.getCategory(), gifticon.getPrice(), gifticon.isUsed(), gifticon.getExpireDate());

        client.post()
                .uri(requestUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue("")
                .exchange()
                .flatMap(clientResponse -> {
                    if (clientResponse.statusCode().is4xxClientError()) {
                        return Mono.error(new BusinessException(ErrorCode.ELSE));
                    } else {
                        return Mono.just("success");
                    }
                })
                .block();
    }

    @Transactional
    public void SendRegisterReq(String barcodeNum) throws NoSuchAlgorithmException, IOException, InvalidKeyException {

        String base64Barcode = getAuthStringRegister(barcodeNum, "switchcon", keyBytes);

        WebClient client = WebClient.builder()
                .baseUrl(GIFTICON_SERVER_URL)
                .build();

        client.post()
                .uri("/gifticon/encrypt/register/" + barcodeNum)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(base64Barcode)
                .exchange()
                .flatMap(clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST)
                        return Mono.error(new BusinessException(ErrorCode.NOT_EXISTING_IN_DATABASE));
                    else if (clientResponse.statusCode() == HttpStatus.NOT_ACCEPTABLE)
                        return Mono.error(new BusinessException(ErrorCode.EXPIRED_BARCODE));
                    else if (clientResponse.statusCode() == HttpStatus.CONFLICT)
                        return Mono.error(new BusinessException(ErrorCode.HASH_CHECK_FAILURE));
                    else if (clientResponse.statusCode() == HttpStatus.GONE)
                        return Mono.error(new BusinessException(ErrorCode.USED_BARCODE));
                    else if (clientResponse.statusCode() == HttpStatus.UNPROCESSABLE_ENTITY)
                        return Mono.error(new BusinessException(ErrorCode.ELSE));
                    else
                        return Mono.just("success");
                })
                .block();
    }

    @Transactional
    public GifticonResponseDTO getGifticon(Member member, long gifticonId) {
        Gifticon gifticon = gifticonRepository.findByIdAndMember(member, gifticonId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.GIFTICON_NOT_FOUND));
        return GifticonResponseDTO.from(gifticon);
    }

    @Transactional
    public List<AllGifticonsResponseDTO> getAllGifticons(Member member, String sortType) {
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
                .map(AllGifticonsResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delGifticon(Member member, long gifticonId) {
        Gifticon gifticon = gifticonRepository.findByIdAndMember(member, gifticonId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.GIFTICON_NOT_FOUND));

        if (gifticon.isActive() == false)
            throw new BusinessException(ErrorCode.INACTIVE_GIFTION);

        gifticonRepository.delete(gifticon);
    }

    @Transactional
    public String useGifticon(Member member, long gifticonId) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        Gifticon gifticon = gifticonRepository.findByIdAndMember(member, gifticonId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.GIFTICON_NOT_FOUND));

        if (gifticon.isActive() == false)
            throw new BusinessException(ErrorCode.INACTIVE_GIFTION);

        String base64Barcode = getAuthStringQRUse(gifticon.getBarcodeNum(), "switchcon", keyBytes);

        WebClient client = WebClient.builder()
                .baseUrl(GIFTICON_SERVER_URL)
                .build();

        client.post()
                .uri("/gifticon/encrypt/useqrcode")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(base64Barcode)
                .exchange()
                .flatMap(clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST)
                        return Mono.error(new BusinessException(ErrorCode.NOT_EXISTING_IN_DATABASE));
                    else if (clientResponse.statusCode() == HttpStatus.UNAUTHORIZED)
                        return Mono.error(new BusinessException(ErrorCode.SWITCHCON_REGISTERED_INVALID_USE));
                    else if (clientResponse.statusCode() == HttpStatus.NOT_ACCEPTABLE)
                        return Mono.error(new BusinessException(ErrorCode.EXPIRED_BARCODE));
                    else if (clientResponse.statusCode() == HttpStatus.REQUEST_TIMEOUT)
                        return Mono.error(new BusinessException(ErrorCode.SWITCHCON_TIMEOUT));
                    else if (clientResponse.statusCode() == HttpStatus.CONFLICT)
                        return Mono.error(new BusinessException(ErrorCode.HASH_CHECK_FAILURE));
                    else if (clientResponse.statusCode() == HttpStatus.GONE)
                        return Mono.error(new BusinessException(ErrorCode.USED_BARCODE));
                    else if (clientResponse.statusCode() == HttpStatus.UNPROCESSABLE_ENTITY)
                        return Mono.error(new BusinessException(ErrorCode.ELSE));
                    else
                        return Mono.just("success");
                })
                .block();

        gifticon.updateUse(true);
        gifticon.updateActive(false);

        return "기프티콘이 성공적으로 사용처리 되었습니다.";
    }
}
