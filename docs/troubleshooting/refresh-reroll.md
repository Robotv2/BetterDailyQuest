# Refresh or reroll behaves unexpectedly

## Scheduled refresh did not run

- Confirm `automatic-reset` is present on the correct group.
- Validate the five-field cron expression.
- Confirm the server JVM time zone, not your local browser time zone.
- After editing, run `bdq reload` and confirm the group reloads.
- Keep the server running across the expected time during testing.

## Refresh removed quests but did not replace them

Automatic replacement requires `automatically-given: true`, a positive assignment limit, loaded players, and eligible quests.

## Reroll says no replacement is available

The same current quest cannot replace itself. BetterDailyQuest also excludes quests already held and completed non-repeatable Quest IDs. Compare eligible quest count with the player's current assignments.

## Reroll limit is reached too early

`max-rerolls` applies to the current replacement chain. Each successful reroll increments the count carried into the new assignment.

## `reset` did not choose a different quest

That is expected. `bdq reset` is an assignment restart for the same Quest ID. Use `reroll` for a different eligible quest.

Related: [Replacement actions](../concepts/replacement-actions.md)
