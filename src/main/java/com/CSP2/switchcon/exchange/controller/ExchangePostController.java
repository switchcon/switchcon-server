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

    @PostMapping("")
    @Operation(summary = "기프티콘 교환 게시물 등록", description = "기프티콘 교환 게시물을 등록합니다.")
    public ResponseEntity<BasicResponse> addExchange(@ReqMember SecurityUserDetails securityUserDetails,
                                                     @RequestBody ExchangePostRequestDTO requestDTO) {
        return basicResponse.ok(
                exchangeService.addExchange(securityUserDetails.member(), requestDTO)
        );
    }

    @GetMapping("/{exchangePostId}")
    @Operation(summary = "기프티콘 교환 게시물 상세 조회", description = "기프티콘 교환 게시물을 상세 조회합니다.")
    public ResponseEntity<BasicResponse> getExchange(@ReqMember SecurityUserDetails securityUserDetails,
                                                     @PathVariable("exchangePostId") long exchangePostId) {
        return basicResponse.ok(
                exchangeService.getExchange(securityUserDetails.member(), exchangePostId)
        );
    }

}
