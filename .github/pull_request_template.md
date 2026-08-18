## Summary

Describe the user-visible outcome.

## Verification

- [ ] The change is focused on one user-visible outcome and uses an allowed neutral branch name.
- [ ] Relevant automated tests pass.
- [ ] `gradle.properties` is unchanged, unless this is the automated release pull request.
- [ ] Administrator-facing examples were exercised against the changed behavior.
- [ ] Documentation links and YAML examples pass `python scripts/validate_docs.py`.
- [ ] `python -m mkdocs build --strict` passes.
- [ ] `python scripts/release.py validate` passes.

## Administrator documentation

- [ ] No administrator-visible behavior changed.
- [ ] Or, guided documentation and reference pages were updated in this pull request.
- [ ] Config keys, defaults, commands, permissions, placeholders, compatibility claims, and limitations match the implementation.
- [ ] New screenshots, if any, were captured from the release candidate and include useful alternative text.
