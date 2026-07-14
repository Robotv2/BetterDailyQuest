# Placeholder scopes

BetterDailyQuest has two different placeholder systems. They are not interchangeable.

## PlaceholderAPI expansion

Available to other PlaceholderAPI-aware plugins:

| Placeholder | Result |
| --- | --- |
| `%betterdailyquest_group_reset_<group>%` | Formatted time until the group's next scheduled refresh |

Example: `%betterdailyquest_group_reset_daily%`.

## Internal quest placeholders

Used in BetterDailyQuest cosmetics and reward actions:

| Placeholder | Value |
| --- | --- |
| `%quest_id%` | Globally unique Quest ID |
| `%quest_name%` | Configured quest name |
| `%quest_group%` | Quest group ID |
| `%quest_tasks%` | Number of configured tasks |

## Internal assignment placeholders

Available for quest-completion cosmetics and quest rewards, in addition to quest placeholders:

| Placeholder | Value |
| --- | --- |
| `%quest_player%` | Online owner name or `Unknown.` |
| `%quest_started%` | `true` or `false` |
| `%quest_done%` | `true` or `false` |
| `%quest_tasks_completed%` | Completed task count |
| `%quest_reset_timestamp%` | Next refresh timestamp in milliseconds |
| `%quest_reset_time%` | Formatted time until next refresh |

## Internal task placeholders

Available for task progress/completion cosmetics and task rewards, together with quest placeholders:

| Placeholder | Value |
| --- | --- |
| `%task_id%` | Numeric task ID |
| `%task_type%` | Task type literal |
| `%task_progress%` | Current progress |
| `%task_done%` | `true` or `false` |
| `%task_required%` | Required progress |

`%player%` is replaced by reward actions with the completing player's name. It is not a general PlaceholderAPI value from this expansion.

Related: [PlaceholderAPI integration](../integrations/placeholderapi.md)
