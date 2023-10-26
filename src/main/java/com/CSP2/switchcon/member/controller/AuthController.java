package com.CSP2.switchcon.member.controller;

import com.CSP2.switchcon.common.dto.BasicResponse;
import com.CSP2.switchcon.member.dto.JoinRequestDTO;
import com.CSP2.switchcon.member.dto.LoginRequestDTO;
import com.CSP2.switchcon.member.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "🔑 인증", description = "회원가입, 로그인")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final BasicResponse basicResponse = new BasicResponse();

    @PostMapping("/join")
    @Operation(summary = "회원 가입")
    public ResponseEntity<BasicResponse> join(@RequestBody JoinRequestDTO requestDTO) {
        var joinResponseDTO = authService.join(requestDTO);
        return basicResponse.ok(joinResponseDTO.getNickname());
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<BasicResponse> login(@RequestBody LoginRequestDTO requestDTO) {
        String token = authService.login(requestDTO.getAccountId(), requestDTO.getPassword());
        return basicResponse.ok(token);
    }
}
