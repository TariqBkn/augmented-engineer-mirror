# Java Coding Guidelines

This document defines the Java coding conventions and best practices for the project. Follow these rules to keep the codebase consistent, readable, and maintainable.

## Formatting

- **Indentation:** 4 spaces per indent level. Do not use tabs.
- **Line length:** Prefer max 120 characters.
- **Braces:** Always use K&R style (opening brace on same line): `if (cond) {`.

Recommended tooling: use `google-java-format` or `spotless` configured in Gradle.

## Naming Conventions

- **Packages:** All lower-case, reverse-domain style: `com.exalt.it.belairdomain`.
- **Classes / Interfaces / Enums:** PascalCase: `OrderService`, `MenuItem`.
- **Methods:** camelCase, verb-based: `calculateTotal()`.
- **Variables / Parameters / Fields:** camelCase: `orderItems`, `totalAmount`.
- **Constants:** UPPER_SNAKE_CASE and `static final`: `DEFAULT_TAX_RATE`.

## Java Records

Use `record` types for concise, immutable data carriers (DTOs, value objects):

```java
public record Money(BigDecimal amount, Currency currency) {
    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
    }
}
```

## Constructor Injection

```java
@Service
public class OrderService {
    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }
}
```

## Error Handling

- Use checked exceptions for recoverable conditions.
- Use runtime exceptions for programming errors and unrecoverable states.
- Prefer specific exception types to provide context.

## Null Handling

- Avoid returning `null` from public methods. Prefer `Optional<T>` for optional results.
- Validate public method arguments using `Objects.requireNonNull()`.

## Logging

- Use `org.slf4j.Logger`: `private static final Logger LOGGER = LoggerFactory.getLogger(MyClass.class);`
- Never log sensitive information.

## Build & Dependencies

- Use Gradle for builds. Keep dependency versions centralized in `gradle/libs.versions.toml`.

## References & Further Reading

- Google Java Style Guide
- Effective Java, Joshua Bloch