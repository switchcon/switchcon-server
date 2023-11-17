package com.CSP2.switchcon.gifticon.dto;

import com.CSP2.switchcon.gifticon.domain.Gifticon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@Builder(access = PRIVATE)
public class AddGifticonResponseDTO {

    private final long id;

    public static AddGifticonResponseDTO from (Gifticon gifticon) {
        return AddGifticonResponseDTO.builder()
                .id(gifticon.getId())
                .build();
    }
}
