package com.interviewchallenge.dto;

import com.interviewchallenge.entity.Session;
import lombok.Data;

@Data
public class SessionCreationResponse {

    private Long id;
    private String title;
    private DeckType deckType;
    private String inviteLink;

    public SessionCreationResponse(Session session) {
        this.id = session.getId();
        this.title = session.getTitle();
        this.deckType = session.getDeckType();
    }
}
