package com.CSP2.switchcon.gifticon.domain;

import com.CSP2.switchcon.common.domain.DateTimeEntity;
import com.CSP2.switchcon.exchange.domain.ExchangePost;
import com.CSP2.switchcon.exchange.domain.ExchangeRequest;
import com.CSP2.switchcon.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class Gifticon extends DateTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "gifticon_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String gifticonImg;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String store;

    @Column(nullable = false)
    private String product;

    @Column(nullable = false)
    private LocalDate expireDate;

    @Column(nullable = false)
    private String barcodeNum;

    private String orderNum;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    private boolean isUsed;

    @Column(nullable = false)
    private boolean isActive;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "gifticon", cascade = ALL, orphanRemoval = true)
    private List<ExchangePost> exchagePosts = new ArrayList<>();

    @OneToMany(mappedBy = "gifticon", cascade = ALL, orphanRemoval = true)
    private List<ExchangeRequest> exchangeRequests = new ArrayList<>();

    @Builder
    public Gifticon(String gifticonImg, String category, String store, String product, LocalDate expireDate,
                    String barcodeNum, String orderNum, long price, boolean isUsed, boolean isActive, Member member) {
        this.gifticonImg = gifticonImg;
        this.category = category;
        this.store =  store;
        this.product = product;
        this.expireDate = expireDate;
        this.barcodeNum = barcodeNum;
        this.orderNum = orderNum;
        this.price = price;
        this.isUsed = isUsed;
        this.isActive = isActive;
        this.member = member;
    }

    public void updateUse(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public void updateActive(boolean isActive) {this.isActive = isActive;}
}
