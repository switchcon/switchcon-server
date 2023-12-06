package com.CSP2.switchcon.gifticon.controller;

import com.CSP2.switchcon.common.dto.BasicResponse;
import com.CSP2.switchcon.gifticon.dto.GifticonRequestDTO;
import com.CSP2.switchcon.gifticon.dto.OcrRequestDTO;
import com.CSP2.switchcon.gifticon.service.GifticonService;
import com.CSP2.switchcon.security.annotation.ReqMember;
import com.CSP2.switchcon.security.provider.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/gifticon")
@Tag(name = "🎁 기프티콘", description = "기프티콘 CRUD API")
@RequiredArgsConstructor
public class GifticonController {

    private final GifticonService gifticonService;
    private final BasicResponse basicResponse = new BasicResponse();

    @PostMapping("/ocr")
    @Operation(summary = "기프티콘 이미지 ocr 정보", description = "기프티콘 이미지의 ocr 정보를 받아옵니다")
    public ResponseEntity<BasicResponse> uploadGifticonImg(@RequestBody OcrRequestDTO requestDTO) {
        return basicResponse.ok(
                gifticonService.uploadGifticonImg(requestDTO.getGifticonImg())
        );
    }

    @PostMapping("")
    @Operation(summary = "기프티콘 등록", description = "기프티콘을 등록합니다.")
    public ResponseEntity<BasicResponse> saveGifticon(@ReqMember SecurityUserDetails securityUserDetails,
                                                   @RequestBody GifticonRequestDTO requestDTO) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        return basicResponse.ok(
                gifticonService.addGifticon(securityUserDetails.member(), requestDTO)
        );
    }

    @GetMapping("/{gifticonId}")
    @Operation(summary = "기프티콘 상세 조회", description = "기프티콘을 상세 조회합니다.")
    public ResponseEntity<BasicResponse> getGifticon(@ReqMember SecurityUserDetails securityUserDetails,
                                                     @PathVariable ("gifticonId") long gifticonId) {
        return basicResponse.ok(
                gifticonService.getGifticon(securityUserDetails.member(), gifticonId)
        );
    }

    @GetMapping("/all/{sortType}")
    @Operation(summary = "기프티콘 전체 조회", description = "기프티콘을 전체 조회합니다.")
    public ResponseEntity<BasicResponse> getAllGifticons(@ReqMember SecurityUserDetails securityUserDetails,
                                                         @PathVariable ("sortType") String sortType) {
        return basicResponse.ok(
                gifticonService.getAllGifticons(securityUserDetails.member(), sortType)
        );
    }

    @DeleteMapping("/{gifticonId}")
    @Operation(summary = "기프티콘 삭제", description = "기프티콘을 삭제합니다.")
    public ResponseEntity<BasicResponse> delGifticon(@ReqMember SecurityUserDetails securityUserDetails,
                                                     @PathVariable ("gifticonId") long gifticonId) {
        gifticonService.delGifticon(securityUserDetails.member(), gifticonId);
        return  basicResponse.noContent();
    }

    @PostMapping("/use/{gifticonId}")
    @Operation(summary = "기프티콘 사용", description = "기프티콘을 사용합니다.")
    public ResponseEntity<BasicResponse> useGifticon(@ReqMember SecurityUserDetails securityUserDetails,
                                                     @PathVariable ("gifticonId") long gifticonId) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        return basicResponse.ok(
                gifticonService.useGifticon(securityUserDetails.member(), gifticonId)
        );
    }
}
