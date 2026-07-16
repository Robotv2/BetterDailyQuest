---
description: Reference BetterDailyQuest command syntax, permissions, targets, and failure cases.
---

# Command and permission reference

Arguments in `<angle brackets>` are required. Arguments in `[square brackets]` are optional. Do not type the brackets.

## Root command

```text
betterdailyquest
bdq
```

Shows the running plugin version. It has no explicit permission.

## Commands

| Syntax | Permission | Target | Common failure |
| --- | --- | --- | --- |
| `bdq reload` | `betterdailyquest.command.reload` | None | Invalid content is skipped and logged |
| `bdq give <group> <quest> [player]` | `betterdailyquest.command.give` | Online; defaults to sender | Already assigned or completed non-repeatable quest |
| `bdq clear <player> <questID>` | `betterdailyquest.command.clear` | Online and loaded | Assignment not found |
| `bdq reset <player> <questID>` | `betterdailyquest.command.reset` | Online and loaded | Assignment or quest content unavailable |
| `bdq reroll <questID>` | `betterdailyquest.command.reroll` | Command sender | Limit reached or no different quest |
| `bdq reroll-others <player> <questID>` | `betterdailyquest.command.reroll.others` | Online and loaded | Limit reached or no different quest |
| `bdq complete <player> <questID>` | `betterdailyquest.command.complete` | Online and loaded | Assignment unavailable or already complete |

## Behavior

- `give` creates a new assignment. It does not fill the whole group.
- `clear` deletes the assignment and task progress without recording completion.
- `reset` restarts the same quest with fresh progress.
- `reroll` chooses a different eligible quest from the same group.
- `complete` finishes tasks and runs configured completion events and rewards.
- Quest IDs are matched without case, but using the configured spelling is clearer in logs.

See [Use commands and permissions](../administration/commands-permissions.md) for staff-role guidance.
