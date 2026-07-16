---
description: Configure BetterDailyQuest BREED, FISH_ENTITY, KILL, MILK, SHEAR, and TAME tasks.
---

# Entity task reference

## `BREED`

Counts an entity-breeding event credited to the assigned player. This type is registered on Minecraft 1.10 and newer.

```yaml
task_type: "BREED"
required_amount: 2
required_target: COW
```

- **Target:** New entity type.
- **Progress:** One per breeding event.
- **Common mistake:** The assigned player must be the breeder reported by the event.

## `FISH_ENTITY`

Counts an entity caught with a fishing rod.

```yaml
task_type: "FISH_ENTITY"
required_amount: 1
required_target: ZOMBIE
```

- **Target:** Caught entity type.
- **Progress:** One per catch event.
- **Common mistake:** Caught item entities also enter the entity event. Use `FISH_ITEM` for material targets.

## `KILL`

Counts a living entity killed by the assigned player.

```yaml
task_type: "KILL"
required_amount: 5
required_targets:
  - ZOMBIE
  - SKELETON
```

- **Target:** Killed entity type.
- **Progress:** One per entity.
- **Common mistake:** The player must be reported as the killer.

## `MILK`

Counts a successful player interaction that milks a cow.

```yaml
task_type: "MILK"
required_amount: 3
```

- **Target:** None.
- **Progress:** One per allowed interaction.
- **Common mistake:** A cancelled interaction does not count.

## `SHEAR`

Counts a successful entity-shear event.

```yaml
task_type: "SHEAR"
required_amount: 3
conditions:
  sheep_color: WHITE
```

- **Target:** None.
- **Progress:** One per allowed shear event.
- **Common mistake:** Use `sheep_color` to restrict sheep color; do not add a target.

## `TAME`

Counts an entity tamed by the assigned player.

```yaml
task_type: "TAME"
required_amount: 2
required_target: WOLF
```

- **Target:** Tamed entity type.
- **Progress:** One per tame event.
- **Common mistake:** The new owner must be a player.

## Entity conditions

Use `sheep_color` only for sheep events. Use `required_villager` only where the event can contain a villager. An unrelated entity condition can allow the event instead of blocking it.
