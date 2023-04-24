package com.interviewchallenge.service;

import com.interviewchallenge.entity.Member;
import com.interviewchallenge.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member createNewUnnamedMember() {
        return memberRepository.save(new Member());
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Member findMemberBySessionToken(String sessionId) {
        return memberRepository.findBySessionToken(sessionId);
    }
}
