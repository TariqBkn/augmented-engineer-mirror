---
name: create-issue
description: Create an issue in the form of a markdown file with title, context, and Gherkin acceptance criteria from a functional request. Use when needing structured, testable issues ready to be implemented in a Java hexagonal architecture project.
---

# Instructions

1. Extract context and success criteria from the request
2. Ask 2-3 clarifying questions if the request is ambiguous or underspecified
3. Identify impacted layers among: `domain`, `application`, `infrastructure`
   - **domain** : business rules, entities, value objects, domain events
   - **application** : use case orchestration, ports (primary & secondary)
   - **infrastructure** : REST adapters, persistence, schedulers, messaging
4. If more than one layer is impacted, generate **one issue per layer**. For each layer:
   1. Summarize the context specific to that layer
   2. Identify acceptance criteria specific to that layer
   3. Generate a concise, explicit title prefixed with the layer: `[Domain]`, `[Application]`, or `[Infrastructure]`
   4. Produce 3 to 5 Gherkin scenarios covering the happy path and edge cases (missing data, insufficient balance, invalid state transitions, etc.)
   5. Create the issue file at `docs/features/{feature_name}/issue-{layer}.md` using the `templates/issue.md` template
   6. Validate the issue by running: `py skills/create-issue/script-validation.py docs/features/{feature_name}/issue-{layer}.md`
   7. If validation fails, fix the issue file and re-run the validation script until it prints `VALID`

# Gherkin rules

- Every scenario must have a numbered header (e.g. `Scenario: 1 - ...`)
- Every scenario must follow the `Given / When / Then` order
- `And` steps are allowed to extend `Given`, `When`, or `Then`
- Each step must have at least one word after the keyword (no bare `Given` or `When`)
- The `Feature:` block is mandatory and must appear once per issue

# Layer guidance

| Layer | Typical content |
|---|---|
| domain | Entities, value objects, domain rules, invariants, domain events |
| application | Use case class, input/output DTOs, secondary port interfaces, error mapping |
| infrastructure | REST controller, persistence adapter, scheduler, notification gateway |

# File naming

Issues are saved as:
```
docs/features/{feature_name}/issue-domain.md
docs/features/{feature_name}/issue-application.md
docs/features/{feature_name}/issue-infrastructure.md
```

Use lowercase kebab-case for `{feature_name}`, e.g. `commande-boisson`, `transfert-tokens`.

# Notes

- This skill is intended to produce focused, implementable issues. A single issue should not span more than one layer.
- If the request is too broad (e.g. "implement the full order flow"), propose breaking it down feature by feature or layer by layer before proceeding.
- Business rules belong exclusively in the domain issue. Application and infrastructure issues reference the domain but do not restate its rules.
- Always run the validation script after creating each file. Do not consider an issue complete until the script prints `VALID`.

# Example

Input:
```
@workspace #create-issue Implémenter la fonctionnalité "Passer une commande" :
un client peut commander plusieurs articles.
La commande échoue si un article est en rupture de stock.
```

Expected output: three files, one per impacted layer:
- `docs/features/passer-commande/issue-domain.md`
- `docs/features/passer-commande/issue-application.md`
- `docs/features/passer-commande/issue-infrastructure.md`

Each validated with:
```
py skills/create-issue/script-validation.py docs/features/passer-commande/issue-domain.md
py skills/create-issue/script-validation.py docs/features/passer-commande/issue-application.md
py skills/create-issue/script-validation.py docs/features/passer-commande/issue-infrastructure.md
```
