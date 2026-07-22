---
description: Reference BetterDailyQuest placeholders, action bars, titles, and cosmetic inheritance.
---

# Placeholder and cosmetic reference

BDQ uses one external PlaceholderAPI value and several internal replacement values. They are not interchangeable.

## PlaceholderAPI value

| Placeholder | Result |
| --- | --- |
| `%betterdailyquest_group_reset_<group>%` | Time until the group's next scheduled reset |

Example: `%betterdailyquest_group_reset_daily%`.

## Quest values

| Placeholder | Value |
| --- | --- |
| `%quest_id%` | Quest ID |
| `%quest_name%` | Quest name |
| `%quest_group%` | Group ID |
| `%quest_tasks%` | Number of configured tasks |

## Assignment values

Quest-completion cosmetics and quest rewards can also use:

| Placeholder | Value |
| --- | --- |
| `%quest_player%` | Online owner name or `Unknown.` |
| `%quest_started%` | `true` or `false` |
| `%quest_done%` | `true` or `false` |
| `%quest_tasks_completed%` | Completed task count |
| `%quest_reset_timestamp%` | Next reset time in milliseconds |
| `%quest_reset_time%` | Formatted time until reset |

The Quest Board also supports `%quest_status%`, using the configured waiting, in-progress, completed, or unavailable label. Board item names and lore apply internal values first, optional PlaceholderAPI values second, and BDQ color formatting last.

## Task values

Task progress and task rewards can also use:

| Placeholder | Value |
| --- | --- |
| `%task_id%` | Task ID |
| `%task_type%` | Task type |
| `%task_progress%` | Current progress |
| `%task_done%` | `true` or `false` |
| `%task_required%` | Required progress |

Reward actions replace `%player%` with the completing player's name.

## Cosmetic events

- `task_increment`
- `task_done`
- `quest_done`

Each event can contain `action_bar` and `titles`.

```yaml
cosmetics:
  task_done:
    action_bar:
      enabled: true
      message: "&a%quest_name% &8| &eTask complete"
    titles:
      enabled: true
      title: "&aTask complete"
      subtitle: "&e%quest_name%"
      fade-in: 10
      stay: 20
      fade-out: 10
```

For task events, BDQ checks task, quest, group, then global cosmetics. For quest completion, it checks quest, group, then global cosmetics.

A present but disabled section stops fallback. Remove the section to inherit the next available value.
