# Changelog

## [0.0.4] - 2026-07-20

Runtime dependency delivery beta release.

### Changed

- Load Anchor `v0.1.1` from JitPack on first startup instead of embedding it in the plugin JAR.
- Build against the public Anchor release without a local Anchor checkout.

## [0.0.3] - 2026-07-18

Assignment-start and runtime-hardening beta release.

### Added

- Added player and staff commands for starting waiting quest assignments.
- Added `QuestStartEvent` for addons that react when an assignment starts.

### Fixed

- Accepted modern Paper API version strings with two components, such as `26.2`.
- Restored SQLite decimal task progress correctly after a server restart.

### Changed

- Isolated and relocated bundled runtime libraries to reduce dependency conflicts.

## [0.0.2] - 2026-07-16

Documentation-focused beta release.

### Changed

- Rebuilt the administrator guide with a shorter and clearer structure.
- Unified page titles and rewrote the guide in accessible English.
- Added complete quest recipes and stronger documentation validation.
- Updated the public release and compatibility references.

## [0.0.1] - 2026-07-14

First public beta of BetterDailyQuest.

### Included

- YAML-defined quest groups, quests, tasks, conditions, rewards, and presentation actions.
- Player assignments, completion history, sequential tasks, rerolls, and scheduled quest group refreshes.
- SQLite and MariaDB storage support.
- Optional Vault role limits, PlaceholderAPI integration, and addon loading.
- An administrator guide with installation, quest creation, operations, reference, and troubleshooting paths.

### Beta limitations

- The complete player golden path and optional integrations have not been verified on every listed server version.
- The core plugin does not include a quest menu.
- `need-starting` has no core start command.
- Numerical placeholder comparator guidance is limited to exact string matching.
- Paper 26.1.2 may log a cron4j classloader warning during shutdown.

[Unreleased]: https://github.com/Robotv2/BetterDailyQuest/compare/v0.0.4...HEAD
[0.0.4]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.4
[0.0.3]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.3
[0.0.2]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.2
[0.0.1]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.1
