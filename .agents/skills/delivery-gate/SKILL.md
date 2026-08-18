---
name: delivery-gate
description: Enforces BetterDailyQuest's safe implementation, commit, pull-request, and automated-release workflow. Use whenever modifying repository files, writing commits, opening or reviewing pull requests, changing versions or changelogs, tagging, or publishing releases.
---

# Delivery gate

## Start every task

1. Read `AGENTS.md`, `CONTRIBUTING.md`, and relevant domain documentation.
2. Inspect `git status` and recent history. Preserve unrelated user changes.
3. Identify implementation, tests, administrator documentation, and changelog impact before editing.
4. Keep one task scoped to one user-visible outcome. Do not mix release preparation with implementation.
5. Use a neutral `feat/*`, `fix/*`, `docs/*`, `refactor/*`, `test/*`, `build/*`, or `chore/*` branch. Never put an agent or model name in branches or commits.

## While implementing

- Add or update tests for changed behavior.
- Update guided and reference documentation when administrator-visible behavior changes.
- Add user-visible changes beneath `## [Unreleased]` in `CHANGELOG.md`.
- Never claim planned or unverified behavior is available.
- Keep the project version unchanged. `gradle.properties` is changed only by the release workflow.

## Before reporting completion

Inspect the full diff and run:

```bash
python scripts/test_delivery.py
python scripts/test_release.py
python scripts/check_delivery.py
python scripts/release.py validate
./gradlew clean test shadowJar
python scripts/validate_docs.py
python -m mkdocs build --strict
```

If a check cannot run, report exactly which check and why. Never describe unchecked work as passing.

## Commits and pull requests

- Commit, push, or open a PR only when the user explicitly requests it.
- Iterative commits may stay on the working branch, but the completed PR must represent one coherent outcome.
- Make focused commits with concise imperative subjects; conventional-commit syntax is optional.
- Squash-merge completed PRs into one meaningful commit and delete the branch.
- Exclude generated files, secrets, local IDE state, and unrelated changes.
- Keep PR bodies focused on the outcome and relevant context.
- Do not add generic Verification or Test plan sections to PR bodies, release notes, or commit messages. Run the checks silently; mention only failures, reviewer-required manual steps, or details the user requests.
- Do not bypass failed checks or branch protection.

## Releases

Follow `RELEASING.md`. The only supported route is:

1. Put release notes under `Unreleased` in normal implementation PRs.
2. Run **Prepare release pull request** with the chosen semantic version.
3. Review and merge the isolated `release/v<version>` PR.
4. Let **Publish release** build, tag, upload, and publish the exact merge commit.

Never edit the version, create or move a tag, upload a release JAR, or publish a GitHub release manually.
