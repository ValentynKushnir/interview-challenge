package com.interviewchallenge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.interviewchallenge.entity.StoryVote;
import lombok.Data;

@Data
public class StoryVoteDto {

    private String nickName;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String card;

    public StoryVoteDto(StoryVote storyVote, boolean showCard) {
        this.nickName = storyVote.getMember().getNickName();
        if (showCard) {
            this. card = storyVote.getCard();
        }
    }

}
