# Quest and task schema

## Quest keys

| Path | Type | Default/requirement | Effect |
| --- | --- | --- | --- |
| Quest ID | YAML key | Required, globally unique without case | Command and storage identity |
| `name` | Text | Recommended | Display name for internal placeholders and cosmetics |
| `group` | Text | Required existing group ID | Attaches quest to a group |
| `description` | Text list | Optional metadata | Not displayed by core BetterDailyQuest |
| `options` | Section | Inherit group | Quest overrides for repeatable, sequential, and start behavior |
| `conditions` | Section | None | Quest-level progress conditions |
| `cosmetics` | Section | Inherit group/global | Quest presentation overrides |
| `rewards` | Text list | Empty | Actions after every task completes |
| `tasks` | Numeric-keyed section | Strongly required | Configured requirements |

Use at least one task. An empty task collection has no useful administrator workflow and can appear complete by definition.

## Task keys

| Path | Type | Default/requirement | Effect |
| --- | --- | --- | --- |
| Task ID | Numeric YAML key | Required, unique in quest | Ordering and storage identity |
| `task_type` | Text | Required | Selects the progress event and target type |
| `task_description` | Text | Optional metadata | Not displayed by core BetterDailyQuest |
| `required_amount` | Number or `min-max` text | `1` for numerical types | Completion requirement chosen for assignment |
| `required_target` | Text | Required for one-target matching types | One accepted target |
| `required_targets` | Text list | Required for list-target matching types | Allowed, wildcard, tag, and excluded targets |
| `required_location` | Section | Required for `LOCATION` | World, coordinates, and radius |
| `conditions` | Section | None | Task-level progress conditions |
| `cosmetics` | Section | Inherit quest/group/global | Task progress/completion presentation |
| `task_rewards` | Text list | Empty | Actions when this task completes |

If a target-based task has neither `required_target` nor `required_targets`, it matches no target. Void-target task types are the exception.

## Complete example

```yaml
--8<-- "docs/examples/stonebreaker/extended/quests/stonebreaker.yml"
```

Related: [Task types](task-types.md) · [Conditions](conditions.md) · [Actions](actions.md)
