package com.CSP2.switchcon.member.dto;

import com.CSP2.switchcon.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberIdReseponseDTO {

    private final long id;

    public static MemberIdReseponseDTO from (Member member) {
        return MemberIdReseponseDTO.builder()
                .id(member.getId())
                .build();
    }
}
