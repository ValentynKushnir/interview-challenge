package com.interviewchallenge.repository;

import com.interviewchallenge.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findBySessionToken(String sessionId);
}
