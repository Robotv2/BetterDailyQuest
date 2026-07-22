---
description: Reference every supported BetterDailyQuest quest and task key.
---

# Quest and task reference

## Quest keys

| Path | Type | Requirement | Use |
| --- | --- | --- | --- |
| Quest ID | YAML key | Required and globally unique | Command and storage identity |
| `name` | Text | Recommended | Display name for internal placeholders |
| `group` | Text | Existing group ID | Connect the quest to a group |
| `description` | Text list | Optional | Quest Board summary lines |
| `options` | Section | Group fallback | Override quest behavior |
| `conditions` | Section | Optional | Restrict all task progress |
| `cosmetics` | Section | Group/global fallback | Override display settings |
| `rewards` | Text list | Empty | Run actions after all tasks finish |
| `tasks` | Number-keyed section | Required for useful content | Define progress goals |

## Task keys

| Path | Type | Requirement | Use |
| --- | --- | --- | --- |
| Task ID | Numeric YAML key | Required and unique in the quest | Order and storage identity |
| `task_type` | Text | Required | Choose the tracked action |
| `task_description` | Text | Optional | Quest Board task progress line |
| `required_amount` | Number or range text | Default `1` for counted types | Completion amount |
| `required_target` | Text | One-target types | One accepted value |
| `required_targets` | Text list | List-target types | Accepted values and expressions |
| `required_location` | Section | `LOCATION` only | World, coordinates, and radius |
| `conditions` | Section | Optional | Restrict this task |
| `cosmetics` | Section | Higher-level fallback | Override task display |
| `task_rewards` | Text list | Empty | Run actions when the task finishes |

## Complete example

```yaml
quests:
  stonebreaker:
    name: "&eStonebreaker"
    description:
      - "&7Break ten stone or cobblestone blocks."
    group: "daily"
    rewards:
      - "[message] &6Quest complete."
    tasks:
      1:
        task_type: "BREAK"
        task_description: "&7Broken: &e%task_progress%&8/&e%task_required%"
        required_amount: 10
        required_targets:
          - STONE
          - COBBLESTONE
        task_rewards:
          - "[message] &aTask complete."
```

Target-based tasks with no target match no values. `CARVE`, `MILK`, and `SHEAR` do not need a target. `LOCATION` uses `required_location` and ignores `required_amount`.
