# Rerolls and assignment limits

## Configure rerolls

```yaml
max-rerolls: 2
```

- A positive number caps rerolls for the current assignment chain.
- `0` means no numeric cap.
- A reroll still fails when no different eligible quest exists.

Successful rerolls carry the incremented reroll count into the replacement assignment.

## Make a group rerollable

A practical group needs more eligible quests than each player holds. Consider:

- assignment limit per player;
- total quest count;
- quests already assigned;
- non-repeatable quests in completion history;
- group-specific role limits.

Example: a group with five eligible quests and an assignment limit of three leaves at most two immediate alternatives.

## Operational checks

Test the first allowed reroll, the limit boundary, a group with no replacement, and a player with completed non-repeatable quests. Confirm both self-reroll and staff reroll permissions.

Related: [Assignment limits and history](../concepts/assignment-limits.md) · [Refresh and reroll troubleshooting](../troubleshooting/refresh-reroll.md)
