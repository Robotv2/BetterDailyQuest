# `config.yml` reference

## Top-level sections

| Path | Type | Default/requirement | Effect |
| --- | --- | --- | --- |
| `debug` | Boolean | `false` when absent | Enables additional BetterDailyQuest diagnostic messages |
| `database` | Section | Required | Selects SQLite or MariaDB storage |
| `cosmetics` | Section | Required by bundled config | Global progress/completion presentation fallback |
| `time_format` | Section | Required | Formats reset countdown placeholders |
| `messages.commands` | Section | Command strings have built-in fallbacks | Customizes core command responses |

## Database

| Path | Type | Default/requirement | Notes |
| --- | --- | --- | --- |
| `database.type` | Text | Bundled default `SQLITE` | Supported guide values: `SQLITE`, `MARIADB` |
| `database.mariadb.host` | Text | Required for MariaDB | Database host or IP |
| `database.mariadb.port` | Integer | Bundled default `3306` | MariaDB TCP port |
| `database.mariadb.database` | Text | Required for MariaDB | Existing database/schema name |
| `database.mariadb.username` | Text | Required for MariaDB | Least-privilege account |
| `database.mariadb.password` | Text | Required for MariaDB | Keep secret |

See [Storage and backups](../administration/storage-backups.md) before changing storage type.

## Cosmetics

Event paths are `cosmetics.task_increment`, `cosmetics.task_done`, and `cosmetics.quest_done`.

| Child path | Type | Default | Effect |
| --- | --- | --- | --- |
| `action_bar.enabled` | Boolean | `false` | Send action bar for this event |
| `action_bar.message` | Text | No text | Action-bar content |
| `titles.enabled` | Boolean | `false` | Send title and subtitle |
| `titles.title` | Text | No text | Main title |
| `titles.subtitle` | Text | No text | Subtitle |
| `titles.fade-in` | Integer ticks | `10` | Fade-in duration |
| `titles.stay` | Integer ticks | `20` | Visible duration |
| `titles.fade-out` | Integer ticks | `10` | Fade-out duration |

A configured but disabled section prevents fallback to a lower-precedence level. Remove the event section to inherit. See [Messages and cosmetics](../creating-quests/messages-cosmetics.md).

## Time format

| Path | Type | Bundled value | Effect |
| --- | --- | --- | --- |
| `time_format.days` | Text | `d` | Suffix for non-zero days |
| `time_format.hours` | Text | `h` | Suffix for non-zero hours |
| `time_format.minutes` | Text | `m` | Suffix for non-zero minutes |
| `time_format.seconds` | Text | `s` | Suffix for seconds |
| `time_format.format` | Text | `%days%%hours%%minutes%%seconds%` | Output template |

Supported tokens are `%days%`, `%hours%`, `%minutes%`, and `%seconds%`.

## Command messages

All paths begin with `messages.commands.` and accept color codes plus the placeholders shown below.

| Key | Placeholders |
| --- | --- |
| `reload_success` | None |
| `player_not_loaded` | None |
| `quest_not_found` | None |
| `quest_already_has` | None |
| `quest_unavailable` | `%quest_id%` |
| `quest_already_completed` | None |
| `max_rerolls_reached` | None |
| `no_quest_available` | None |
| `specify_player_for_reroll` | None |
| `give_success` | `%quest_id%`, `%player%` |
| `clear_success` | `%quest_id%`, `%player%` |
| `reset_success` | `%quest_id%`, `%player%` |
| `reroll_success_others` | `%quest_id%`, `%player%` |
| `reroll_success_self` | `%quest_id%` |
| `complete_success` | `%quest_id%`, `%player%` |

Keep the `messages` section present. Individual command keys may be omitted to use built-in English defaults.
