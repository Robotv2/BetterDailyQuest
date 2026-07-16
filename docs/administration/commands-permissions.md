---
description: Use BetterDailyQuest commands safely and give each role the correct permissions.
---

# Use commands and permissions

The root command is `betterdailyquest`. The short alias is `bdq`.

## Command overview

| Command | Permission | Main use |
| --- | --- | --- |
| `bdq` | None | Show the running version |
| `bdq reload` | `betterdailyquest.command.reload` | Reload configuration and content |
| `bdq give <group> <quest> [player]` | `betterdailyquest.command.give` | Give one quest |
| `bdq clear <player> <questID>` | `betterdailyquest.command.clear` | Delete one assignment and its progress |
| `bdq reset <player> <questID>` | `betterdailyquest.command.reset` | Restart the same quest |
| `bdq reroll <questID>` | `betterdailyquest.command.reroll` | Replace your quest with another one |
| `bdq reroll-others <player> <questID>` | `betterdailyquest.command.reroll.others` | Replace another player's quest |
| `bdq complete <player> <questID>` | `betterdailyquest.command.complete` | Complete all unfinished tasks |

## Safe permission design

- Give `reload`, `give`, `clear`, `reset`, `reroll-others`, and `complete` only to trusted staff.
- Give self-reroll to players only when group limits and rewards cannot be abused.
- Avoid broad wildcard permissions because future versions can add commands.

## Online player rule

Commands that change a player's assignment use loaded online player data. If BDQ reports that the player is not loaded, check storage errors and ask the player to reconnect.

## Important command differences

- `clear` deletes an assignment without recording completion.
- `reset` deletes current progress and creates the same quest again.
- `reroll` selects a different eligible quest from the same group.
- `complete` runs task and quest completion behavior, including rewards.

## Check permissions

Test each staff role with the exact command it needs. Also test that an ordinary player cannot use staff commands.

For exact arguments and failure cases, see [Command and permission reference](../reference/commands-permissions.md).
