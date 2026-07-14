# Known limitations

## No built-in quest browser

Core BetterDailyQuest has no player menu or command that lists assignments. Use progress cosmetics and a separately verified presentation layer. [Learn more](../integrations/presentation.md).

## `need-starting` has no core start action

Setting `need-starting: true` creates assignments that cannot receive progress until started, but core exposes no start command. Keep it `false` unless a verified addon supplies the complete workflow.

## Description fields are not displayed by core

`description` and `task_description` may be consumed by addons, but core does not present them and the core PlaceholderAPI expansion does not expose them.

## PlaceholderAPI surface is intentionally small

Only the group-reset countdown is exposed to external PlaceholderAPI consumers. Quest/task placeholders documented for cosmetics and rewards are internal replacements.

## Numerical placeholder comparators are not supported guidance

The current numerical comparator path also performs exact rendered-text equality. Use exact string matching only until the behavior is corrected and verified.

## Storage-engine changes are not a migration workflow

Changing `database.type` does not guarantee transfer of existing assignments or completion history. Back up and plan migrations separately.

## Removed or renamed quests can strand assignments

Stored assignments retain Quest ID and group ID. If matching configured content disappears, commands may report the assignment as unavailable until it is cleared, refreshed, or restored.

## Compatibility evidence is currently startup-level

Paper 1.8.8 and 26.1.2 startup checks load the canonical Stonebreaker files, but release-matched gameplay and optional-integration verification remain publication gates.

## Latest Paper may log a scheduler warning during shutdown

The Paper 26.1.2 smoke run logged a cron4j `zip file closed` warning after BetterDailyQuest disabled during immediate server shutdown. Startup, SQLite initialization, and Stonebreaker loading completed successfully before shutdown, but clean scheduled-task shutdown is not yet verified on that server version. Keep the shutdown log when reporting this problem.
