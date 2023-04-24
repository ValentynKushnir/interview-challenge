package com.interviewchallenge.util;

import com.interviewchallenge.entity.Member;

public class MemberThreadLocalManager {

    private static final ThreadLocal<Member> THREAD_LOCAL = new ThreadLocal<>();

    public static void putMemberToThreadLocal(Member member) {
        THREAD_LOCAL.set(member);
    }

    public static void removeMemberFromThreadLocal() {
        THREAD_LOCAL.remove();
    }

    public static Member getCurrentMember() {
        return THREAD_LOCAL.get();
    }

    public static Long getCurrentMemberId() {
        return getCurrentMember().getId();
    }


}
