package com.CSP2.switchcon.exchange.repository;

import com.CSP2.switchcon.exchange.domain.ExchangePost;
import com.CSP2.switchcon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExchangePostRepository extends JpaRepository<ExchangePost, Long> {
}
