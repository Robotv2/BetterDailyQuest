---
description: Control automatic assignment, repeatability, task order, and completion history.
---

# Control quest assignment

Assignment rules decide which quests a player can receive and how they begin.

## Option order

BDQ reads options in this order:

1. A value set on the quest.
2. A value set on the quest group.
3. The plugin default.

This lets a group use one rule while a special quest uses another.

```yaml
quests:
  training-stone:
    group: "training"
    options:
      repeatable: true
      sequential-tasks: true
```

## Automatic assignment

When `automatically-given` is `true`, BDQ tries to fill the group to the player's assignment limit after player data loads and after a scheduled group reset.

A quest is not eligible when:

- the player already has it;
- it is complete and not repeatable;
- it belongs to another group;
- another rule makes it unavailable.

Manual `bdq give` does not fill the whole group. It gives one selected quest.

## Repeatable quests

Use `repeatable: true` only when the quest and its rewards are safe to complete more than once. A non-repeatable Quest ID in completion history is not selected again.

## Waiting assignments

`need-starting: true` creates an assignment that cannot progress until something starts it. Core BDQ has no start command. Keep the value `false` unless a tested addon provides the full start flow.

## Check the design

Test with a new player and a player who already completed some quests. Confirm:

- the group fills to the correct limit;
- completed non-repeatable quests stay excluded;
- repeatable quests can return;
- sequential tasks do not progress early;
- rewards cannot be repeated in an unsafe way.

For role limits and reset behavior, see [Manage assignments and resets](../administration/assignments-resets.md).

## Next step

[Add progress conditions](conditions.md).
