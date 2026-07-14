# Commands and permissions

The root command is `betterdailyquest`; `bdq` is its alias. Arguments in angle brackets are required and brackets are not typed.

| Command | Permission | Effect |
| --- | --- | --- |
| `bdq` | None | Show the running BetterDailyQuest version |
| `bdq reload` | `betterdailyquest.command.reload` | Reload global config, groups, quests, and addon reload hooks |
| `bdq give <group> <quest> [player]` | `betterdailyquest.command.give` | Create an assignment; target defaults to the executing player |
| `bdq clear <player> <questID>` | `betterdailyquest.command.clear` | Delete one assignment and its task progress |
| `bdq reset <player> <questID>` | `betterdailyquest.command.reset` | Restart the same quest assignment with fresh progress |
| `bdq reroll <questID>` | `betterdailyquest.command.reroll` | Replace the sender's assignment with a different eligible quest |
| `bdq reroll-others <player> <questID>` | `betterdailyquest.command.reroll.others` | Reroll another online player's assignment |
| `bdq complete <player> <questID>` | `betterdailyquest.command.complete` | Complete unfinished tasks and run completion behavior |

## Permission design

- Reserve `reload`, `give`, `clear`, `reset`, `reroll-others`, and `complete` for trusted staff.
- Grant player self-reroll only when the group's `max-rerolls` and reward design make abuse acceptable.
- Console and command-block access is already privileged; still use explicit permissions for staff accounts.
- Do not grant broad wildcard permissions without reviewing future command additions.

Commands act on loaded online players. A `player_not_loaded` response means the target is offline or their data did not finish loading.

See the [command reference](../reference/commands-permissions.md) for argument and failure details.
