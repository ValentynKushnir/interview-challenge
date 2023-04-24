package com.interviewchallenge.dto;

import lombok.Data;

@Data
public class SessionCreationRequest {

    private String title;
    private DeckType deckType;

}
