---
description: Manage automatic assignments, role limits, schedules, restarts, and rerolls.
---

# Manage assignments and resets

This page explains how BDQ fills, removes, restarts, and replaces assignments.

## Assignment limit order

When BDQ fills a group, it selects the limit in this order:

1. Ask Vault for the player's primary role when Vault is ready.
2. Use the matching `assignment-limits` value.
3. Otherwise use `global-assignment-limit`.

Without Vault, the role name is `default`.

```yaml
global-assignment-limit: 1
assignment-limits:
  default: 1
  vip: 3
```

## Scheduled group refresh

```yaml
automatic-reset: "0 0 * * *"
```

At the scheduled time, BDQ removes assignments from that group. When `automatically-given` is `true`, loaded players are filled again to their current limit. Offline player data is checked when the player joins.

The scheduler uses the server JVM time zone. Test the time on a private server before changing a live group.

## Start a waiting assignment

Assignments created with `need-starting: true` ignore progress until they are started:

```text
bdq start questID
bdq start-others PlayerName questID
```

The first command is player-only. The second works for trusted staff and the console when the target player is online and loaded. Starting changes only the assignment state; it does not reset task progress.

## Restart the same quest

```text
bdq reset PlayerName questID
```

This removes progress and creates a new assignment for the same Quest ID. It does not choose a new quest.

## Reroll to another quest

```text
bdq reroll questID
bdq reroll-others PlayerName questID
```

A reroll excludes the current quest, quests the player already holds, and completed non-repeatable quests.

```yaml
max-rerolls: 2
```

A successful reroll carries the increased reroll count to the new assignment. A group with no different eligible quest cannot reroll, even when the number limit has not been reached.

## Test the full flow

Test progress before and after starting a waiting assignment, the first reroll, the final allowed reroll, no available replacement, a scheduled refresh, and a player with completed non-repeatable quests.

Use `%betterdailyquest_group_reset_<group>%` through PlaceholderAPI to show the time before the next scheduled refresh.
