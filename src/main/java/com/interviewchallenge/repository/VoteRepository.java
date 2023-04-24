package com.interviewchallenge.repository;

import com.interviewchallenge.entity.StoryVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<StoryVote, Long> {
}
