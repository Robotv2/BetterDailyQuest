# Options and inheritance

Quest options may be set on a quest group and overridden by an individual quest.

## Precedence

1. A value set in the quest's `options` section wins.
2. Otherwise, the group's `options` value is used.
3. Otherwise, the plugin default is used.

| Option | Plugin default | Recommended use |
| --- | --- | --- |
| `repeatable` | `false` | Allow a completed Quest ID to be assigned again |
| `sequential-tasks` | `false` | Require task-number order |
| `need-starting` | `false` | Leave `false` until a supported start workflow exists |
| `automatically-given` | `true` | Group-level control for fill after refresh or player load |

`automatically-given` is meaningful as a group behavior. Do not rely on a quest-level override for automatic group filling.

## Example override

```yaml
# groups/daily.yml
options:
  repeatable: false
  sequential-tasks: false

# quests/training.yml
quests:
  training-stone:
    group: daily
    options:
      repeatable: true
      sequential-tasks: true
```

The training quest is repeatable and sequential even though the rest of the group is not.

Related: [Quest group schema](../reference/pool-schema.md) · [Quest schema](../reference/quest-schema.md)
