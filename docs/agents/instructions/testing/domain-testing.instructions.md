# Domain Module — Testing Philosophy

## High-level Principles

- **Entry point:** tests MUST drive the system through a use-case handler (the primary port implementation). Never call domain entities or services directly from tests.
- **Dependency Inversion:** tests MUST depend only on abstractions (interfaces/ports), never on concrete technical implementations.
- **No mocking frameworks:** provide lightweight, in-process fakes for secondary ports. Do not use Mockito or similar tools in domain tests.
- **Fakes implement TestState:** every fake MUST implement the `TestState<T, ID>` interface so tests can seed initial state and assert outcomes.
- **Structure:** tests MUST follow Given-When-Then (Behaviour Driven) structure with explicit comments.

## TestState Contract

```java
public interface TestState<T, ID> {
    void add(T item);
    Optional<T> find(ID id);
    List<T> findAll();
}
```

## Fake Repository Pattern

```java
public class FakeOrderRepository implements OrderRepository, TestState<Order, String> {
    private final List<Order> store = new ArrayList<>();

    @Override
    public void add(Order item) {
        store.removeIf(o -> o.id().equals(item.id()));
        store.add(item);
    }

    @Override
    public Optional<Order> find(String id) {
        return store.stream().filter(o -> o.id().equals(id)).findFirst();
    }

    @Override
    public List<Order> findAll() { return List.copyOf(store); }
}
```

## Test Structure (Given-When-Then)

```java
@Test
void givenValidBasket_whenCreateOrder_thenOrderCreatedAndPaymentRequested() {
    // Given
    fixture.state1().add(/* pre-existing entity */);

    // When
    var result = fixture.useCase().create(/* command */);

    // Then
    assertThat(result).isSuccessful();
    assertThat(fixture.state1().findAll()).hasSize(1);
}
```

## TDD Cycle

Follow the Red-Green-Refactor cycle strictly:

1. **Red 🔴** — Write a failing test that describes the expected behaviour. Do not write any production code yet.
2. **Green 🟢** — Write the minimal production code required to make the test pass. Do not over-engineer.
3. **Refactor ⚪** — Improve the code structure without changing observable behaviour. All tests must still pass.

## Test Naming Convention

```
given<Precondition>_when<Action>_then<ExpectedOutcome>
```

## Coverage Expectations

- Every use case MUST have at least one happy-path test and one test per identified edge case.
- Tests MUST be overlapping across use cases where shared invariants apply (e.g., stock validation).
- Do not aim for line coverage — aim for behaviour coverage.
