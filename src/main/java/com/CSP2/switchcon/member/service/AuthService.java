package com.CSP2.switchcon.member.service;

import com.CSP2.switchcon.common.exception.BusinessException;
import com.CSP2.switchcon.common.exception.ErrorCode;
import com.CSP2.switchcon.common.exception.InvalidValueException;
import com.CSP2.switchcon.member.domain.Member;
import com.CSP2.switchcon.member.dto.JoinRequestDTO;
import com.CSP2.switchcon.member.dto.JoinResponseDTO;
import com.CSP2.switchcon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public JoinResponseDTO join(JoinRequestDTO requestDTO) {
        // 계정 아이디 중복 검사
        validateUnique(requestDTO.getAccountId());

        // 비밀번호 재확인
        if (!requestDTO.getPassword().equals(requestDTO.getCheckPassword()))
            throw new InvalidValueException(ErrorCode.INVALID_PASSWORD);

        var member = Member.builder()
                .accountId(requestDTO.getAccountId())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .nickname(requestDTO.getNickname())
                .exchangeCoin(3)
                .build();

        var savedMember = memberRepository.save(member);

        return JoinResponseDTO.of(savedMember);
    }

    private void validateUnique(String accountId) {
        if (memberRepository.findByAccountId(accountId).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_USERNAME);
        }
    }
}
