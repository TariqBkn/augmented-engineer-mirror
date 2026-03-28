# Development Workflow Instructions

This document describes the step-by-step workflow to follow when implementing any feature in this project. It applies to all three modules: domain, application, and infrastructure.

## Guiding Principle

Work in small, validated increments. Each increment must leave the codebase in a passing state. Never accumulate unverified changes.

## The TDD Cycle

Every feature implementation follows the Red-Green-Refactor cycle:

### 🔴 Red — Write a failing test

1. Read the issue for the feature (in `docs/features/<feature-name>/`).
2. Identify the entry point to drive the behaviour (use-case handler for domain, HTTP endpoint for application, port implementation for infrastructure).
3. Write a single failing test that describes one concrete scenario from the acceptance criteria.
4. Run the test suite and confirm the new test fails for the right reason.
5. Do not write any production code yet.

### 🟢 Green — Make the test pass

1. Write the minimal production code required to make the failing test pass.
2. Do not add logic that is not yet covered by a test.
3. Run the test suite and confirm all tests pass.
4. Commit with a descriptive message following Conventional Commits.

### ⚪ Refactor — Improve without breaking

1. Review the code you just wrote for clarity, duplication, and adherence to coding guidelines.
2. Refactor freely — no behaviour changes allowed.
3. Run the test suite after every refactoring step.
4. Commit when the suite is green and the code is clean.

Repeat the cycle for each scenario in the acceptance criteria.

## Implementation Order

Work layer by layer, starting from the inside:

1. **Domain** — entities, value objects, ports, use-case implementation.
2. **Infrastructure** — port implementations (repositories, external adapters).
3. **Application** — controllers, DTOs, request/response mapping.

## Before Opening a PR

- All tests pass (`./gradlew check`).
- No unrelated changes are included.
- The commit history is clean and follows Conventional Commits.
- The feature issue file in `docs/features/` is up to date.

## Running Tests

```bash
# All modules
./gradlew check

# Single module
./gradlew :domain:test
./gradlew :application:test
./gradlew :infrastructure:test
```

## Context Markers Reference

| Emoji | Phase |
|---|---|
| 🔴 | TDD Red — writing a failing test |
| 🟢 | TDD Green — making the test pass |
| ⚪ | TDD Refactor — improving without changing behaviour |
| 💻 | General code implementation |
| 🔎 | Analysis or architecture design |
| 🕵️ | Code review |
| 📚 | Documentation |
| 🏗️ | AGENTS.md or instruction file maintenance |
