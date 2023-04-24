package com.interviewchallenge.service;

import com.interviewchallenge.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class SseEmitterMessagesSender {

    private final Map<Long, SseEmitter> perMemberIdSseEmitter = new HashMap<>();

    public void addEmitter(Long memberId, SseEmitter emitter) {
        perMemberIdSseEmitter.put(memberId, emitter);
    }

    public void clearEmitters() {
        perMemberIdSseEmitter.values().forEach(ResponseBodyEmitter::complete);
        perMemberIdSseEmitter.clear();
    }

    public void dispatchMessage(Set<Member> members, Object message) {
        members.forEach(member -> Optional.ofNullable(perMemberIdSseEmitter.get(member.getId())).ifPresent(emitter -> {
            try {
                emitter.send(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

}
