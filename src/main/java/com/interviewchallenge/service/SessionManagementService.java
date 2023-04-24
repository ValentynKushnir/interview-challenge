package com.interviewchallenge.service;

import com.interviewchallenge.dto.AddUserStoryRequest;
import com.interviewchallenge.dto.SessionCreationRequest;
import com.interviewchallenge.entity.*;
import com.interviewchallenge.exception.ServiceException;
import com.interviewchallenge.exception.ServiceExceptionEnum;
import com.interviewchallenge.repository.SessionRepository;
import com.interviewchallenge.util.MemberThreadLocalManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionManagementService {

    private final ModelMapper modelMapper;
    private final SessionService sessionService;
    private final UserStoryService userStoryService;
    private final SseEmitterMessagesSender sseEmitterMessagesSender;
    private final SessionRepository sessionRepository;

    public Session createNewSession(SessionCreationRequest dto) {
        Session newSession = modelMapper.map(dto, Session.class);
        newSession.setSessionStatus(SessionStatus.ONGOING);
        newSession.setCreator(MemberThreadLocalManager.getCurrentMember());
        return sessionService.save(newSession);
    }

    public void destroySessionById(Long id) {
        Session session = sessionService.findSessionOrThrowException(id);
        checkUserSessionActionAllowed(session);

        session.getUserStories().forEach(userStory -> userStory.setStatus(UserStoryStatus.CANCELLED));
        session.setSessionStatus(SessionStatus.DESTROYED);

        session.getMembers().forEach(member -> member.setSession(null));
        sessionService.save(session);

        sseEmitterMessagesSender.clearEmitters();
    }

    public void addUserStoryToSession(Long sessionId, AddUserStoryRequest request) {
        Session session = sessionService.findSessionOrThrowException(sessionId);
        checkUserSessionActionAllowed(session);

        UserStory newUserStory = userStoryService.createNewUserStory(session, request.getStoryId(), request.getDescription());
        session.getUserStories().add(newUserStory);

        sessionRepository.save(session);
    }

    public void removeUserStoryFromSession(Long sessionId, Long userStoryId) {
        Session session = sessionService.findSessionOrThrowException(sessionId);
        checkUserStoryRemovalAllowed(session, userStoryId);

        UserStory userStory = session.getUserStories().stream().filter(us -> us.getId().equals(userStoryId)).findFirst().get();
        userStory.setStatus(UserStoryStatus.CANCELLED);
        userStoryService.save(userStory);

        session.getUserStories().removeIf(us -> us.getId().equals(userStoryId));
        sessionRepository.save(session);
    }

    private void checkUserSessionActionAllowed(Session session) {
        Member currentMember = MemberThreadLocalManager.getCurrentMember();
        if (!session.getCreator().getId().equals(currentMember.getId())) {
            throw new ServiceException(ServiceExceptionEnum.SESSION_REMOVAL_NOT_ALLOWED);
        }
    }

    private void checkUserStoryRemovalAllowed(Session session, Long userStoryId) {
        checkUserSessionActionAllowed(session);
        if (session.getUserStories().stream().noneMatch(userStory -> userStory.getId().equals(userStoryId))) {
            throw new ServiceException(ServiceExceptionEnum.USER_STORY_NOT_PRESENT_IN_SESSION);
        }
        UserStory userStory = session.getUserStories().stream()
                .filter(us -> us.getId().equals(userStoryId))
                .findFirst()
                .get();
        if (userStory.getStatus() != UserStoryStatus.PENDING) {
            throw new ServiceException(ServiceExceptionEnum.USER_STORY_NOT_IN_CORRECT_STATUS);
        }
    }
}
