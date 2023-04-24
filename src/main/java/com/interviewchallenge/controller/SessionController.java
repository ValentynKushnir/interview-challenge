package com.interviewchallenge.controller;

import com.interviewchallenge.dto.*;
import com.interviewchallenge.entity.Session;
import com.interviewchallenge.service.SessionManagementService;
import com.interviewchallenge.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("${api.baseUrl}/session")
@RequiredArgsConstructor
public class SessionController {

    @Value("${urls.sessionInviteLink}")
    private String inviteLinkUrl;
    @Value("${urls.sessionDestroyConfirmationPage}")
    private String destroyConfirmationPageLinkUrl;
    private final SessionManagementService sessionManagementService;
    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<SessionCreationResponse> createNewSession(@RequestBody SessionCreationRequest sessionCreationDto) {
        Session newSession = sessionManagementService.createNewSession(sessionCreationDto);
        SessionCreationResponse sessionDto = new SessionCreationResponse(newSession);
        sessionDto.setInviteLink(String.format(inviteLinkUrl, newSession.getId()));
        return ResponseEntity.ok(sessionDto);
    }

    @DeleteMapping("/{id}")
    public RedirectView destroySession(@PathVariable("id") Long id) {
        sessionManagementService.destroySessionById(id);

        RedirectView redirectView = new RedirectView(destroyConfirmationPageLinkUrl);
        redirectView.setStatusCode(HttpStatus.ACCEPTED);
        return redirectView;
    }

    @PostMapping("/{sessionId}/join")
    public ResponseEntity<SessionJoinResponse> joinSession(@PathVariable("sessionId") Long sessionId,
                                                           @Validated @RequestBody SessionJoinRequest sessionJoinRequest) {
        Session session = sessionService.joinSession(sessionId, sessionJoinRequest);
        SessionJoinResponse response = new SessionJoinResponse(session);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{sessionId}/userStory")
    public void addUserStory(@PathVariable("sessionId") Long sessionId,
                             @RequestBody AddUserStoryRequest request) {
        sessionManagementService.addUserStoryToSession(sessionId, request);
    }

    @DeleteMapping("/{sessionId}/userStory/{userStoryId}")
    public void deleteUserStory(@PathVariable("sessionId") Long sessionId,
                                @PathVariable("userStoryId") Long userStoryId) {
        sessionManagementService.removeUserStoryFromSession(sessionId, userStoryId);
    }


}
