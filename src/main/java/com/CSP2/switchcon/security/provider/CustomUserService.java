package com.CSP2.switchcon.security.provider;

import com.CSP2.switchcon.common.exception.EntityNotFoundException;
import com.CSP2.switchcon.common.exception.ErrorCode;
import com.CSP2.switchcon.member.domain.Member;
import com.CSP2.switchcon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public SecurityUserDetails loadUserByUsername(String accountId) {
        Member member = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        return new SecurityUserDetails(member);
    }

}
