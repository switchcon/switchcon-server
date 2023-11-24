package com.CSP2.switchcon.member.domain;

import com.CSP2.switchcon.common.domain.DateTimeEntity;
import com.CSP2.switchcon.gifticon.domain.Gifticon;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class Member extends DateTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private boolean notifyOn;

    @Column(nullable = false)
    private int exchangeCoin;

    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private List<Gifticon> gifticons = new ArrayList<>();

    @Builder
    public Member(String accountId, String password, String nickname, boolean notifyOn, int exchangeCoin) {
        this.accountId = accountId;
        this.password = password;
        this.nickname = nickname;
        this.notifyOn = notifyOn;
        this.exchangeCoin = exchangeCoin;
    }

    public void update(boolean notifyOn) {
        this.notifyOn = notifyOn;
    }

    public void minusExchangeCoin() { this.exchangeCoin -= 1; }

}
