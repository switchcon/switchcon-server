package com.CSP2.switchcon.exchange.controller;

import com.CSP2.switchcon.common.dto.BasicResponse;
import com.CSP2.switchcon.exchange.dto.ExchangePostRequestDTO;
import com.CSP2.switchcon.exchange.service.ExchangePostService;
import com.CSP2.switchcon.security.annotation.ReqMember;
import com.CSP2.switchcon.security.provider.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exchange")
@Tag(name = "🔄 기프티콘 교환", description = "기프티콘 교환 API")
@RequiredArgsConstructor
public class ExchangePostController {

    private final ExchangePostService exchangeService;
    private final BasicResponse basicResponse = new BasicResponse();

    @PostMapping("/{gifticonId}")
    @Operation(summary = "기프티콘 교환 게시물 정보 확인", description = "기프티콘 교환 게시물 등록 전 정보를 확인합니다.")
    public ResponseEntity<BasicResponse> uploadExchange(@ReqMember SecurityUserDetails securityUserDetails,
                                                        @PathVariable("gifticonId") long gifticonId) {
        return basicResponse.ok(
                exchangeService.uploadExchange(securityUserDetails.member(), gifticonId)
        );
    }

    @PostMapping("")
    @Operation(summary = "기프티콘 교환 게시물 등록", description = "기프티콘 교환 게시물을 등록합니다.")
    public ResponseEntity<BasicResponse> addExchange(@ReqMember SecurityUserDetails securityUserDetails,
                                                     @RequestBody ExchangePostRequestDTO requestDTO) {
        return basicResponse.ok(
                exchangeService.addExchange(securityUserDetails.member(), requestDTO)
        );
    }
}
