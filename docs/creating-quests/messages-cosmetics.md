# Messages and cosmetics

BetterDailyQuest can display action bars and titles for task progress, task completion, and quest completion.

## Cosmetic events

- `task_increment`: a matching activity adds progress.
- `task_done`: one task reaches its requirement.
- `quest_done`: every task in the assignment is complete.

Each event can contain `action_bar` and `titles` sections.

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

## Inheritance

For task progress and task completion, the first configured section wins:

1. Task
2. Quest
3. Quest group
4. Global `config.yml`

For quest completion, the order is quest, group, then global config.

A present but disabled section still counts as the selected section. Remove the section entirely when you want a lower level to provide the behavior.

Command success and failure messages live under `messages.commands` in `config.yml`; they do not inherit from groups or quests.

Related: [config.yml reference](../reference/config.md) · [Internal placeholders](../reference/placeholders.md)
