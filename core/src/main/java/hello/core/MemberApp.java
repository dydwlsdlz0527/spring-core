package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;

public class MemberApp {

    public static void main(String[] args) {
        // Create a new member
        MemberService memberService = new MemberServiceImpl();
        Member member = new Member(1L, "memberA", Grade.VIP);

        // Join the member
        memberService.join(member);

        // Find the member by ID
        Member findMember = memberService.findMember(1L);

        // Print the found member's details
        System.out.println("new Member: " + member.getName());
        System.out.println("find Member: " + findMember.getName());
    }
}
