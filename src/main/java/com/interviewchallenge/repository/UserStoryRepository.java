package com.interviewchallenge.repository;

import com.interviewchallenge.entity.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoryRepository extends JpaRepository<UserStory, Long> {
}
