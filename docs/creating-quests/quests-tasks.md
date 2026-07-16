---
description: Create BetterDailyQuest quest files with valid task types, targets, and amounts.
---

# Create quests and tasks

A quest file defines what players must do and what happens when they finish.

## Organize quest files

Store quest files in `plugins/BetterDailyQuest/quests/`. One file can hold one quest or several quests under a `quests` section.

```text
quests/
├── daily-mining.yml
├── daily-combat.yml
└── weekly.yml
```

Quest IDs must be unique across all files and groups.

## Basic quest

```yaml
quests:
  stonebreaker:
    name: "&eStonebreaker"
    group: "daily"
    tasks:
      1:
        task_type: "BREAK"
        required_amount: 10
        required_targets:
          - STONE
          - COBBLESTONE
```

Use spaces for indentation. Lists use a dash. Text with color codes or special characters should be inside quotes.

## Task questions

Every task answers three questions:

1. What activity counts? Use `task_type`.
2. What must match? Use a target field when the type needs one.
3. How much is needed? Use `required_amount` for counted types.

## Target forms

```yaml
# One target
required_target: STONE

# Any target in a list
required_targets:
  - STONE
  - COBBLESTONE

# All values except TNT
required_targets:
  - "*"
  - "!TNT"

# A supported XSeries tag
required_targets:
  - "TAG:LOGS"
```

Target names must exist on the running server version. New materials cannot load on old Minecraft versions.

## Amount ranges

Most task types can choose a random required amount for each assignment:

```yaml
required_amount: "8-12"
```

Use a fixed number when players should receive the same goal.

## Sequential tasks

Set `sequential-tasks: true` on the quest or group when task 2 must wait for task 1.

```yaml
options:
  sequential-tasks: true
```

Tasks are ordered by numeric task IDs. Progress made before an earlier task finishes is not added later.

## Apply and check

Run `bdq reload`. If a quest is skipped, check the first warning for that Quest ID. Common causes are a missing group, duplicate Quest ID, non-number task ID, invalid task type, or invalid target.

Use [Task type overview](../reference/task-types.md) to choose the right type.

## Next step

[Control quest assignment](assignments.md).
