package com.interviewchallenge.service;

import com.interviewchallenge.entity.Member;
import com.interviewchallenge.entity.StoryVote;
import com.interviewchallenge.entity.UserStory;
import com.interviewchallenge.repository.VoteRepository;
import com.interviewchallenge.util.MemberThreadLocalManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;

    public StoryVote createNewVote(UserStory userStory, String card) {
        Member currentMember = MemberThreadLocalManager.getCurrentMember();
        StoryVote storyVote = new StoryVote();
        storyVote.setUserStory(userStory);
        storyVote.setCard(card);
        storyVote.setMember(currentMember);
        return voteRepository.save(storyVote);
    }
}
