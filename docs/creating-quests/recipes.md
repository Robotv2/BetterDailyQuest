---
description: Copy complete BetterDailyQuest recipes for block, entity, item, and location quests.
---

# Quest recipe book

These recipes are independent. Copy one group file and one quest file, then change the IDs and values for your server.

## Stonebreaker

**Shows:** `BREAK`, several material targets, task messages, and quest messages.

Group file:

```yaml
--8<-- "docs/examples/stonebreaker/groups/daily.yml"
```

Quest file:

```yaml
--8<-- "docs/examples/stonebreaker/quests/stonebreaker.yml"
```

Give it with `bdq give daily stonebreaker PlayerName`.

## Ranch hand

**Shows:** `BREED`, `SHEAR`, sequential tasks, and a sheep-color condition.

Group file:

```yaml
--8<-- "docs/examples/ranch-hand/groups/ranch.yml"
```

Quest file:

```yaml
--8<-- "docs/examples/ranch-hand/quests/ranch-hand.yml"
```

The player must breed two cows, then shear three white sheep. `BREED` is available on Minecraft 1.10 and newer.

## Workshop order

**Shows:** `CRAFT`, `ENCHANT`, an enchantment condition, and a console reward.

Group file:

```yaml
--8<-- "docs/examples/workshop-order/groups/workshop.yml"
```

Quest file:

```yaml
--8<-- "docs/examples/workshop-order/quests/workshop-order.yml"
```

Review the console reward before using it on a public server.

## Wayfinder

**Shows:** `LOCATION`, a world condition, and a scheduled group reset.

Group file:

```yaml
--8<-- "docs/examples/wayfinder/groups/exploration.yml"
```

Quest file:

```yaml
--8<-- "docs/examples/wayfinder/quests/wayfinder.yml"
```

Change the world and coordinates before loading the quest. The world must already be loaded when BDQ reads the file.

## Test a recipe

1. Put the group and quest files in their matching folders.
2. Run `bdq reload`.
3. Confirm that both IDs load.
4. Give the quest to an online test player.
5. Test one action that should count and one that should not.
6. Complete the quest and check every reward.

For all accepted task values, open [Task type overview](../reference/task-types.md).
