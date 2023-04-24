package com.interviewchallenge.controller;

import com.interviewchallenge.service.UserStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("${api.baseUrl}/userStory")
@RequiredArgsConstructor
public class UserStoryController {

    private final UserStoryService userStoryService;

    @PutMapping("/{id}/start")
    public void startUserStoryVoting(@PathVariable("id") Long id) {
        userStoryService.startUserStoryVoting(id);
    }

    @PutMapping("/{id}/stop")
    public void stopUserStoryVoting(@PathVariable("id") Long id) {
        userStoryService.stopUserStoryVoting(id);
    }

    @GetMapping("/{id}/subscribe")
    public SseEmitter subscribeToUserStory(@PathVariable("id") Long userStoryId) {
        return userStoryService.subscribeToUserStory(userStoryId);
    }

    @PutMapping("/{storyId}/vote/{card}")
    public void voteUserStory(@PathVariable("storyId") Long id, @PathVariable String card) {
        userStoryService.voteUserStory(id, card);
    }

}
