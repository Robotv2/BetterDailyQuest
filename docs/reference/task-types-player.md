---
description: Configure BetterDailyQuest DEATH, LOCATION, WALK, and SWIM tasks.
---

# Player task reference

## `DEATH`

Counts the assigned player's death and matches the last Bukkit damage cause.

```yaml
task_type: "DEATH"
required_amount: 1
required_target: FALL
```

- **Target:** Bukkit damage cause, such as `FALL`, `FIRE`, or `ENTITY_ATTACK`.
- **Progress:** One per death.
- **Common mistake:** The target is a damage cause, not the entity that caused the death.
- **Check:** Test the selected cause and a different cause.

## `LOCATION`

Completes when the assigned player moves inside a configured radius.

```yaml
task_type: "LOCATION"
required_location:
  world: world
  x: 100
  y: 64
  z: -25
  distance_from_location: 5
```

- **Target:** Loaded world, coordinates, and radius.
- **Progress:** Completes when a full-block movement enters the area.
- **Default radius:** `5` blocks when `distance_from_location` is missing.
- **Common mistake:** The world must already be loaded when the quest loads.

The distance check is strictly inside the radius. A player exactly on the boundary does not match.

`LOCATION` does not use `required_amount`.

## `WALK`

Counts distance traveled on foot while walking, sprinting, or sneaking.

```yaml
task_type: "WALK"
required_amount: 500
```

- **Target:** None.
- **Progress:** Distance in blocks; partial blocks can produce decimal progress.
- **Included:** Walking, sprinting, and sneaking.
- **Excluded:** Swimming, climbing, falling, flying, gliding, and vehicle travel.
- **Common mistake:** Adding a material or entity target is not needed.

Only distance gained after the player is being tracked counts. Existing lifetime statistics are used as a baseline, not granted as quest progress.

## `SWIM`

Counts swimming distance.

```yaml
task_type: "SWIM"
required_amount: 1000
```

- **Target:** None.
- **Progress:** Swimming distance in blocks; partial blocks can produce decimal progress.
- **Common mistake:** Walking through shallow water does not necessarily increase Minecraft's swimming statistic.

Only distance gained after the player is being tracked counts. Walking, sprinting, flying, and vehicle travel do not count.
