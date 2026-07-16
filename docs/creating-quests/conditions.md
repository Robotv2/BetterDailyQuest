---
description: Restrict BetterDailyQuest progress by world, enchantment, entity data, or placeholder value.
---

# Add progress conditions

Conditions stop an otherwise matching action from adding progress.

## Choose the level

- A quest condition applies to every task in the quest.
- A task condition applies only to that task.

Place a condition as close as possible to the task that needs it.

## World condition

```yaml
conditions:
  worlds:
    - world
    - resource_world
```

World names must match the server names exactly.

## Enchantment condition

```yaml
conditions:
  required_enchants:
    required_level: 3
    required_types:
      - FORTUNE
    error-message: "&cUse an item with Fortune III."
```

This condition checks enchantments available in the task event. Use it with item-related tasks.

## Sheep condition

```yaml
conditions:
  sheep_color: WHITE
```

Use a Bukkit dye-color name. The condition only restricts an event when its entity is a sheep.

## Villager condition

```yaml
conditions:
  required_villager:
    required_professions:
      - FARMER
    required_types:
      - PLAINS
    required_level: 2
    error-message: "&cUse a level 2 plains farmer."
```

Empty profession or type lists mean that field has no restriction.

## Placeholder condition

```yaml
conditions:
  placeholders:
    ready-state:
      placeholder: "%some_plugin_state%"
      match: "ready"
```

PlaceholderAPI must be installed for external placeholders to resolve.

!!! warning "Use exact text matching"
    Only exact string matching is safe public guidance. Numerical comparator values exist in the code, but the current path also performs an exact text check.

## Test both results

For every condition, perform one action that should pass and one that should fail. A condition that does not apply to an event can allow the event, so do not place entity-specific conditions on unrelated tasks.

See [Progress condition reference](../reference/conditions.md) for every accepted shape.

## Next step

[Add rewards and progress messages](rewards-display.md).
