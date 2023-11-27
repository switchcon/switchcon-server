package com.CSP2.switchcon.exchange.repository;

import com.CSP2.switchcon.exchange.domain.ExchangePost;
import com.CSP2.switchcon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExchangePostRepository extends JpaRepository<ExchangePost, Long> {

    @Query(value = "select ep from ExchangePost ep where ep.gifticon.price >= :min and ep.gifticon.price < :max")
    List<ExchangePost> findAllByPrice(@Param("min") int min, @Param("max") int max);

    @Query(value = "select ep from ExchangePost ep where ep.gifticon.member = :member")
    List<ExchangePost> findByMember(@Param("member") Member member);
}
