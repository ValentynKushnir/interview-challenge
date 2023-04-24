package com.interviewchallenge.service;

import com.interviewchallenge.dto.SessionJoinRequest;
import com.interviewchallenge.entity.Member;
import com.interviewchallenge.entity.Session;
import com.interviewchallenge.entity.SessionStatus;
import com.interviewchallenge.exception.ServiceException;
import com.interviewchallenge.exception.ServiceExceptionEnum;
import com.interviewchallenge.repository.SessionRepository;
import com.interviewchallenge.util.MemberThreadLocalManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final MemberService memberService;

    private final SessionRepository sessionRepository;

    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    public Session joinSession(Long sessionId, SessionJoinRequest sessionJoinRequest) {
        Session session = findSessionOrThrowException(sessionId);
        checkSessionCanBeJoined(session);

        Member currentMember = MemberThreadLocalManager.getCurrentMember();
        checkCurrentMemberCanJoinSession(currentMember, session);

        currentMember.setNickName(sessionJoinRequest.getNickName());
        currentMember = memberService.save(currentMember);

        currentMember.setSession(session);
        session.getMembers().add(currentMember);
        sessionRepository.save(session);

        return session;
    }

    public Session findSessionOrThrowException(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ServiceException(ServiceExceptionEnum.SESSION_NOT_FOUND));
    }

    private void checkSessionCanBeJoined(Session session) {
        if (session.getSessionStatus() != SessionStatus.ONGOING) {
            throw new ServiceException(ServiceExceptionEnum.SESSION_CANNOT_BE_JOINED);
        }
    }

    private void checkCurrentMemberCanJoinSession(Member currentMember, Session session) {
        if (currentMember.getSession() != null && !currentMember.getSession().getId().equals(session.getId())) {
            throw new ServiceException(ServiceExceptionEnum.MEMBER_ALREADY_IN_SESSION);
        }
    }
}
