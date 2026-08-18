# Contributing to BetterDailyQuest

## Delivery workflow

Keep each pull request focused on one user-visible outcome. Use a `feat/*`, `fix/*`, `docs/*`, `refactor/*`, `test/*`, `build/*`, or `chore/*` branch. Add tests for changed behavior and record user-visible changes under `Unreleased` in `CHANGELOG.md`.

Completed pull requests are squash-merged into one meaningful commit, then their branches are deleted. Do not change `gradle.properties`, create tags, or publish releases from an implementation pull request. Releases use the automated release pull request described in `RELEASING.md`.

## Update the administrator guide

Update the guide when a change affects:

- configuration keys, values, defaults, or inheritance;
- commands, arguments, permissions, or messages;
- task types, targets, conditions, rewards, or placeholders;
- storage, integrations, updates, assignments, or resets;
- the setup or test process.

Update both the practical guide and the exact reference when both are affected. Add or update a real YAML example for configuration-driven behavior.

Do not describe planned behavior as available behavior.

## Writing style

- Use clear B1–B2 English.
- Keep sentences and paragraphs short.
- Use sentence case for titles.
- Make the navigation label match the page H1.
- Put warnings and common fixes on the related page.
- Prefer one complete example over several incomplete snippets.
- Use the same terms as the configuration and commands.

## Page structure

A practical guide should explain the result, requirements, steps, complete example, check, common problems, and next useful page.

A reference page should explain its scope, list exact values, show examples, and include important warnings.

## Local checks

```powershell
./gradlew clean test shadowJar
python scripts/test_delivery.py
python scripts/test_release.py
python scripts/check_delivery.py
python scripts/release.py validate
python -m pip install -r requirements-docs.txt
python scripts/validate_docs.py
$env:NO_MKDOCS_2_WARNING = "true"
python -m mkdocs build --strict
```

The public site is built from a published release tag. The release must contain at least one JAR file before the site is deployed.
