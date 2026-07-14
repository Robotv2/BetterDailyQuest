# Changelog

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

[0.0.1]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.1
