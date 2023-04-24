package com.interviewchallenge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "members")
@NoArgsConstructor
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nick_name")
    private String nickName;
    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;
    @Column(name = "session_token")
    private String sessionToken;

    public Member(String nickName) {
        this.nickName = nickName;
    }

}
