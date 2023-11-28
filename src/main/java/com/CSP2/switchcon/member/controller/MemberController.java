package com.CSP2.switchcon.member.controller;


import com.CSP2.switchcon.common.dto.BasicResponse;
import com.CSP2.switchcon.exchange.service.ExchangePostService;
import com.CSP2.switchcon.exchange.service.ExchangeRequestService;
import com.CSP2.switchcon.member.service.MemberService;
import com.CSP2.switchcon.security.annotation.ReqMember;
import com.CSP2.switchcon.security.provider.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "👩🏻‍💻 사용자", description = "사용자 API")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ExchangePostService exchangePostService;
    private final ExchangeRequestService exchangeRequestService;
    private final BasicResponse basicResponse = new BasicResponse();

    @GetMapping("/mypage/exchangePosts")
    @Operation(summary = "교환 등록글 조회", description = "교환 등록글을 조회합니다.")
    public ResponseEntity<BasicResponse> getMyExchangePosts(@ReqMember SecurityUserDetails securityUserDetails) {
        return basicResponse.ok(
                exchangePostService.getMyExchangePosts(securityUserDetails.member())
        );
    }

    @GetMapping("/mypage/exchangeRequests")
    @Operation(summary = "교환 신청글 조회", description = "교환 신청글을 조회합니다.")
    public ResponseEntity<BasicResponse> getMyExchangeRequests(@ReqMember SecurityUserDetails securityUserDetails) {
        return basicResponse.ok(
                exchangeRequestService.getMyExchangeRequests(securityUserDetails.member())
        );
    }

    @GetMapping("/mypage/info")
    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회합니다.")
    public ResponseEntity<BasicResponse> getMyInfo(@ReqMember SecurityUserDetails securityUserDetails) {
        return basicResponse.ok(
                memberService.getMyInfo(securityUserDetails.member())
        );
    }
}
