# Quest groups, quests, and tasks

## Quest group

A quest group is a collection of quests that shares assignment limits, reset timing, reroll limits, and default options. Configuration and commands identify the group with a `group` value.

Example: the `daily` group refreshes at midnight and intends to keep one assignment per ordinary player.

## Quest

A quest is reusable configured content. It has a globally unique Quest ID, belongs to one group, and contains one or more tasks.

Example: `stonebreaker` belongs to `daily`.

## Task

A task is one measurable requirement inside a quest. Its type decides which player activity can count, its target narrows that activity, and its required amount decides when it is complete.

Example: break ten blocks whose material is stone or cobblestone.

## Quest assignment

A quest assignment is the player-specific copy of a quest. It records who owns it, whether it can receive progress, its task progress, completion state, next quest group refresh time, and reroll count.

Changing quest YAML does not rewrite every stored assignment. If you remove or rename a quest that players still hold, the stored assignment can become unavailable until it is cleared or the content returns.

## Task progress

Task progress belongs to one assignment, not to the reusable task definition. Two players assigned Stonebreaker have independent progress.

## Relationship

```text
Quest group: daily
└── Quest: stonebreaker
    ├── Task 1: break 10 stone-like blocks
    └── Task 2: break 5 coal ores

Player Paula
└── Quest assignment: stonebreaker
    ├── Task progress 1: 7 / 10
    └── Task progress 2: 0 / 5
```

Related: [Assignment lifecycle](assignment-lifecycle.md) · [Quest and task schema](../reference/quest-schema.md)
