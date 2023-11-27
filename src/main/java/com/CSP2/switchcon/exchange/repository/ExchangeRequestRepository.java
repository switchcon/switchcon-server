package com.CSP2.switchcon.exchange.repository;

import com.CSP2.switchcon.exchange.domain.ExchangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {
}
