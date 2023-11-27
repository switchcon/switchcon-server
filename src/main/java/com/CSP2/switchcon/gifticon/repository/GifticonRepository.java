package com.CSP2.switchcon.gifticon.repository;

import com.CSP2.switchcon.gifticon.domain.Gifticon;
import com.CSP2.switchcon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GifticonRepository extends JpaRepository<Gifticon, Long> {

    @Query(value = "select g from Gifticon g where g.member = :member and g.id = :gifticonId")
    Optional<Gifticon> findByIdAndMember(@Param("member") Member member, @Param("gifticonId") long gifticonId);

    @Query(value = "select g from Gifticon g where g.member = :member order by g.createdAt DESC")
    List<Gifticon> findAllByMemberAndLatest(@Param("member") Member member);

    @Query(value = "select g from Gifticon g where g.member = :member order by ABS(DATEDIFF(:now, g.expireDate)) ASC")
    List<Gifticon> findAllByMemberAndExpireDate(@Param("member") Member member, @Param("now") LocalDate now);

    @Query(value = "select g from Gifticon g where g.member = :member order by g.price DESC")
    List<Gifticon> findAllByMemberAndHighPrice(@Param("member") Member member);

    @Query(value = "select g from Gifticon g where g.member = :member order by g.price ASC")
    List<Gifticon> findAllByMemberAndLowPrice(@Param("member") Member member);

    @Query(value = "select g from Gifticon g where g.member = :member and g.id = :gifticonId and g.isActive = true")
    Optional<Gifticon> findByIdAndMemberAndActive(@Param("member") Member member, @Param("gifticonId") long gifticonId);
}
