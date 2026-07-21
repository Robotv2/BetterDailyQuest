---
description: Review release-specific BetterDailyQuest server, Java, startup, and gameplay test evidence.
---

# Compatibility reference

Compatibility claims are based on repeatable tests. A startup test and a gameplay test do not prove the same thing.

## Version 0.0.5 Beta evidence

| Server | Java used | Date | Startup | Player gameplay |
| --- | --- | --- | --- | --- |
| Paper 1.8.8 build 445 | Java 17 | 2026-07-21 | Plugin enabled, example loaded, clean shutdown | Not release-verified |
| Paper 26.1.2 build 74 | Java 25 | 2026-07-21 | Plugin enabled, example loaded, clean shutdown | Stonebreaker completion, rewards, and persistence verified |

The plugin bytecode targets Java 17. A server can require a newer Java runtime.

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
