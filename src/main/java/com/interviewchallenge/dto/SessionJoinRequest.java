package com.interviewchallenge.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
public class SessionJoinRequest {

    @NonNull
    private String nickName;

}
