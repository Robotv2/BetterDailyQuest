---
description: Reference every supported BetterDailyQuest config.yml section and value.
---

# `config.yml` reference

`config.yml` controls global storage, cosmetics, reset-time formatting, command messages, and debug output.

## Top-level sections

| Path | Type | Bundled value | Use |
| --- | --- | --- | --- |
| `debug` | Boolean | `true` | Add BDQ diagnostic messages |
| `database` | Section | SQLite setup | Choose and configure storage |
| `cosmetics` | Section | Enabled task and quest messages | Global display fallback |
| `time_format` | Section | Short English units | Format reset time |
| `messages.commands` | Section | English messages | Change command replies |

If `debug` is missing, Bukkit reads it as `false`. The bundled file currently sets it to `true`.

## Database

| Path | Type | Requirement |
| --- | --- | --- |
| `database.type` | Text | `SQLITE` or `MARIADB` |
| `database.mariadb.host` | Text | MariaDB host |
| `database.mariadb.port` | Integer | Bundled value `3306` |
| `database.mariadb.database` | Text | Existing database name |
| `database.mariadb.username` | Text | Database user |
| `database.mariadb.password` | Text | Database password |

Changing the type does not move player data. See [Configure storage and backups](../administration/storage-backups.md).

## Cosmetics

Event sections are `task_increment`, `task_done`, and `quest_done`.

| Child path | Type | Default when missing |
| --- | --- | --- |
| `action_bar.enabled` | Boolean | `false` |
| `action_bar.message` | Text | No text |
| `titles.enabled` | Boolean | `false` |
| `titles.title` | Text | No text |
| `titles.subtitle` | Text | No text |
| `titles.fade-in` | Integer ticks | `10` |
| `titles.stay` | Integer ticks | `20` |
| `titles.fade-out` | Integer ticks | `10` |

## Reset-time format

| Path | Bundled value |
| --- | --- |
| `time_format.days` | `d` |
| `time_format.hours` | `h` |
| `time_format.minutes` | `m` |
| `time_format.seconds` | `s` |
| `time_format.format` | `%days%%hours%%minutes%%seconds%` |

Supported tokens are `%days%`, `%hours%`, `%minutes%`, and `%seconds%`.

## Command messages

All keys start with `messages.commands.`.

| Key | Placeholders |
| --- | --- |
| `reload_success` | None |
| `player_not_loaded` | None |
| `quest_not_found` | None |
| `quest_already_has` | None |
| `quest_unavailable` | `%quest_id%` |
| `quest_already_completed` | None |
| `quest_already_started` | None |
| `max_rerolls_reached` | None |
| `no_quest_available` | None |
| `specify_player_for_reroll` | None |
| `give_success` | `%quest_id%`, `%player%` |
| `clear_success` | `%quest_id%`, `%player%` |
| `reset_success` | `%quest_id%`, `%player%` |
| `start_success_others` | `%quest_id%`, `%player%` |
| `start_success_self` | `%quest_id%` |
| `reroll_success_others` | `%quest_id%`, `%player%` |
| `reroll_success_self` | `%quest_id%` |
| `complete_success` | `%quest_id%`, `%player%` |

Keep the `messages` section present. Missing command keys use built-in English fallback messages.
