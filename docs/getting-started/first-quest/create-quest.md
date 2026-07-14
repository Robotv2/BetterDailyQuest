# Create Stonebreaker

**Outcome:** The `daily` group contains one quest with one block-breaking task.

Create `plugins/BetterDailyQuest/quests/stonebreaker.yml` with:

```yaml
--8<-- "docs/examples/stonebreaker/quests/stonebreaker.yml"
```

## What it means

- `stonebreaker` is the globally unique Quest ID used by commands and stored assignments.
- `group: "daily"` attaches the quest to the `daily` group.
- `BREAK` listens for blocks broken by the assigned player.
- `required_amount: 10` completes the task after ten matching events.
- Only `STONE` and `COBBLESTONE` match.
- The task message runs first; the quest message runs after every task is complete.

Use spaces, not tab characters, for YAML indentation. Material and entity names are uppercase identifiers.

## Next step

[Reload, assign, and verify](verify.md).
