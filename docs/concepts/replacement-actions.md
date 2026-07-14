# Refresh, restart, and reroll

These actions replace assignments for different reasons.

| Action | Scope | Replacement | Typical trigger |
| --- | --- | --- | --- |
| Quest group refresh | Every loaded player assignment in one group, plus stored assignments | New selections from that group when automatic giving is enabled | `automatic-reset` schedule |
| Assignment restart | One player's one assignment | Fresh assignment for the same quest | `bdq reset <player> <questID>` |
| Reroll | One player's one assignment | Different available quest from the same group | `bdq reroll` or `bdq reroll-others` |

## Quest group refresh

A scheduled quest group refresh removes assignments from the group. When `automatically-given: true`, BetterDailyQuest fills each loaded player back to their assignment limit using eligible quests.

## Assignment restart

The `reset` command name is retained for compatibility, but its behavior is a restart. Existing progress is deleted and a fresh assignment for the same Quest ID is created.

## Reroll

A reroll asks the same group for a different eligible quest. The current quest, quests the player already holds, and completed non-repeatable quests are unavailable. A group with only one eligible quest cannot provide a replacement.

Related: [Scheduled quest group refreshes](../administration/schedules.md) · [Rerolls and limits](../administration/rerolls-limits.md)
