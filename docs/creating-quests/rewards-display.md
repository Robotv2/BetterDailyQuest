---
description: Configure BetterDailyQuest rewards, action bars, titles, sounds, commands, and placeholders.
---

# Add rewards and progress messages

Task rewards run when one task finishes. Quest rewards run when every task finishes.

## Add reward actions

```yaml
tasks:
  1:
    task_type: "BREAK"
    required_amount: 10
    required_target: STONE
    task_rewards:
      - "[message] &aTask complete."

rewards:
  - "[console] give %player% diamond 1"
  - "[message] &6Quest complete."
```

Actions run from top to bottom.

| Prefix | Result |
| --- | --- |
| `[player]` | The player runs a command |
| `[console]` | The server console runs a command |
| `[message]` | Send text to the player |
| `[close]` | Close the player's inventory |
| `[sound]` | Play an XSeries sound expression |

Do not put a leading slash before player or console commands.

!!! danger "Review console rewards"
    `[console]` uses full console access. Treat quest files as trusted server configuration and test the final expanded command.

## Add progress cosmetics

BDQ supports `task_increment`, `task_done`, and `quest_done` cosmetic events.

```yaml
cosmetics:
  task_done:
    action_bar:
      enabled: true
      message: "&a%quest_name% &8| &eTask complete"
    titles:
      enabled: true
      title: "&aTask complete"
      subtitle: "&e%quest_name%"
      fade-in: 10
      stay: 20
      fade-out: 10
```

For task progress and completion, BDQ uses the first configured section in this order:

1. Task.
2. Quest.
3. Quest group.
4. Global `config.yml`.

For quest completion, the order starts at quest, then group, then global config. A present but disabled section still stops lower-level fallback. Remove the section when you want to inherit another value.

## Use the correct placeholders

Internal values such as `%quest_name%`, `%task_progress%`, and `%task_required%` work in BDQ cosmetics and rewards. They are not general PlaceholderAPI values.

The reward processor also replaces `%player%` with the completing player's name.

## Verify the result

Complete the task once. Check action order, sender permissions, placeholder output, and offline behavior. Player messages, sounds, inventory closing, and player commands are skipped when the player is offline.

See [Reward action reference](../reference/actions.md) and [Placeholder and cosmetic reference](../reference/placeholders-cosmetics.md).

## Next step

[Test quest changes](testing.md).
