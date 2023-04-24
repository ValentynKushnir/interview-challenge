package com.interviewchallenge.entity;

import com.interviewchallenge.dto.DeckType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sessions")
@Getter
@Setter
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(name = "deck_type")
    private DeckType deckType;
    @Enumerated(EnumType.STRING)
    @Column(name = "session_status")
    private SessionStatus sessionStatus;
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private Set<Member> members = new HashSet<>();
    @OneToMany(mappedBy = "session")
    private Set<UserStory> userStories = new HashSet<>();
    @ManyToOne
    private Member creator;

}
