package hello.core.order;

public interface OrderService {
    /**
     * 주문을 생성하는 메서드
     *
     * @param memberId  회원 ID
     * @param itemName  주문할 아이템 이름
     * @param itemPrice 주문할 아이템 가격
     * @return 생성된 주문 객체
     */
    Order createOrder(Long memberId, String itemName, int itemPrice);

}
