---
description: Install BetterDailyQuest, create quests, and manage player progress on a Minecraft server.
---

# BetterDailyQuest administrator guide

BetterDailyQuest lets server owners create repeatable quest content with YAML files. It assigns quests to players, tracks their progress, saves their data, and runs rewards when they finish.

[Create your first quest](getting-started/first-quest.md){ .md-button .md-button--primary }
[Download 0.0.3 Beta](https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.3){ .md-button }

!!! warning "Use the guide for your release"
    The public site is built from a release tag. Pages in the repository can describe work that is not released yet.

## Start here

<div class="grid cards" markdown>

-   :material-download: **Install BetterDailyQuest**

    Check the requirements, install the plugin, and confirm that it starts.

    [Open the installation guide](getting-started/installation.md)

-   :material-pickaxe: **Create a quest**

    Build Stonebreaker, give it to a player, and test its reward.

    [Create your first quest](getting-started/first-quest.md)

-   :material-cog: **Manage a server**

    Configure commands, assignments, storage, updates, and integrations.

    [Manage BetterDailyQuest](administration/index.md)

-   :material-book-open-page-variant: **Look up a value**

    Find every supported key, task type, condition, action, and command.

    [Open the reference](reference/index.md)

</div>

## What the core plugin includes

- Quest groups with shared rules and reset schedules.
- Quests with one or more tasks.
- Automatic or manual quest assignment.
- SQLite and MariaDB storage.
- Task and quest rewards.
- Action bars and titles for progress and completion.
- Optional Vault, PlaceholderAPI, and addon support.

## What the core plugin does not include

BetterDailyQuest does not include a player quest menu or a command that lists every active quest. Use progress messages or a compatible presentation addon if players need a menu, scoreboard, or browser.

## Current compatibility evidence

Version 0.0.3 Beta has passed startup checks on Paper 1.8.8 and Paper 26.1.2. A startup check proves that the plugin enables and loads its example content. It does not prove every task type or integration.

[Read the compatibility reference](reference/compatibility.md)
