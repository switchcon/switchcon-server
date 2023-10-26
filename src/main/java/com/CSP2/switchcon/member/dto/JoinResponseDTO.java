package com.CSP2.switchcon.member.dto;

import com.CSP2.switchcon.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class JoinResponseDTO {
    private final Long memberId;

    private final String nickname;

    public static JoinResponseDTO of(Member member) {
        return JoinResponseDTO.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();
    }

}
