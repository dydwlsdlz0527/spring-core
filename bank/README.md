# Junit Bank Application

### Jpa LocalDateTime 자동으로 생성하는 법
- @EnableJpaAuditing 추가 (Main Class)
- @EntityListeners(AuditingEntityListener.class) 추가 (Entity Class)

```java
    @CreatedDate
    @Column(nullable=false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable=false)
    private LocalDateTime updatedAt;
```


