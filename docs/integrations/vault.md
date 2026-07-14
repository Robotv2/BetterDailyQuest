# Vault player roles

Vault lets BetterDailyQuest ask a permissions provider for a player's primary role and choose a role-specific assignment limit.

## Requirements

- Vault
- A Vault-compatible permissions provider
- Primary-role lookup supported by that provider

## Configure limits

```yaml
global-assignment-limit: 1
assignment-limits:
  default: 1
  vip: 2
  moderator: 3
```

Role keys are matched without case. The value must be the primary role name returned through Vault, not a permission node.

## Fallback behavior

- If Vault is unavailable, BetterDailyQuest uses `default` as the role name.
- If the permissions provider cannot return a primary role, it uses `default`.
- If the selected role has no entry, `global-assignment-limit` is used.

## Verify

1. Join with an ordinary test player and a VIP test player.
2. Allow the group to fill on player load or refresh.
3. Confirm each player receives the configured number of eligible quests.
4. Check logs for Vault integration warnings and player-load errors.

Related: [Assignment limits](../concepts/assignment-limits.md) · [Rerolls and limits](../administration/rerolls-limits.md)
