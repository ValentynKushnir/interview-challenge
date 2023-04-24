package com.interviewchallenge.dto;

import com.interviewchallenge.entity.Member;
import com.interviewchallenge.entity.Session;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SessionJoinResponse {

    private String sessionTitle;
    private List<UserStoryDto> userStories;
    private List<String> membersNames;

    public SessionJoinResponse(Session session) {
        this.sessionTitle = session.getTitle();
        this.userStories = session.getUserStories().stream().map(UserStoryDto::new).collect(Collectors.toList());
        this.membersNames = session.getMembers().stream().map(Member::getNickName).collect(Collectors.toList());
    }
}
