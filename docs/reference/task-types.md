---
description: Choose the correct BetterDailyQuest task type and open its detailed reference.
---

# Task type overview

BDQ provides 23 task types. Choose the family that matches the player action.

| Family | Types | Reference |
| --- | --- | --- |
| Block | `BREAK`, `PLACE`, `CARVE` | [Block task reference](task-types-block.md) |
| Entity | `BREED`, `FISH_ENTITY`, `KILL`, `MILK`, `SHEAR`, `TAME` | [Entity task reference](task-types-entity.md) |
| Item | `CONSUME`, `COOK`, `ENCHANT`, `FISH_ITEM`, `LAUNCH`, `PICKUP`, `CRAFT` | [Item task reference](task-types-item.md) |
| Player | `DEATH`, `LOCATION`, `WALK`, `SWIM`, `JUMP`, `BOAT`, `MINECART` | [Player task reference](task-types-player.md) |

## Target forms

- Material targets use Minecraft/XSeries material names.
- Entity targets use Minecraft/XSeries entity names.
- `DEATH` uses a Bukkit damage cause.
- `LOCATION` uses a world, coordinates, and radius.
- `CARVE`, `MILK`, `SHEAR`, `WALK`, `SWIM`, `JUMP`, `BOAT`, and `MINECART` accept no target; adding `required_target` or `required_targets` makes the task invalid.

Most counted task types add one per event. `COOK`, `ENCHANT`, and `PICKUP` can use an item amount from the event. `WALK`, `SWIM`, `BOAT`, and `MINECART` add distance in blocks. The detailed pages explain each rule.

Target names must exist on the running server version. `PICKUP` is registered on Minecraft 1.9 and newer. `BREED` is registered on Minecraft 1.10 and newer.
