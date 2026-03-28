# Application Module — Testing Philosophy

## High-level Principles

- **Contract-driven:** tests MUST assert response shape and HTTP status codes against the documented API contract (OpenAPI spec).
- **Isolated from infrastructure:** use mocked use-cases and deterministic fakes (e.g. WireMock for external HTTP) — never hit a real database or external service.
- **Fast and deterministic:** every test MUST produce the same result on every run, with no dependency on execution order.
- **HTTP-level entry point:** tests drive the system through the HTTP layer using RestAssured or a lightweight HTTP client. Do not call controllers directly.

## Test Structure

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateOrderApiTest {

    @MockBean
    CreateOrderUseCase createOrderUseCase;

    @Test
    void givenValidRequest_whenCreateOrder_thenReturns201WithOrderId() {
        // Given
        var command = new CreateOrderCommand(/* ... */);
        given(createOrderUseCase.execute(command)).willReturn(new OrderId("order-42"));

        // When / Then
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(/* request body */)
        .when()
            .post("/orders")
        .then()
            .statusCode(201)
            .body("orderId", equalTo("order-42"));
    }
}
```

## What to Assert

- HTTP status code (always).
- Response body shape and key fields.
- Error response format for invalid inputs (4xx).
- Content-Type header when relevant.

## What NOT to Assert

- Internal domain logic — that belongs in domain tests.
- Database state — use domain and infrastructure tests for persistence.

## TDD Cycle in the Application Layer

1. **Red 🔴** — Write a failing API-level test describing the endpoint contract.
2. **Green 🟢** — Implement the controller and wire the use case to make the test pass.
3. **Refactor ⚪** — Clean up request mapping, response formatting, and validation logic.

## Test Naming Convention

```
given<Precondition>_when<HttpAction>_then<ExpectedHttpOutcome>
```

Example: `givenOutOfStockItem_whenCreateOrder_thenReturns422WithError`
