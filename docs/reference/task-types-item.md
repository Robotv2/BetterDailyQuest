---
description: Configure BetterDailyQuest CONSUME, COOK, ENCHANT, FISH_ITEM, LAUNCH, PICKUP, and CRAFT tasks.
---

# Item task reference

## `CONSUME`

Counts an item consumed by the assigned player.

```yaml
task_type: "CONSUME"
required_amount: 5
required_target: BREAD
```

- **Target:** Consumed item material.
- **Progress:** One per consume event.
- **Common mistake:** A cancelled consume event does not count.

## `COOK`

Counts items taken from a furnace result.

```yaml
task_type: "COOK"
required_amount: 16
required_target: COOKED_BEEF
```

- **Target:** Result material, not the ingredient.
- **Progress:** Number of items extracted.
- **Common mistake:** Items cooked but not taken from the furnace do not count.

## `ENCHANT`

Counts an item enchanted by the assigned player.

```yaml
task_type: "ENCHANT"
required_amount: 1
required_target: IRON_PICKAXE
conditions:
  required_enchants:
    required_level: 1
```

- **Target:** Enchanted item material.
- **Progress:** Enchanted stack amount.
- **Common mistake:** Use `required_enchants` to check enchantment types or levels.

## `FISH_ITEM`

Counts an item caught with a fishing rod.

```yaml
task_type: "FISH_ITEM"
required_amount: 3
required_target: COD
```

- **Target:** Caught item material.
- **Progress:** One per caught item event.
- **Common mistake:** Use `FISH_ENTITY` for non-item entities.

## `LAUNCH`

Counts a projectile launched by the assigned player.

```yaml
task_type: "LAUNCH"
required_amount: 10
required_target: ARROW
```

- **Target:** Projectile entity type.
- **Progress:** One per launch.
- **Common mistake:** This type uses entity names, not item material names.

## `PICKUP`

Counts items picked up by the assigned player. This type is registered on Minecraft 1.9 and newer.

```yaml
task_type: "PICKUP"
required_amount: 32
required_target: COBBLESTONE
```

- **Target:** Picked-up item material.
- **Progress:** Picked-up stack amount.
- **Common mistake:** A cancelled pickup event does not count.

## `CRAFT`

Counts a completed craft event.

```yaml
task_type: "CRAFT"
required_amount: 2
required_target: IRON_PICKAXE
```

- **Target:** Crafted result material.
- **Progress:** One per craft event.
- **Common mistake:** The output stack size is not used as the progress amount.

## Item conditions

`required_enchants` checks enchantments available in an item event. Test the condition with the exact task type because not every item event exposes the same item data.
