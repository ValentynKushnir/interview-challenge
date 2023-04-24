package com.interviewchallenge.infra;

import com.interviewchallenge.entity.Member;
import com.interviewchallenge.service.MemberService;
import com.interviewchallenge.util.MemberThreadLocalManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SessionFilter extends HttpFilter {

    private static final String HEADER_SESSION_TOKEN_FIELD = "session_token";
    private final MemberService memberService;

    @Override
    public void doFilter(HttpServletRequest servletRequest,
                         HttpServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String sessionToken = servletRequest.getHeader(HEADER_SESSION_TOKEN_FIELD);

        Member member;
        if (!StringUtils.hasLength(sessionToken)) {
            member = memberService.createNewUnnamedMember();
            sessionToken = generateSessionToken();
            member.setSessionToken(sessionToken);
            memberService.save(member);
        } else {
            member = memberService.findMemberBySessionToken(sessionToken);
        }
        servletResponse.addHeader(HEADER_SESSION_TOKEN_FIELD, sessionToken);

        try {
            MemberThreadLocalManager.putMemberToThreadLocal(member);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MemberThreadLocalManager.removeMemberFromThreadLocal();
        }
    }

    private static String generateSessionToken() {
        return UUID.randomUUID().toString();
    }
}
