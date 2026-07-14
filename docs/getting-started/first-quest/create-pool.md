# Create the quest group

**Outcome:** A `daily` group exists with manual assignment and one-assignment intent.

Open `plugins/BetterDailyQuest/groups/daily.yml` and replace its contents with:

```yaml
--8<-- "docs/examples/stonebreaker/groups/daily.yml"
```

## Why these settings

- `automatically-given: false` makes the tutorial deterministic; you assign the quest manually.
- `need-starting: false` allows progress immediately after assignment.
- `repeatable: false` prevents a completed quest from being selected again for that player.
- `automatic-reset` schedules a daily quest group refresh at midnight in the server's local time zone.
- `global-assignment-limit: 1` is the fallback group size when automatic filling is later enabled.
- `max-rerolls: 0` means no reroll limit, but one quest alone still has no alternative to select.

!!! note "Exact runtime name"
    Commands, configuration, and this guide all use **quest group**. The exact key or argument remains `group`.

## Next step

[Create Stonebreaker](create-quest.md).
