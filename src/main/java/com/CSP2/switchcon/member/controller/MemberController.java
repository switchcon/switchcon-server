package com.CSP2.switchcon.member.controller;


import com.CSP2.switchcon.common.dto.BasicResponse;
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
    private final BasicResponse basicResponse = new BasicResponse();

    @GetMapping("/memberId")
    @Operation(summary = "현재 사용자 id 조회", description = "현재 사용자의 id를 조회합니다.")
    public ResponseEntity<BasicResponse> getMemberId(@ReqMember SecurityUserDetails securityUserDetails) {
        return basicResponse.ok(
                memberService.getMemberId(securityUserDetails.member())
        );
    }
}
