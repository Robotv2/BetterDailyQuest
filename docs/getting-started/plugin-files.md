# Plugin files

BetterDailyQuest separates global behavior, quest groups, quests, storage, and addons.

| Location | Purpose | Back up? |
| --- | --- | --- |
| `plugins/BetterDailyQuest/config.yml` | Storage, global cosmetics, time formatting, and command messages | Yes |
| `plugins/BetterDailyQuest/groups/` | Quest group options, limits, schedules, and group cosmetics | Yes |
| `plugins/BetterDailyQuest/quests/` | Quests, tasks, conditions, rewards, and quest cosmetics | Yes |
| Database or SQLite files | Assignments, task progress, reroll count, and completion history | Yes |
| `plugins/BetterDailyQuest/addons/` | Separately installed addon JARs and addon data | Yes, if used |
| `plugins/BetterDailyQuest/.libs/` | Runtime-managed libraries | No; do not edit |

Group and quest files may contain one object or a wrapper section containing several objects. For maintainability, use one group per file and group related quests into clearly named files.

See [Organize quest files](../creating-quests/file-organization.md).
