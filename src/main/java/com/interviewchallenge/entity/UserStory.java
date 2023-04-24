package com.interviewchallenge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_stories")
@Getter
@Setter
@NoArgsConstructor
public class UserStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private UserStoryStatus status;
    @ManyToOne
    @JoinColumn
    private Session session;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<StoryVote> storyVotes = new HashSet<>();

    public UserStory(String name, String description, Session session) {
        this.name = name;
        this.description = description;
        this.session = session;
        this.status = UserStoryStatus.PENDING;
    }

}
