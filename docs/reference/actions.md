---
description: Reference every supported BetterDailyQuest reward action and its sender behavior.
---

# Reward action reference

Every reward action starts with a prefix.

| Prefix | Argument | Runs as | Offline behavior |
| --- | --- | --- | --- |
| `[player]` | Command without `/` | Completing player | Skipped |
| `[console]` | Command without `/` | Server console | Runs with the saved player name |
| `[message]` | Message text | Direct player message | Skipped |
| `[close]` | No argument | Closes the inventory | Skipped |
| `[sound]` | XSeries sound expression | Plays to the player | Skipped |

## Complete example

```yaml
rewards:
  - "[console] give %player% diamond 1"
  - "[message] &6You earned a diamond."
  - "[sound] ENTITY_PLAYER_LEVELUP,1,1"
  - "[close]"
```

Actions run in list order. Prefix matching ignores case. An unknown prefix creates a warning and does not run as a command.

## Safety

- `[console]` can run any server command.
- Do not insert untrusted text into console commands.
- Check the final command after placeholders are replaced.
- Test player commands with the player's real permissions.

Available internal placeholders depend on whether the list belongs to a task or quest. See [Placeholder and cosmetic reference](placeholders-cosmetics.md).
