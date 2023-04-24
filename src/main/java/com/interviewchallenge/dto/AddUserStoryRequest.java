package com.interviewchallenge.dto;

import lombok.Data;

@Data
public class AddUserStoryRequest {

    private String storyId;
    private String description;

}
