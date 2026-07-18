---
description: Configure BetterDailyQuest groups, shared options, limits, schedules, and rerolls.
---

# Configure quest groups

A quest group controls shared rules for related quests. Daily, weekly, training, and event quests are common group designs.

## Organize group files

Store group files in:

```text
plugins/BetterDailyQuest/groups/
```

For one group per file, the file name becomes the group ID:

```text
groups/daily.yml -> group ID daily
```

Use lowercase file names with hyphens. Keep backup files outside the live `groups` folder.

## Basic group

```yaml
options:
  repeatable: false
  sequential-tasks: false
  need-starting: false
  automatically-given: true

automatic-reset: "0 0 * * *"
global-assignment-limit: 1
max-rerolls: 2
```

## Shared options

| Option | Default | Use |
| --- | --- | --- |
| `repeatable` | `false` | Allow completed quests to be selected again |
| `sequential-tasks` | `false` | Require tasks to finish in number order |
| `need-starting` | `false` | Wait for a player or staff start command before progress |
| `automatically-given` | `true` | Fill the group when player data loads or the group resets |

Use `need-starting: true` when players should explicitly accept or begin an assignment. Players use `bdq start <questID>`; staff or the console use `bdq start-others <player> <questID>`.

## Assignment limits

`global-assignment-limit` is the fallback number of active quests for the group. Vault role limits can replace it:

```yaml
global-assignment-limit: 1
assignment-limits:
  default: 1
  vip: 3
```

The group still needs enough eligible quests. A limit of three cannot create three assignments when only two quests are available.

## Reset schedule

`automatic-reset` uses five cron fields: minute, hour, day of month, month, and day of week.

```yaml
automatic-reset: "0 0 * * *"
```

This example runs every day at midnight in the server JVM time zone.

## Reroll limit

`max-rerolls` limits successful rerolls in one replacement chain. A value of `0` means no number limit. A reroll still needs a different eligible quest.

## Apply and check

Run `bdq reload`, then check that the group loaded without warnings. Test schedules and automatic assignment on a private server before using them with production data.

For every key and default, see [Quest group reference](../reference/quest-groups.md).

## Next step

[Create quests and tasks](quests-tasks.md).
