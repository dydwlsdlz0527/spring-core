## 신규 할인 정책 적용과 문제점
새로운 할인 정책을 적용하려면 `OrderServiceImpl` 에서 이전 할인 정책을 변경해야 한다.
```java
public class OrderServiceImpl implements OrderService {
    // private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
}
```
### 문제점
- OCP, DIP 같은 객체지향 설계 원칙을 충실히 준수했다.
-> 새로운 할인 정책을 적용하려면 이전 할인 정책을 변경해야 한다.

    - DIP : 주문 서비스 클라이언트(`OrderServiceImpl`)는 `DiscountPolicy` 인터페이스에만 의존해야 하지만 현재 구현 클래스에도 의존하고 있다.
  
    - OCP : 위의 코드는 기능을 확장해서 변경하면, `OrderServiceImpl` 코드에 영향을 주기 때문에 OCP 위반이다.

![OrderService 문제점](/images/order-problems.png)

`OrderServiceImpl`이 `DiscountPolicy` 인터페이스 뿐만 아니라 `FixDiscountPolicy`인 구현 클래스도 함께 의존하고 있다. **DIP 위반**

`FixDiscountPolicy`를 `RateDiscountPolicy`로 변경하는 순간 `OrderServiceImpl`의 코드를 변경해야 한다. **OCP 위반**