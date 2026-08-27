# Changelog

## [Unreleased]

## [0.0.12] - 2026-08-27

### Added

- Added `game_modes`, `biomes`, and `height` progress conditions for restricting quest progress by player context.

### Fixed

- Replaced stale version-specific README links with links to the current public releases.

## [0.0.11] - 2026-08-11

MySQL quest progress display fix.

### Fixed

- Quest progress loaded from a shared MySQL database no longer shows trailing decimal zeros or scientific notation after switching servers.

## [0.0.10] - 2026-08-05

Movement and safe reload beta release.

### Added

- Added `JUMP` tasks for counting jumps.
- Added `BOAT` and `MINECART` tasks for vehicle distance in blocks.
- Added one daily, weekly, and monthly movement quest to fresh installations.
- Added a configurable failure message for `bdq reload`.

### Fixed

- Invalid configuration or content no longer replaces the working runtime during `bdq reload`.
- Quest group schedules now start only after their content has loaded successfully.

## [0.0.9] - 2026-07-29

Movement quest types beta release.

### Added

- Added `WALK` tasks for distance traveled while walking, sprinting, or sneaking.
- Added `SWIM` tasks for swimming distance.
- Added one daily walking quest and one weekly swimming quest to fresh installations.

### Changed

- Made movement-statistic tracking safely initialize already-online players and ignore statistic resets.

## [0.0.8] - 2026-07-25

Default configuration showcase beta release.

### Added

- Added ten daily, ten weekly, and ten monthly quests to fresh installations.
- Added recurring weekly and monthly groups alongside the daily group.

### Changed

- Reworked the default Quest Board into a restrained three-group layout with five daily, three weekly, and one monthly assignment slot.
- Made fresh-install server smoke tests load and validate all thirty bundled quests.

## [0.0.7] - 2026-07-23

bStats usage metrics beta release.

### Added

- Added anonymous bStats usage metrics under plugin ID `32844`, including the configured database type.

## [0.0.6] - 2026-07-22

Built-in Quest Board beta release.

### Added

- Added the built-in `/bdq quests` inventory Quest Board with protected self-viewing and click-to-start support for permitted players.
- Added configurable board layouts, status labels, items, and board failure messages.

### Fixed

- Made `%quest_name%` fall back to the Quest ID when a quest omits its optional name.
- Persisted newly assigned quests and task rows before their progress changes.
- Avoided modern `InventoryView` ABI calls so Quest Board protection works on Paper 1.8.8.

## [0.0.5] - 2026-07-21

Condition reliability and clean-lifecycle beta release.

### Added

- Added a `permissions` progress condition with `ALL` and `ANY` matching modes.

### Fixed

- Made numerical PlaceholderAPI comparators return their numerical result instead of also requiring exact text equality.
- Rejected quests with invalid condition configuration instead of loading them without the intended restriction.
- Stopped quest-group schedulers before plugin resources close during shutdown.

### Changed

- Extended cross-version server checks to require a clean process exit without BetterDailyQuest shutdown errors.

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

[Unreleased]: https://github.com/Robotv2/BetterDailyQuest/compare/v0.0.12...HEAD
[0.0.12]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.12
[0.0.11]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.11
[0.0.10]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.10
[0.0.9]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.9
[0.0.8]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.8
[0.0.7]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.7
[0.0.6]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.6
[0.0.5]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.5
[0.0.4]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.4
[0.0.3]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.3
[0.0.2]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.2
[0.0.1]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.1
