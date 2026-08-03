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
| `bdq quests` | None | Command sender | Board configuration invalid or assignment count exceeds configured slots |
| `bdq reload` | `betterdailyquest.command.reload` | None | Invalid content rejects the reload and keeps the previous runtime active |
| `bdq give <group> <quest> [player]` | `betterdailyquest.command.give` | Online; defaults to sender | Already assigned or completed non-repeatable quest |
| `bdq clear <player> <questID>` | `betterdailyquest.command.clear` | Online and loaded | Assignment not found |
| `bdq reset <player> <questID>` | `betterdailyquest.command.reset` | Online and loaded | Assignment or quest content unavailable |
| `bdq start <questID>` | `betterdailyquest.command.start` | Command sender | Assignment unavailable, complete, or already started |
| `bdq start-others <player> <questID>` | `betterdailyquest.command.start.others` | Online and loaded | Assignment unavailable, complete, or already started |
| `bdq reroll <questID>` | `betterdailyquest.command.reroll` | Command sender | Limit reached or no different quest |
| `bdq reroll-others <player> <questID>` | `betterdailyquest.command.reroll.others` | Online and loaded | Limit reached or no different quest |
| `bdq complete <player> <questID>` | `betterdailyquest.command.complete` | Online and loaded | Assignment unavailable or already complete |

## Behavior

- `give` creates a new assignment. It does not fill the whole group.
- `reload` validates first-party configuration and content before replacing the running state.
- `clear` deletes the assignment and task progress without recording completion.
- `reset` restarts the same quest with fresh progress.
- `start` lets a player begin a waiting assignment created with `need-starting: true`.
- `quests` opens the sender's protected inventory board. Left-clicking a waiting quest uses the same start transition and requires `betterdailyquest.command.start`.
- `start-others` lets staff or the console start a waiting assignment for an online, loaded player.
- `reroll` chooses a different eligible quest from the same group.
- `complete` finishes tasks and runs configured completion events and rewards.
- Quest IDs are matched without case, but using the configured spelling is clearer in logs.
- The self `start` command requires a player sender. The console must use `start-others`.
- The `quests` command also requires a player sender and cannot view another player's assignments.

See [Use commands and permissions](../administration/commands-permissions.md) for staff-role guidance.
