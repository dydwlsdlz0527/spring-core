package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;

public class FixDiscountPolicy implements DiscountPolicy {

    private int discountFixAmount = 1000; // 고정 할인 금액

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return discountFixAmount; // VIP 회원에게는 1000원 할인
        } else {
            return 0; // 그 외 회원은 할인 없음
        }
    }

}
