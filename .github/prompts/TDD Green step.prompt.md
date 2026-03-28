---
agent: agent
name: TDD Green step
description: Write the minimal code to make a failing test pass. Code is written inside the test class itself — no production files created.
argument-hint: "Make the following test pass: test file {test_file_path}, method {test_method_name}"
tools: ['execute/getTerminalOutput', 'execute/runInTerminal', 'read/problems', 'read/readFile', 'read/terminalSelection', 'read/terminalLastCommand', 'edit/createDirectory', 'edit/createFile', 'edit/editFiles', 'search', 'upstash/context7/*']
model: claude-sonnet-4-20250514
---

# TDD Green Step

## Input

You will receive:
- `test_file_path`: path to the test file containing the failing test
- `test_method_name`: name of the specific test method to make pass

Read the test file before doing anything else.

## Instructions

1. Read the failing test carefully. Understand exactly what it expects.
2. Run the test to confirm it currently fails.
3. Write the **minimal** code required to make the test pass.
   - **CRITICAL: Write ALL production code inside the test class itself.** Do NOT create any new production files.
   - Use inner classes, inner records, or static methods inside the test class if needed.
   - Do not implement anything beyond what the test strictly requires.
4. Run the test again and confirm it passes.
5. Confirm no other existing tests have been broken.
6. Output a structured JSON summary (see Output Format below).

## Requirements

- **CRITICAL: NEVER create a new production class, interface, or file.** All code goes inside the test class.
- **NEVER modify the test method itself.** The test is the specification — it must not change.
- **NEVER modify other test methods** in the same file.
- **NEVER add logic not required by the test.** If the test does not assert it, do not implement it.
- The test MUST pass after your changes.
- If the test still fails after your attempt, try again. Do not stop until it passes.

## Output Format

Once the test passes, output the following JSON:

```json
{
  "test_file": "domain/src/test/java/com/it/exalt/belair/domain/commande/PasserCommandeUseCaseTest.java",
  "test_method": "givenSufficientStock_whenPasserCommande_thenCommandeCreatedEnAttente",
  "layer": "domain",
  "production_code_location": "inside test class — inner classes and methods",
  "classes_to_extract": [
    "PasserCommandeUseCase",
    "PasserCommandeCommand",
    "PasserCommandeFixture",
    "StockArticle"
  ],
  "test_status": "GREEN"
}
```

This JSON is the input for the next step: `/TDD Refactor step`.

## Examples

### ✅ Positive example

Failing test expects `PasserCommandeUseCase.execute()` to return a commande with status `EN_ATTENTE`.

**CORRECT — write inside the test class:**

```java
class PasserCommandeUseCaseTest {

    // Inner classes written here to make the test pass
    record PasserCommandeCommand(String festivalierId, List<LigneCommande> lignes) {}
    record LigneCommande(String articleId, int quantite) {}
    record Commande(String id, StatutCommande statut) {}
    enum StatutCommande { EN_ATTENTE, ACQUITTEE, ANNULEE }

    static class PasserCommandeUseCase {
        Commande execute(PasserCommandeCommand command) {
            return new Commande(UUID.randomUUID().toString(), StatutCommande.EN_ATTENTE);
        }
    }

    private PasserCommandeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new PasserCommandeUseCase();
    }

    @Test
    void givenSufficientStock_whenPasserCommande_thenCommandeCreatedEnAttente() {
        // Given / When / Then — unchanged
    }
}
```

---

### ❌ Negative example — DO NOT create production files

```
// WRONG: creating this file during the Green step
src/main/java/com/it/exalt/belair/domain/commande/PasserCommandeUseCase.java

// WRONG: creating this file during the Green step
src/main/java/com/it/exalt/belair/domain/commande/Commande.java
```

Production files are created in the **Refactor step only**. Never here.

---

### ❌ Negative example — DO NOT modify the test

```java
// WRONG: changing the assertion to make the test pass
// Original:
assertThat(result.statut()).isEqualTo(StatutCommande.EN_ATTENTE);
// Modified (FORBIDDEN):
assertThat(result).isNotNull();
```

The test is the specification. It must never be weakened to make it pass.

---

### ❌ Negative example — DO NOT over-implement

```java
// WRONG: implementing stock validation when the test only checks EN_ATTENTE status
static class PasserCommandeUseCase {
    Commande execute(PasserCommandeCommand command) {
        // validating stock, checking festival goer balance, sending notifications...
        // NONE of this is required by the current test
    }
}
```

Implement only what the test requires. Nothing more.
