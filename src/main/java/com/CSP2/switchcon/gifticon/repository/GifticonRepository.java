package com.CSP2.switchcon.gifticon.repository;

import com.CSP2.switchcon.gifticon.domain.Gifticon;
import com.CSP2.switchcon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GifticonRepository extends JpaRepository<Gifticon, Long> {

    @Query(value = "select g from Gifticon g where g.member = :member and g.id = :gifticonId")
    Optional<Gifticon> findByIdAndMember(@Param("member") Member member, @Param("gifticonId") long gifticonId);
}
