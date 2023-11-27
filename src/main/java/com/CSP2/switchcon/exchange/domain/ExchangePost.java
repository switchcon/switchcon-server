package com.CSP2.switchcon.exchange.domain;

import com.CSP2.switchcon.common.domain.DateTimeEntity;
import com.CSP2.switchcon.gifticon.domain.Gifticon;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class ExchangePost extends DateTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "exchange_post_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ExchangeStatus status;

    private String preference;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "gifticon_id")
    private Gifticon gifticon;

    @OneToMany(mappedBy = "exchangePost", cascade = ALL, orphanRemoval = true)
    private List<ExchangeRequest> exchangeRequests = new ArrayList<>();

    @Builder
    public ExchangePost(ExchangeStatus status, String preference, Gifticon gifticon) {
        this.status = status;
        this.preference = preference;
        this.gifticon = gifticon;
    }

    public void updateStatus(ExchangeStatus status) { this.status = status; }
}
