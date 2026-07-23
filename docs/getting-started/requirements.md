---
description: Check the server, Java, release, and optional plugin requirements for BetterDailyQuest.
---

# Check the requirements

Check these points before you install BetterDailyQuest.

## Required

- A Bukkit-compatible Minecraft server.
- A Java runtime supported by that server.
- Access to the server console and `plugins` folder.
- The JAR from [BetterDailyQuest 0.0.7 Beta](https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.7).

BetterDailyQuest targets Java 17 bytecode. A recent server can require a newer Java version. Use the Java version required by your server software.

BetterDailyQuest downloads its required runtime libraries on first startup. Allow the server to reach JitPack and Maven Central, and keep the generated `.libs` folder. Do not install Anchor as a separate server plugin.

BetterDailyQuest uses [bStats](https://bstats.org/docs/server-owners) for anonymous usage metrics, including the configured database type. It does not send database connection details. Server owners can disable metrics globally in `plugins/bStats/config.yml`.

## Optional plugins

| Plugin or component | Use | Behavior when missing |
| --- | --- | --- |
| PlaceholderAPI | Show a quest-group reset timer or use placeholder conditions | Core quests continue to work |
| Vault and a permissions provider | Use role-based assignment limits | BDQ uses the `default` role and then the global limit |
| BetterDailyQuest addon | Add presentation or addon-specific features | Only core features are available |

## Verified servers

The current release has passed startup tests on Paper 1.8.8 and Paper 26.1.2. Other versions are not automatically incompatible, but they are not part of the current public test evidence.

See [Compatibility reference](../reference/compatibility.md) for exact Java and test details.

## Next step

[Install BetterDailyQuest](installation.md).
