# Command and permission reference

## Root

```text
betterdailyquest
bdq
```

Shows the running plugin version. No explicit permission.

## Administrator commands

| Syntax | Permission | Target requirement | Common failures |
| --- | --- | --- | --- |
| `bdq reload` | `betterdailyquest.command.reload` | None | Invalid content is skipped and logged |
| `bdq give <group> <quest> [player]` | `betterdailyquest.command.give` | Online/loaded; defaults to sender | Already assigned; completed non-repeatable quest |
| `bdq clear <player> <questID>` | `betterdailyquest.command.clear` | Online/loaded | Assignment not found |
| `bdq reset <player> <questID>` | `betterdailyquest.command.reset` | Online/loaded | Assignment or configured quest unavailable |
| `bdq reroll-others <player> <questID>` | `betterdailyquest.command.reroll.others` | Online/loaded | Limit reached; no alternative quest |
| `bdq complete <player> <questID>` | `betterdailyquest.command.complete` | Online/loaded | Assignment unavailable or already complete |

## Player self-reroll

| Syntax | Permission | Requirement |
| --- | --- | --- |
| `bdq reroll <questID>` | `betterdailyquest.command.reroll` | Sender must be an online player with that assignment |

## Behavioral notes

- `<group>` corresponds to the implementation `group` argument.
- Quest IDs are stored and compared without case for uniqueness, but use the configured spelling in administration.
- `reset` restarts the same assignment; it is not a different-quest reroll.
- `complete` invokes task and quest completion behavior, including configured rewards.
- `clear` deletes progress without recording completion.

Related: [Command operations](../administration/commands-permissions.md)
