package com.CSP2.switchcon.exchange.domain;

import com.CSP2.switchcon.common.domain.DateTimeEntity;
import com.CSP2.switchcon.gifticon.domain.Gifticon;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class ExchangeRequest extends DateTimeEntity  {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "exchange_request_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ExchangeStatus status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "gifticon_id")
    private Gifticon gifticon;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "exchange_post_id")
    private ExchangePost exchangePost;

    @Builder
    public ExchangeRequest(ExchangeStatus status, Gifticon gifticon, ExchangePost exchangePost) {
        this.status = status;
        this.gifticon = gifticon;
        this.exchangePost = exchangePost;
    }

    public void updateStatus(ExchangeStatus status) { this.status = status; }
}
