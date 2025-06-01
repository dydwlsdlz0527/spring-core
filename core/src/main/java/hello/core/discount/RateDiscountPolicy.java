package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;

public class RateDiscountPolicy implements DiscountPolicy {

    private final int discountRate = 10; // 10% 할인

    @Override
    public int discount(Member member, int price) {
        // 할인 정책: VIP 회원에게만 할인 적용
        if (member.getGrade() == Grade.VIP) {
            return price * discountRate / 100;
        }
        return 0; // VIP가 아니면 할인 없음
    }

}
