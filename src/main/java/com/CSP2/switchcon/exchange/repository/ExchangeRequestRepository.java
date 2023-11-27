package com.CSP2.switchcon.exchange.repository;

import com.CSP2.switchcon.exchange.domain.ExchangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {

    @Query(value = "select er from ExchangeRequest er where er.exchangePost.id = :postId")
    List<ExchangeRequest> findAllByPostId(@Param("postId") long postId);

    @Query(value = "select er from ExchangeRequest er where er.exchangePost.id = :postId and er.id != :reqId")
    List<ExchangeRequest> findAllByPostIdAndReqId(@Param("postId") long postId, @Param("reqId") long reqId);
}
