---
description: Review release-specific BetterDailyQuest server, Java, startup, and gameplay test evidence.
---

# Compatibility reference

Compatibility claims are based on repeatable tests. A startup test and a gameplay test do not prove the same thing.

## Version 0.0.9 pre-release evidence

| Server | Java used | Date | Startup | Player gameplay |
| --- | --- | --- | --- | --- |
| Paper 1.8.8 build 445 | Java 17 | 2026-07-29 | Plugin enabled, all thirty-two fresh-install quests loaded, clean shutdown | Not run |
| Paper 26.2 build 87 | Java 25 | 2026-07-29 | Plugin enabled, all thirty-two fresh-install quests loaded, clean shutdown | Not run |
| User gameplay check | Not recorded | 2026-07-29 | Not used for startup evidence | Walking, sprinting, sneaking, swimming, completion, and persistence confirmed |

Version 0.0.9 adds `WALK` and `SWIM` progression. Automated tests cover statistic baselines, positive distance changes, statistic resets, player cleanup, and conversion from centimeters to blocks. Cross-version player gameplay was not recorded, so the two server rows only claim startup and shutdown compatibility.

## Version 0.0.8 Beta evidence

| Server | Java used | Date | Startup | Player gameplay |
| --- | --- | --- | --- | --- |
| Paper 1.8.8 build 445 | Java 17 | 2026-07-25 | Plugin enabled, all thirty fresh-install quests loaded, clean shutdown | Not rerun |
| Paper 26.1.2 build 74 | Java 25 | 2026-07-25 | Plugin enabled, all thirty fresh-install quests loaded, clean shutdown | Not rerun |

The plugin bytecode targets Java 17. A server can require a newer Java runtime.

Version 0.0.6 verified Quest Board opening and protection, Stonebreaker start and completion, reload, reconnect, and persistence on both listed servers. Version 0.0.7 only added bStats metrics. Version 0.0.8 changes fresh-install configuration and does not change quest progression code.

## What a startup test proves

- The server accepted the release JAR.
- BetterDailyQuest enabled.
- Default storage and content loaded far enough for the server to become ready.

It does not prove every task event, target name, optional integration, MariaDB environment, addon, or data change.

## Version-specific task types

- `PICKUP` is registered on Minecraft 1.9 and newer.
- `BREED` is registered on Minecraft 1.10 and newer.
- Materials and entity names must exist on the running server version.

## Before production use

1. Run startup checks with the exact release JAR.
2. Complete the Stonebreaker tutorial with a player.
3. Restart and check saved data.
4. Test every optional integration you use.
5. Test your own quests on every server version you support.

A server not listed here is unverified, not automatically incompatible.
