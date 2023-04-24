package com.interviewchallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStoryVoteStats {

    private Long userStoryId;
    private String votedMemberNickName;
    private Integer numberOfVotes;

}
