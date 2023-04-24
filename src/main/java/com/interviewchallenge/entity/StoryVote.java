package com.interviewchallenge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "story_votes")
@Getter
@Setter
public class StoryVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "story_id")
    private UserStory userStory;
    @OneToOne
    private Member member;
    private String card;
}
