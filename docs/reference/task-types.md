# Task types and targets

Target names use Minecraft/XSeries identifiers appropriate to the running server. Counts describe what one matching event contributes.

| Type | Player activity | Target | Count behavior | Version note |
| --- | --- | --- | --- | --- |
| `BREAK` | Break a block | Material | 1 per block | Loaded on all verified baselines |
| `PLACE` | Place a block | Material | 1 per block | Loaded on all verified baselines |
| `CARVE` | Use shears on a pumpkin | None | 1 per carve | Loaded on all verified baselines |
| `BREED` | Be the breeder for a new entity | Entity type | 1 per breed event | Registered on 1.10+ |
| `FISH_ENTITY` | Catch an entity with a fishing rod | Entity type | 1 per caught entity | Includes caught item entities |
| `KILL` | Kill a living entity | Entity type | 1 per entity | Player must be reported as killer |
| `MILK` | Use a bucket on a cow | None | 1 per interaction | Cancelled interactions do not count |
| `SHEAR` | Shear an entity | None | 1 per shear event | Cancelled events do not count |
| `TAME` | Become owner when an entity is tamed | Entity type | 1 per tame event | Non-player owners do not count |
| `CONSUME` | Consume an item | Material | 1 per consume event | Cancelled events do not count |
| `COOK` | Extract a furnace result | Material | Extracted item amount | Uses furnace extraction |
| `ENCHANT` | Enchant an item | Material | Enchanted stack amount | Conditions can inspect applied enchants |
| `FISH_ITEM` | Catch an item with a fishing rod | Material | 1 per caught item event | Separate from `FISH_ENTITY` |
| `LAUNCH` | Launch a projectile | Entity type | 1 per projectile | Shooter must be a player |
| `PICKUP` | Pick up an item entity | Material | Picked-up stack amount | Registered on 1.9+ |
| `CRAFT` | Take a crafted result | Material | 1 per craft event | Does not count output stack size |
| `DEATH` | Player dies | Damage cause | 1 per death | Target is Bukkit damage cause, for example `FALL` |
| `LOCATION` | Move into a configured radius | Location section | Completes on matching movement | No `required_amount` |

## Location shape

```yaml
task_type: LOCATION
required_location:
  world: world
  x: 100
  y: 64
  z: -25
  distance_from_location: 5
```

The world must already be loaded when quests load. The boundary comparison is strictly inside the configured radius.

## Target expressions

Targeted types accept one value, a list, `*`, `!VALUE` exclusions combined with `*`, and supported `TAG:<name>` expressions. See [Tasks and targets](../creating-quests/tasks-targets.md).
