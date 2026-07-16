---
description: Understand quest groups, quests, tasks, assignments, and saved player progress.
---

# Understand how quests work

BetterDailyQuest separates reusable quest files from each player's saved progress.

## Main parts

| Part | Meaning | Example |
| --- | --- | --- |
| Quest group | A set of quests with shared rules | `daily` |
| Quest | Reusable content that belongs to one group | `stonebreaker` |
| Task | One action needed to complete a quest | Break ten stone blocks |
| Assignment | One player's saved copy of a quest | Alex's Stonebreaker progress |
| Completion history | Quest IDs the player has finished | `stonebreaker` |

```text
Quest group: daily
└── Quest: stonebreaker
    ├── Task 1: break 10 stone blocks
    └── Task 2: break 5 coal ores

Player assignment
└── stonebreaker
    ├── Task 1 progress: 7 / 10
    └── Task 2 progress: 0 / 5
```

Two players can have the same quest with different progress.

## How progress is checked

When a player performs an action, BDQ checks:

1. The player data and assignment are loaded.
2. The assignment is started and not complete.
3. The configured quest still exists.
4. Quest conditions pass.
5. The task can progress in the current order.
6. The task type, conditions, and target match the action.
7. Progress reaches the required amount.

Task rewards run when one task finishes. Quest rewards run when all tasks finish.

## Quest IDs are saved data

Quest IDs are compared without case and must be unique across every group. Commands and player data use these IDs.

Renaming or removing a quest file does not rename saved assignments. A player can then hold an assignment that no longer has matching quest content. Plan ID changes as data changes, not simple text edits.

## Completion history

When a player completes a quest, BDQ saves its Quest ID in completion history. A completed non-repeatable quest cannot be selected again. A repeatable quest can return later.

There is no core command that removes one completion-history entry. Decide whether a quest is repeatable before publishing it.

## Next step

[Configure quest groups](quest-groups.md).
