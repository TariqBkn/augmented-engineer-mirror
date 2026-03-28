# Infrastructure Module — Testing Philosophy

## High-level Principles

- **Real dependencies:** infrastructure tests MUST run against real technical dependencies (databases, message brokers, etc.) managed via Testcontainers.
- **Adapter boundary:** tests validate that the infrastructure adapter correctly implements the port defined in the domain. Assert domain-level outcomes, not SQL internals.
- **Migration validation:** every test suite MUST start from a clean database with all Flyway/Liquibase migrations applied.
- **No domain logic in infrastructure tests:** if you find yourself testing business rules in an infrastructure test, move that logic to a domain test.

## Testcontainers Setup

```java
@Testcontainers
class OrderRepositoryIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("buvette_test")
        .withUsername("test")
        .withPassword("test");

    // Spring datasource auto-configured via @DynamicPropertySource
    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    OrderRepository orderRepository; // the port implementation under test

    @Test
    void givenSavedOrder_whenFindById_thenReturnsOrder() {
        // Given
        var order = /* build a domain Order */;
        orderRepository.save(order);

        // When
        var found = orderRepository.find(order.id());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().id()).isEqualTo(order.id());
    }
}
```

## What to Test

- Persistence: save and reload entities, assert field mapping correctness.
- Queries: filtering, pagination, ordering — assert correct domain objects are returned.
- Migration: assert that a clean container starts up without errors and the schema is correct.
- External adapters: use WireMock or equivalent to stub external HTTP/messaging services.

## TDD Cycle in the Infrastructure Layer

1. **Red 🔴** — Write a failing integration test against the port interface.
2. **Green 🟢** — Implement the adapter (JPA entity, mapper, repository) to make the test pass.
3. **Refactor ⚪** — Clean up mapping logic, extract helpers, ensure the container lifecycle is optimal.

## Test Naming Convention

Same as domain: `given<Precondition>_when<Action>_then<ExpectedOutcome>`

Example: `givenPersistedOrder_whenFindById_thenReturnsMappedDomainObject`
