---
description: Configure BetterDailyQuest BREAK, PLACE, and CARVE tasks.
---

# Block task reference

## `BREAK`

Counts a block broken by the assigned player.

```yaml
task_type: "BREAK"
required_amount: 10
required_targets:
  - STONE
  - COBBLESTONE
```

- **Target:** Block material.
- **Progress:** One per block.
- **Common mistake:** Use the block material, such as `DIAMOND_ORE`, not the dropped item.
- **Check:** A listed block counts; a different block does not.

## `PLACE`

Counts a block placed by the assigned player.

```yaml
task_type: "PLACE"
required_amount: 16
required_target: OAK_PLANKS
```

- **Target:** Placed block material.
- **Progress:** One per block.
- **Common mistake:** Protection plugins can cancel the event, so the placement does not count.
- **Check:** Place the target in an allowed area and in a protected area.

## `CARVE`

Counts a player using shears to turn a pumpkin into a carved pumpkin.

```yaml
task_type: "CARVE"
required_amount: 3
```

- **Target:** None.
- **Progress:** One per carve action.
- **Common mistake:** Adding a material target is not needed.
- **Check:** Use shears on a normal pumpkin.

## Target expressions

`BREAK` and `PLACE` support one target, a target list, `*`, exclusions such as `!TNT`, and supported `TAG:<name>` expressions.
