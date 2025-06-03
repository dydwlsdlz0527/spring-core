### AppConfig
```java
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }
}
```
- AppConfig는 애플리케이션의 실제 동작에 필요한 **구현 객체를 생성**한다.
- AppConfig는 생성한 객체 인스턴스의 참조(레퍼런스)를 생성자를 통해서 주입(연결)해준다.
- 이제 더 이상 `MemberServiceImpl`은 `MemoryMemberRepository`를 의존하지 않고 `MemoryRepository` 인터페이스만 의존한다.
- `MemberServiceImpl` 입장에서 생성자를 통해 어떤 객체가 들어올지(주입될지)는 알 수 없고, 오롯이 `AppConfig`에서 결정된다.
- `MemberServiceImpl`은 이제부터 **의존 관계에 대한 고민은 외부**에 맡기고 **실행에만 집중**하면 된다.

![AppConfig ClassDiagram](/images/appconfig-class.png)

- 객체의 생성과 연결은 `AppConfig`가 담당한다.
- **DIP 완성 :** `MemberServiceImpl`은 `MemberRepository`인 추상에만 의존하면 된다. 이제 구체 클래스는 몰라도 된다.
- **관심사의 분리 :** 객체를 생성하고 연결하는 역할과 실행하는 역할이 분리되어있다.

### 회원 객체 인스턴스 다이어그램
![AppConfig InstanceDiagram](/images/appconfig-instance.png)

- AppConfig 객체는 `memoryMemberRepository` 객체를 생성하고 그 참조값을 `memberServiceImpl`을 생성하면서 생성자로 전달한다.
- 클라이언트인 `memberServiceImpl` 입장에서 보면 의존 관계를 마치 외부에서 주입해주는 것 같다고 해서 DI(Dependency Injection) 의존관계 주입 또는 의존성 주입이라 한다.

### AppConfig를 통한 할인 정책 변경
![AppConfig Change](/images/appconfig-change.png)
```java
    // AppConfig.java
    public DiscountPolicy discountPolicy() {
        // return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
```
- 구성 영역인 AppConfig를 통해 변경할 정책을 변경하면 된다.   
  **사용 영역**의 어떤 코드도 변경할 필요가 없다.