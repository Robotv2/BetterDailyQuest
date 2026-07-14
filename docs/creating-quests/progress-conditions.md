# Progress conditions

Progress conditions decide whether an otherwise matching player activity may advance a quest or task.

## Quest-level versus task-level

- Quest-level conditions apply before any task in the assignment can progress.
- Task-level conditions apply only to that task.

## Restrict Stonebreaker to one world

```yaml
conditions:
  worlds:
    - world
```

World names are exact and case-sensitive. Use the folder/world name shown by the server.

## Available condition families

| Key | Use |
| --- | --- |
| `worlds` | Allow progress only in listed worlds |
| `required_enchants` | Require an enchantment and optional minimum level on a relevant item/event |
| `sheep_color` | Require a sheep color for sheep-related entity activity |
| `required_villager` | Restrict villager profession, biome type, or level |
| `placeholders` | Compare PlaceholderAPI output; advanced numerical comparators are not yet supported by this guide |

Conditions that are irrelevant to an activity may allow it rather than rejecting it. Place specialized conditions only on matching task types.

!!! warning "Numerical PlaceholderAPI comparisons"
    `MORE`, `MORE_EQUAL`, `LESS`, and `LESS_EQUAL` behavior is not documented as supported until its comparison and exact-string interaction is corrected and verified.

See exact shapes and limitations in the [condition reference](../reference/conditions.md).
