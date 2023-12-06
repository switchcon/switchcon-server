package com.CSP2.switchcon.gifticon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendRegisterResponseDTO {
    private boolean success;
    private String reason;
}
