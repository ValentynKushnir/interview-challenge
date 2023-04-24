package com.interviewchallenge.dto;

import com.interviewchallenge.entity.UserStory;
import com.interviewchallenge.entity.UserStoryStatus;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserStoryDto {

    private Long id;
    private String name;
    private UserStoryStatus status;
    private Set<StoryVoteDto> storyVotes;

    public UserStoryDto(UserStory userStory) {
        this.id = userStory.getId();
        this.name = userStory.getName();
        this.status = userStory.getStatus();
        this.storyVotes = userStory.getStoryVotes().stream()
                .map(vote -> new StoryVoteDto(vote, userStory.getStatus() == UserStoryStatus.VOTED))
                .collect(Collectors.toSet());
    }
}
