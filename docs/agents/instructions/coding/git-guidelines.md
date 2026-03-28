## Git Usage Guidelines

### Branching Model

- Trunk-based development: use a single shared trunk (`main`) for integration.
- Avoid long-lived feature branches. Keep branches very short-lived (hours to a few days).
- Use feature flags for work-in-progress to allow frequent merges to trunk.

### Conventional Commits

Follow the Conventional Commits specification:

```
<type>(<scope>): <short description>

<optional body>

<optional footer>
```

Common `type` values: `feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`, `chore`, `build`, `ci`.

Examples:

```
feat(payment): add support for 3DS authentication

fix(order): prevent negative totals when discounts apply

docs(readme): update contribution section
```

### Commit Message Guidelines

- Subject line: imperative, present tense, max 72 characters.
- Body: explain the motivation and contrast with previous behavior.
- Footer: reference issues or breaking changes (e.g., `Refs #123`, `BREAKING CHANGE: ...`).

### Reverting Commits

Use a revert commit rather than rewriting history on `main`:

```
git revert <commit-sha>
git push origin main
```