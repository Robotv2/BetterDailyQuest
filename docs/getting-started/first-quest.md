---
description: Create, assign, complete, and verify a first BetterDailyQuest quest.
---

# Create your first quest

**Result:** A player completes Stonebreaker by breaking ten stone blocks.<br>
**Time:** About 15 minutes.<br>
**You need:** One online test player and permission to edit YAML files and run BDQ commands.

This tutorial uses manual assignment and SQLite. It does not need an optional plugin.

## 1. Create the quest group

Open `plugins/BetterDailyQuest/groups/daily.yml` and replace its content with:

```yaml
--8<-- "docs/examples/stonebreaker/groups/daily.yml"
```

This group uses manual assignment. It also schedules a daily reset, but automatic assignment is disabled for this test.

## 2. Create Stonebreaker

Create `plugins/BetterDailyQuest/quests/stonebreaker.yml` with:

```yaml
--8<-- "docs/examples/stonebreaker/quests/stonebreaker.yml"
```

Important values:

- `stonebreaker` is the Quest ID. Quest IDs must be unique across all groups.
- `group: "daily"` connects the quest to the group created above.
- `BREAK` listens for blocks broken by the assigned player.
- `required_amount: 10` means that ten matching events are needed.
- `STONE` and `COBBLESTONE` are the only accepted targets.

Use spaces for YAML indentation. Do not use tab characters.

## 3. Reload the content

Run:

```text
bdq reload
```

Check the console. The `daily` group and `stonebreaker` quest should load without a warning.

If the quest does not load, check:

1. Both files end with `.yml`.
2. The indentation uses spaces.
3. The quest group is named `daily`.
4. No other file uses the `stonebreaker` Quest ID.
5. The task ID is a number.
6. `BREAK`, `STONE`, and `COBBLESTONE` are written as shown.

## 4. Give the quest to a player

Run this command from the console:

```text
bdq give daily stonebreaker PlayerName
```

Replace `PlayerName` with the exact name of an online player. The player must be online and their data must be loaded.

## 5. Complete the quest

As the assigned player, break any mix of ten stone or cobblestone blocks.

The player should receive these messages:

```text
Task complete: 10 blocks broken.
Stonebreaker complete!
```

!!! success "Quest checkpoint"
    Both messages appear once. Breaking more blocks does not complete the finished assignment again.

If progress does not increase, confirm that the player has the assignment and is breaking the exact target blocks. Also check whether another plugin cancels the block event.

## 6. Check saved data

Stop and start the server cleanly. Confirm that BetterDailyQuest starts and the player's completed quest history is still present when later assignment rules use it.

## Next step

Read [Understand how quests work](../creating-quests/how-it-works.md), then use the [Quest recipe book](../creating-quests/recipes.md) for more examples.
