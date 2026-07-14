# Sequential tasks

Set `sequential-tasks: true` when task 2 must not progress until task 1 is complete.

## Extend Stonebreaker

```yaml
options:
  sequential-tasks: true
tasks:
  1:
    task_type: BREAK
    required_amount: 10
    required_targets:
      - STONE
      - COBBLESTONE
  2:
    task_type: BREAK
    required_amount: 5
    required_targets:
      - COAL_ORE
      - DEEPSLATE_COAL_ORE
```

Tasks are ordered by their numeric task IDs. Use `1`, `2`, `3`, and so on without duplicates.

With sequential tasks enabled, coal broken before task 1 completes does not count retroactively. The player must perform the activity again after the previous task is complete.

Set the option at group level for a consistent group, or override it in a particular quest. See [Options and inheritance](../concepts/options-inheritance.md).
