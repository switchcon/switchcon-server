package com.CSP2.switchcon.member.dto;

import lombok.Getter;

@Getter
public class JoinRequestDTO {

    private String accountId;
    private String password;
    private String checkPassword;
    private String nickname;

}
