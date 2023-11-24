package com.CSP2.switchcon.member.service;

import com.CSP2.switchcon.member.domain.Member;
import com.CSP2.switchcon.member.dto.MemberIdReseponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Transactional
    public MemberIdReseponseDTO getMemberId(Member member) {
        return MemberIdReseponseDTO.from(member);
    }
}
