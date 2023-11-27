package com.CSP2.switchcon.member.service;

import com.CSP2.switchcon.member.domain.Member;
import com.CSP2.switchcon.member.dto.MemberResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    @Transactional
    public MemberResponseDTO getMyInfo(Member member) {
        return MemberResponseDTO.from(member);
    }
}
