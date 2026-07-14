# Progress-condition reference

Conditions may appear under a quest or task `conditions` section.

## `worlds`

```yaml
conditions:
  worlds:
    - world
    - resource_world
```

The initiating player's exact current world name must be listed. Default: no world condition.

## `required_enchants`

```yaml
conditions:
  required_enchants:
    required_level: 3
    required_types:
      - FORTUNE
    error-message: "&cUse a Fortune III item."
```

- `required_level` defaults to no minimum.
- An empty `required_types` list accepts any enchantment meeting the level.
- Use on item-related tasks where the event exposes enchantments.

## `sheep_color`

```yaml
conditions:
  sheep_color: WHITE
```

Applies when the progress context contains a sheep. Use a Bukkit dye-color identifier.

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

Empty profession or type lists mean no restriction for that field. Level defaults to no minimum. Apply only to entity tasks that can involve villagers.

## `placeholders`

```yaml
conditions:
  placeholders:
    permission-state:
      placeholder: "%some_plugin_state%"
      match: "ready"
    error-message: "&cYou are not ready for this quest."
```

Every named entry must match. PlaceholderAPI must be installed for external placeholders to resolve.

Only exact string equality is documented as supported. Although comparator names exist (`MORE`, `MORE_EQUAL`, `EQUAL`, `LESS_EQUAL`, `LESS`), non-equality numerical behavior is a known readiness gap and must not be used in production guidance yet.

## Error messages

Section-shaped conditions can include `error-message`. BetterDailyQuest replaces applicable internal assignment/task placeholders and sends the message when that condition blocks progress. Scalar/list conditions such as `worlds` and `sheep_color` do not have a reliable inline callback shape.
