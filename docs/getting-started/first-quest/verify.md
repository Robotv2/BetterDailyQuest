# Assign and verify Stonebreaker

**Outcome:** A player completes Stonebreaker and sees both configured rewards.

## Reload content

From the server console, run:

```text
bdq reload
```

Confirm that `daily` and `stonebreaker` load without a warning. If the quest is skipped, use [Quest group or quest will not load](../../troubleshooting/content-loading.md).

## Assign the quest

With the test player online, run:

```text
bdq give daily stonebreaker PlayerName
```

Replace `PlayerName` with the exact online player name.

## Complete the task

As that player, break any combination of ten stone or cobblestone blocks. The player should see:

```text
Task complete: 10 blocks broken.
Stonebreaker complete!
```

<div class="bdq-checkpoint" markdown>

**Checkpoint:** Both messages appear once, and repeating the same block-breaking action does not complete the finished assignment again.

</div>

## Common failure

If assignment succeeds but blocks do not count, confirm that the player is breaking `STONE` or `COBBLESTONE`, not a visually similar material, and check [Progress does not count](../../troubleshooting/progress.md).

## Next step

Learn how [quest groups, quests, assignments, and tasks](../../concepts/content-model.md) relate, then extend Stonebreaker with [sequential tasks](../../creating-quests/sequential-tasks.md).
