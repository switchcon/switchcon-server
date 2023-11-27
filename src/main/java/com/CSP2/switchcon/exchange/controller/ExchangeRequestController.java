package com.CSP2.switchcon.exchange.controller;


import com.CSP2.switchcon.common.dto.BasicResponse;
import com.CSP2.switchcon.exchange.dto.request.ExchangeRequestDTO;
import com.CSP2.switchcon.exchange.service.ExchangeRequestService;
import com.CSP2.switchcon.security.annotation.ReqMember;
import com.CSP2.switchcon.security.provider.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exchange")
@Tag(name = "✉️ 기프티콘 교환 요청", description = "기프티콘 교환 요청 API")
@RequiredArgsConstructor
public class ExchangeRequestController {

    private final ExchangeRequestService exchangeRequestService;
    private final BasicResponse basicResponse = new BasicResponse();

    @PostMapping("/{exchangePostId}/request")
    @Operation(summary = "기프티콘 교환 요청 등록", description = "기프티콘 교환 요청을 등록합니다.")
    public ResponseEntity<BasicResponse> addExchangeRequest(@ReqMember SecurityUserDetails securityUserDetails,
                                                     @PathVariable("exchangePostId") long exchangePostId,
                                                            @RequestBody ExchangeRequestDTO requestDTO) {
        return basicResponse.ok(
                exchangeRequestService.addExchangeRequest(securityUserDetails.member(), exchangePostId, requestDTO.getGifticonId())
        );
    }

    @DeleteMapping("/{exchangePostId}/request/{exchangeRequestId}")
    @Operation(summary = "기프티콘 교환 요청 삭제", description = "기프티콘 교환 요청을 삭제합니다.")
    public ResponseEntity<BasicResponse> delExchangeRequest(@ReqMember SecurityUserDetails securityUserDetails,
                                                            @PathVariable("exchangePostId") long exchangePostId,
                                                            @PathVariable("exchangeRequestId") long exchangeRequestId) {
        exchangeRequestService.delExchangeRequest(securityUserDetails.member(), exchangePostId, exchangeRequestId);
        return basicResponse.noContent();
    }
}
