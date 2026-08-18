# Releasing BetterDailyQuest

Releases use one supported path: an automated release pull request. Do not edit the project version, create tags, or create GitHub releases manually.

## Repository settings

A maintainer must configure `master` branch protection once:

- require pull requests before merging;
- require the `Build and test / verify` and `Administrator documentation / build` checks;
- require branches to be up to date before merging;
- block direct pushes and force pushes;
- allow squash merges only and automatically delete merged branches;
- prevent administrators from bypassing the rules where practical;
- allow GitHub Actions to create and approve pull requests under **Settings → Actions → General → Workflow permissions**.

These settings turn the documented process into a merge gate. Repository instructions alone cannot enforce it.

## Add release notes

Every pull request with an administrator-visible change must update the `Unreleased` section of `CHANGELOG.md`. Use the Keep a Changelog headings: Added, Changed, Deprecated, Removed, Fixed, or Security.

Do not bump `version` in a feature or fix pull request.

## Choose when to release

Do not publish after every merged pull request. Prepare a release when a planned group of changes is complete, an urgent fix must ship, or the regular release date arrives. Keep completed user-visible changes under `Unreleased` until then.

## Prepare the release pull request

1. Merge all intended changes into `master`.
2. Open **Actions → Prepare release pull request → Run workflow**.
3. Enter a newer `major.minor.patch` version.
4. Review the generated `release/v<version>` pull request.
5. Wait for every required check and merge it without adding other changes.

The preparation workflow moves the Unreleased notes into a dated release section and updates the single version in `gradle.properties`.

## Publish

Merging the release pull request starts `.github/workflows/publish-release.yml`. It verifies that the version came from the matching release PR, then:

1. validates release metadata;
2. runs tests and builds the exact merge commit;
3. validates and builds the administrator guide;
4. creates `v<version>` on that exact commit;
5. publishes a GitHub pre-release with the versioned JAR and changelog notes.

The release event then deploys documentation from the published tag. A failed publish must be fixed in automation; do not repair it with a hand-created tag or release.
