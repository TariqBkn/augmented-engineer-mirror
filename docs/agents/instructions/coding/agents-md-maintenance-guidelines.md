# AGENTS.md Maintenance Guidelines

This document defines how to write, update, and improve AGENTS.md files and other agent instruction files in this repository.

## Purpose

AGENTS.md files are system prompts loaded automatically by code agents. They define the agent's persona, context, and behaviour. Keeping them accurate, concise, and well-structured is critical to getting consistent, high-quality output.

## General Principles

- **Clarity over completeness.** Prefer short, unambiguous instructions over exhaustive documentation. The agent will not read what it cannot fit in its context window.
- **Hierarchy awareness.** Root AGENTS.md sets global rules. Sub-directory AGENTS.md files refine or override for their scope. Avoid duplicating instructions already covered at a higher level.
- **Offload aggressively.** Long reference material (testing strategies, coding conventions, templates) belongs in dedicated files under `docs/agents/instructions/`. Reference them from AGENTS.md rather than inlining them.
- **Keep it current.** An outdated AGENTS.md is worse than none. When you change architecture, tooling, or conventions, update the relevant instruction file immediately.

## Structure of a Good AGENTS.md

```markdown
# [Optional: Persona]
Short description of the agent's role and personality.

## Core Guidelines
Critical rules the agent must always follow (context markers, active partner, etc.)

## Architectural Context
High-level description of the project structure and modules.

## Repository Structure
Directory tree showing where things live.

## Development Guidelines
Links to specialised instruction files (coding, testing, git...)

## Code Review Guidelines
Link to code review instructions.

## Documentation Guidelines
Link to documentation instructions.

## AGENTS.md Maintenance Guidelines
Link to this file.
```

## When to Update

| Trigger | Action |
|---|---|
| New module or layer added | Update Architectural Context and Repository Structure |
| New tech stack or library | Update or create a coding guideline file and reference it |
| New testing strategy | Update the relevant testing instruction file |
| Agent produces consistently wrong output | Identify the gap, add or refine the relevant instruction |
| Instruction file content grows too large | Split into sub-files and reference them |

## Style Rules

- Write instructions in the imperative: "Use constructor injection", not "Constructor injection should be used".
- Be explicit about priority: use `MUST`, `SHOULD`, `AVOID` to distinguish hard rules from preferences.
- Use Markdown headers to separate sections clearly.
- Keep examples short and concrete — one good example beats three vague ones.
- When referencing files, always use relative paths from the repository root.

## Validation

After modifying any instruction file:

1. Open a new agent session (fresh context).
2. Ask the agent to summarise its understanding of the affected area.
3. If the summary misses key points, refine the instruction and repeat.
4. Commit the updated file with a `docs:` commit prefix.

## File Naming Conventions

| Type | Convention | Example |
|---|---|---|
| Module-scoped agent instructions | `AGENTS.md` | `domain/AGENTS.md` |
| Coding guidelines | `<language>-coding-guidelines.md` | `java-coding-guidelines.md` |
| Testing instructions | `<module>-testing.instructions.md` | `domain-testing.instructions.md` |
| Workflow instructions | `<topic>.instructions.md` | `development-workflow.instructions.md` |
| Templates | `<type>.md` inside `templates/` | `templates/issue.md` |
