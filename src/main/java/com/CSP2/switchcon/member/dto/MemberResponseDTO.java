package com.CSP2.switchcon.member.dto;

import com.CSP2.switchcon.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponseDTO {

    private final long id;
    private final String nickname;
    private final int exchangeCoin;
    private final boolean notifyOn;

    public static MemberResponseDTO from (Member member) {
        return MemberResponseDTO.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .exchangeCoin(member.getExchangeCoin())
                .notifyOn(member.isNotifyOn())
                .build();
    }
}
