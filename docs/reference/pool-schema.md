# Quest group schema

A **quest group** is the runtime concept identified by the `group` key and command argument.

## Identity

For one group per file, the lowercase file name without `.yml` is the group ID. A `groups` wrapper can define multiple explicit IDs. Group lookup ignores case.

## Keys

| Path | Type | Default | Scope and effect |
| --- | --- | --- | --- |
| `options.repeatable` | Boolean | `false` | Default repeatability for quests in this group |
| `options.sequential-tasks` | Boolean | `false` | Default task ordering rule |
| `options.need-starting` | Boolean | `false` | Default assignment start state; keep false in core setups |
| `options.automatically-given` | Boolean | `true` | Fill loaded players after refresh/load |
| `automatic-reset` | Cron text | No schedule | Quest group refresh schedule in server JVM time zone |
| `global-assignment-limit` | Integer | `0` | Fallback number of assignments for automatic fill |
| `assignment-limits.<role>` | Integer | No role overrides | Vault primary-role-specific assignment count |
| `max-rerolls` | Integer | `0` | Positive cap; zero means no numeric cap |
| `cosmetics` | Section | Inherit global | Quest group presentation fallback |

## Complete example

```yaml
--8<-- "docs/examples/stonebreaker/extended/groups/daily.yml"
```

## Pitfalls

- `assignment-limits` replaces the older implementation term `assignations`; legacy keys are not parsed.
- `global-assignment-limit` replaces `global-assignation`; legacy keys are not parsed.
- `sequential-tasks` replaces `dependant-tasks`; legacy keys are not parsed.
- A limit larger than the eligible quest count cannot create nonexistent assignments.
- `max-rerolls: 0` does not create an alternative quest; it only removes the numeric cap.

Related: [Options and inheritance](../concepts/options-inheritance.md) · [Schedules](../administration/schedules.md)
