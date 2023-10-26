package com.CSP2.switchcon.member.repository;

import com.CSP2.switchcon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByAccountId(String accountId);

}
