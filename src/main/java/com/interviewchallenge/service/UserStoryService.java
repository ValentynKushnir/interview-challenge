package com.interviewchallenge.service;

import com.interviewchallenge.dto.UserStoryDto;
import com.interviewchallenge.dto.UserStoryVoteStats;
import com.interviewchallenge.entity.*;
import com.interviewchallenge.exception.ServiceException;
import com.interviewchallenge.exception.ServiceExceptionEnum;
import com.interviewchallenge.repository.UserStoryRepository;
import com.interviewchallenge.util.MemberThreadLocalManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Set;

import static com.interviewchallenge.exception.ServiceExceptionEnum.USER_STORY_NOT_VISIBLE;

@Service
@RequiredArgsConstructor
public class UserStoryService {

    private static final long ONE_HOUR = 60 * 60 * 1000;

    private final VoteService voteService;
    private final SseEmitterMessagesSender sseEmitterMessagesManager;
    private final UserStoryRepository userStoryRepository;

    public UserStory save(UserStory userStory) {
        return userStoryRepository.save(userStory);
    }

    public UserStory createNewUserStory(Session session, String name, String description) {
        UserStory newUserStory = new UserStory(name, description, session);
        return userStoryRepository.save(newUserStory);
    }

    public void startUserStoryVoting(Long id) {
        UserStory userStory = findUserStoryOrThrowException(id);
        checkMemberAllowedToChangeUserStoryStatus(userStory);
        checkUserStoryNotCancelled(userStory);
        userStory.setStatus(UserStoryStatus.VOTING);
        userStoryRepository.save(userStory);

        sseEmitterMessagesManager.dispatchMessage(userStory.getSession().getMembers(), new UserStoryDto(userStory));
    }

    public void stopUserStoryVoting(Long id) {
        UserStory userStory = findUserStoryOrThrowException(id);
        checkMemberAllowedToChangeUserStoryStatus(userStory);

        userStory.setStatus(UserStoryStatus.VOTED);
        userStoryRepository.save(userStory);

        sseEmitterMessagesManager.dispatchMessage(userStory.getSession().getMembers(), new UserStoryDto(userStory));
    }

    public SseEmitter subscribeToUserStory(Long userStoryId) {
        UserStory userStory = findUserStoryOrThrowException(userStoryId);
        checkUserStoryIsVisible(userStory);

        SseEmitter sseEmitter = new SseEmitter(ONE_HOUR);
        sseEmitterMessagesManager.addEmitter(MemberThreadLocalManager.getCurrentMember().getId(), sseEmitter);
        return sseEmitter;
    }

    public void voteUserStory(Long id, String card) {
        UserStory userStory = findUserStoryOrThrowException(id);
        checkUserStoryIsVisible(userStory);
        checkUserStoryAllowedToBeVoted(userStory);

        Set<StoryVote> storyVotes = userStory.getStoryVotes();
        checkUserVoteExists(storyVotes);
        storyVotes.add(voteService.createNewVote(userStory, card));

        userStoryRepository.save(userStory);

        sseEmitterMessagesManager.dispatchMessage(userStory.getSession().getMembers(),
                new UserStoryVoteStats(id, MemberThreadLocalManager.getCurrentMember().getNickName(), storyVotes.size()));
    }

    public UserStory findUserStoryOrThrowException(Long id) {
        return userStoryRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceExceptionEnum.USER_STORY_NOT_FOUND));
    }

    private void checkMemberAllowedToChangeUserStoryStatus(UserStory userStory) {
        Member currentMember = MemberThreadLocalManager.getCurrentMember();
        if (!userStory.getSession().getCreator().getId().equals(currentMember.getId())) {
            throw new ServiceException(ServiceExceptionEnum.USER_STORY_STATUS_CHANGE_NOT_ALLOWED);
        }
    }

    private void checkUserVoteExists(Set<StoryVote> storyVotes) {
        Member currentMember = MemberThreadLocalManager.getCurrentMember();
        if (storyVotes.stream().anyMatch(storyVote -> storyVote.getMember().getId().equals(currentMember.getId()))) {
            throw new ServiceException(ServiceExceptionEnum.USER_STORY_ALREADY_VOTED);
        }
    }

    private void checkUserStoryIsVisible(UserStory userStory) {
        Member currentMember = MemberThreadLocalManager.getCurrentMember();
        if (userStory.getSession().getMembers().stream().noneMatch(member -> member.getId().equals(currentMember.getId()))) {
            throw new ServiceException(USER_STORY_NOT_VISIBLE);
        }
    }

    private void checkUserStoryAllowedToBeVoted(UserStory userStory) {
        if (userStory.getStatus() != UserStoryStatus.VOTING) {
            throw new ServiceException(ServiceExceptionEnum.USER_STORY_IS_NOT_VOTING);
        }
    }

    private void checkUserStoryNotCancelled(UserStory userStory) {
        if (userStory.getStatus() == UserStoryStatus.CANCELLED) {
            throw new ServiceException(ServiceExceptionEnum.USER_STORY_IS_CANCELLED);
        }
    }
}
