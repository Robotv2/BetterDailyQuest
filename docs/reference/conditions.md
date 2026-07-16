---
description: Reference every supported BetterDailyQuest progress condition and its YAML shape.
---

# Progress condition reference

Conditions can appear under a quest or task `conditions` section.

## `worlds`

```yaml
conditions:
  worlds:
    - world
    - resource_world
```

The player's current world name must appear in the list.

## `required_enchants`

```yaml
conditions:
  required_enchants:
    required_level: 3
    required_types:
      - FORTUNE
    error-message: "&cUse Fortune III."
```

| Key | Default | Meaning |
| --- | --- | --- |
| `required_level` | No minimum | Minimum enchantment level |
| `required_types` | Any type | Accepted enchantments |
| `error-message` | No message | Text sent when the condition blocks progress |

Use this condition only when the task event contains a useful item.

## `sheep_color`

```yaml
conditions:
  sheep_color: WHITE
```

Use a Bukkit dye-color name. It restricts the event only when the event entity is a sheep.

## `required_villager`

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

An empty profession or type list means any value for that field. A missing level means no minimum.

## `placeholders`

```yaml
conditions:
  placeholders:
    ready-state:
      placeholder: "%some_plugin_state%"
      match: "ready"
```

Each named entry must match. External placeholders need PlaceholderAPI.

Only exact string matching is recommended. Although `MORE`, `MORE_EQUAL`, `EQUAL`, `LESS_EQUAL`, and `LESS` comparator names exist, numerical comparisons also pass through an exact-text check in the current code.

## Error messages

Section-based conditions can use `error-message`. BDQ replaces available internal placeholders before sending it. List or single-value conditions such as `worlds` and `sheep_color` do not provide the same inline message shape.
