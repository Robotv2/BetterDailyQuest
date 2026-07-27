---
description: Reference every supported BetterDailyQuest config.yml section and value.
---

# `config.yml` reference

`config.yml` controls the Quest Board, global storage, cosmetics, reset-time formatting, command messages, and debug output.

## Top-level sections

| Path | Type | Bundled value | Use |
| --- | --- | --- | --- |
| `debug` | Boolean | `true` | Add BDQ diagnostic messages |
| `quest-board` | Section | Six-row daily, weekly, and monthly layout | Configure the built-in player board |
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

## Quest Board

`bdq quests` opens the command sender's board. Group layouts are rendered in configuration order; assignments use case-insensitive Quest ID order. Completed assignments stay visible until the group refresh removes them.

The bundled layout reserves five daily slots, three weekly slots, and one monthly slot. The matching group files assign the same maximum counts, so a fresh installation cannot hide assignments through insufficient board capacity.

| Path | Type | Use |
| --- | --- | --- |
| `quest-board.title` | Text, at most 32 characters | Inventory title |
| `quest-board.rows` | Integer from `1` to `6` | Inventory size |
| `quest-board.filler-item` | Item section | Fill unused inventory slots |
| `quest-board.status.waiting` | Text | Waiting assignment label |
| `quest-board.status.started` | Text | Started assignment label |
| `quest-board.status.completed` | Text | Completed assignment label |
| `quest-board.status.unavailable` | Text | Missing quest-content label |
| `quest-board.groups.<group>.slots` | Integer list | Ordered slots reserved for the group |
| `quest-board.groups.<group>.quest-item` | Item section | Assignment item |
| `quest-board.groups.<group>.empty-item` | Item section | Unused group slot item |

Each item section has an XSeries `material` and `name`. `quest-item` also has a `lore` list. Existing quest, assignment, task, and PlaceholderAPI values work in item names and lore. Two exact lore markers expand into multiple lines:

- `%quest_description%` inserts the quest's `description` list.
- `%task_descriptions%` inserts nonblank `task_description` values in numeric Task ID order.

`%quest_status%` is board-only. The board never displays executable quest or task reward actions.

Every loaded Quest Group needs a layout. Rows, title length, current-server materials, slot bounds, repeated or overlapping slots, and group coverage are checked on startup and `bdq reload`. An invalid layout disables only the board and logs each error. A board also refuses to open instead of hiding assignments when any group has more assignments than slots.

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
| `quest_board_unavailable` | None |
| `quest_board_start_denied` | None |

Keep the `messages` section present. Missing command keys use built-in English fallback messages.
