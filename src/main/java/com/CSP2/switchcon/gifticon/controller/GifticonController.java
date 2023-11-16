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
                                                   @RequestBody GifticonRequestDTO requestDTO) {
        return basicResponse.ok(
                gifticonService.addGifticon(securityUserDetails.member(), requestDTO)
        );
    }
}
