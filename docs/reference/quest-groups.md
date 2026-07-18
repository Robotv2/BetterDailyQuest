---
description: Reference BetterDailyQuest group identity, options, schedules, limits, rerolls, and cosmetics.
---

# Quest group reference

Quest groups share assignment and display rules across related quests.

## Group identity

For one group per file, the file name without `.yml` is the group ID. Group lookup ignores case.

Several groups can share one file under a `groups` section, but one group per file is easier to manage.

## Keys

| Path | Type | Default | Use |
| --- | --- | --- | --- |
| `options.repeatable` | Boolean | `false` | Allow completed quests again |
| `options.sequential-tasks` | Boolean | `false` | Require task-number order |
| `options.need-starting` | Boolean | `false` | Wait for a player or staff start command |
| `options.automatically-given` | Boolean | `true` | Fill assignments after load or reset |
| `automatic-reset` | Cron text | No schedule | Refresh the group |
| `global-assignment-limit` | Integer | `0` | Fallback assignment count |
| `assignment-limits.<role>` | Integer | None | Vault role assignment count |
| `max-rerolls` | Integer | `0` | Positive reroll limit; zero has no number limit |
| `cosmetics` | Section | Global fallback | Group display settings |

## Complete example

```yaml
options:
  repeatable: false
  sequential-tasks: true
  need-starting: false
  automatically-given: true

automatic-reset: "0 0 * * *"
global-assignment-limit: 1
assignment-limits:
  default: 1
  vip: 2
max-rerolls: 2

cosmetics:
  quest_done:
    titles:
      enabled: true
      title: "&6Quest complete"
      subtitle: "&e%quest_name%"
```

## Important points

- Start a waiting assignment with `bdq start <questID>` or `bdq start-others <player> <questID>`.
- A high limit does not create quests that do not exist.
- `max-rerolls: 0` still needs a different eligible quest.
- Use only canonical keys. Old `assignations`, `global-assignation`, and `dependant-tasks` keys are not parsed.
