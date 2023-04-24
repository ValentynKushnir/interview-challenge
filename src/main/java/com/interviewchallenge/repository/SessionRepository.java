package com.interviewchallenge.repository;

import com.interviewchallenge.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
