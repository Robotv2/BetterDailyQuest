---
description: Reference every BetterDailyQuest progress condition for player context, permissions, items, entities, and placeholders.
---

# Progress condition reference

Conditions can appear under a quest or task `conditions` section.

## `permissions`

```yaml
conditions:
  permissions:
    required:
      - server.quests.stonebreaker
      - server.world.resource
    mode: ALL
    error-message: "&cYou cannot progress this quest."
```

| Key | Default | Meaning |
| --- | --- | --- |
| `required` | Required | Non-empty list of Bukkit permission nodes |
| `mode` | `ALL` | `ALL` requires every node; `ANY` requires at least one |
| `error-message` | No message | Text sent when the condition blocks progress |

Mode names are case-insensitive. Invalid modes and empty permission lists prevent the quest from loading.

## `game_modes`

```yaml
conditions:
  game_modes:
    required:
      - SURVIVAL
      - ADVENTURE
    error-message: "&cUse Survival or Adventure mode."
```

| Key | Default | Meaning |
| --- | --- | --- |
| `required` | Required | Non-empty list of Bukkit game modes |
| `error-message` | No message | Text sent when the condition blocks progress |

Names ignore case. Blank and unknown game modes prevent the quest from loading. The condition checks the player's current game mode when the progress event occurs.

## `biomes`

```yaml
conditions:
  biomes:
    required:
      - PLAINS
      - FOREST
    error-message: "&cGo to a plains or forest biome."
```

| Key | Default | Meaning |
| --- | --- | --- |
| `required` | Required | Non-empty list of biome names supported by the running server |
| `error-message` | No message | Text sent when the condition blocks progress |

Names ignore case. BDQ checks the biome at the player's current block. Blank, unknown, and unsupported biome names prevent the quest from loading. Available names depend on the Minecraft server version.

## `height`

```yaml
conditions:
  height:
    minimum: -64
    maximum: 32
    error-message: "&cMine at Y 32 or below."
```

| Key | Default | Meaning |
| --- | --- | --- |
| `minimum` | No minimum | Inclusive minimum block Y coordinate |
| `maximum` | No maximum | Inclusive maximum block Y coordinate |
| `error-message` | No message | Text sent when the condition blocks progress |

Set at least one boundary. Both values must be integers. The minimum cannot be greater than the maximum.

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
    minimum-balance:
      placeholder: "%vault_eco_balance_fixed%"
      match: "100"
      comparator: MORE_EQUAL
```

Each named entry must match. External placeholders need PlaceholderAPI.

| Key | Default | Meaning |
| --- | --- | --- |
| `placeholder` | Required | Text or PlaceholderAPI value to resolve |
| `match` | Required | Expected text or numeric comparison value |
| `comparator` | `EQUAL` | `MORE`, `MORE_EQUAL`, `EQUAL`, `LESS_EQUAL`, or `LESS` |

Text matching is exact and case-sensitive. Numeric equality ignores representation differences such as `10` and `10.0`. Ordered comparators require numeric configured and resolved values.

Missing values, unknown comparators, and ordered comparators with non-numeric match values prevent the quest from loading.

## Error messages

Section-based conditions can use `error-message`. BDQ replaces available internal placeholders before sending it. List or single-value conditions such as `worlds` and `sheep_color` do not provide the same inline message shape.
