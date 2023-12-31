package com.CSP2.switchcon.exchange.repository;

import com.CSP2.switchcon.exchange.domain.ExchangePost;
import com.CSP2.switchcon.exchange.domain.ExchangeRequest;
import com.CSP2.switchcon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {

    @Query(value = "select er from ExchangeRequest er where er.exchangePost.id = :postId")
    List<ExchangeRequest> findAllByPostId(@Param("postId") Long postId);

    @Query(value = "select er from ExchangeRequest er where er.exchangePost.id = :postId and er.id != :reqId")
    List<ExchangeRequest> findAllByPostIdAndReqId(@Param("postId") Long postId, @Param("reqId") Long reqId);

    @Query(value = "select count(er) from ExchangeRequest er where er.exchangePost.id = :postId")
    int countByPostId(@Param("postId") Long postId);

    @Query(value = "select er.exchangePost from ExchangeRequest er where er.gifticon.member = :member")
    List<ExchangePost> findAllByMemberId(@Param("member") Member member);

    @Query(value = "select er from ExchangeRequest er where er.gifticon.member = :member and er.exchangePost.id = :postId")
    ExchangeRequest findByMemberIdAndPostId(@Param("member") Member member, @Param("postId") Long postId);
}
