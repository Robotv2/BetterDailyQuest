---
description: Install BetterDailyQuest, create reliable quest content, and operate it safely on a Minecraft server.
---

# BetterDailyQuest Administrator Guide

<div class="bdq-hero" markdown>

BetterDailyQuest defines quest groups, assigns quests to players, tracks task progress, and runs rewards. This guide is for server owners and administrators. It explains outcomes first and configuration only when you need it.

[Create your first quest](getting-started/first-quest/index.md){ .md-button .md-button--primary }
[Download 0.0.1 Beta](https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.1){ .md-button }

</div>

!!! warning "Release-matched documentation"
    The public site is deployed from the same tag as the latest GitHub Release. Repository previews may describe the next release before its JAR is available.

**Current release:** [BetterDailyQuest 0.0.1 Beta](https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.1)

<div class="grid cards" markdown>

-   :material-rocket-launch: **Create your first quest**

    ---

    Install the plugin, create the Stonebreaker quest, assign it, and prove that progress and rewards work.

    [Start the 15-minute path](getting-started/first-quest/index.md)

-   :material-book-open-page-variant: **Design quest content**

    ---

    Combine task types, targets, conditions, rewards, and presentation without losing track of inheritance.

    [Design quests](creating-quests/index.md)

-   :material-server: **Operate the plugin**

    ---

    Configure storage, permissions, schedules, backups, rerolls, reloads, and support diagnostics.

    [Operate BetterDailyQuest](administration/index.md)

-   :material-lifebuoy: **Solve a problem**

    ---

    Follow symptom-based checks for startup, content loading, assignment, progress, reward, and database failures.

    [Troubleshoot](troubleshooting/index.md)

</div>

## What the core plugin includes

- YAML-defined quest groups, quests, and tasks.
- Player-specific assignments, completion history, rerolls, and scheduled quest group refreshes.
- SQLite and MariaDB storage.
- Action-bar, title, message, command, sound, and inventory-close actions.
- Optional Vault role limits and one PlaceholderAPI group-refresh placeholder.
- An addon loader for separately supplied BetterDailyQuest addons.

## Important presentation boundary

BetterDailyQuest does **not** include a player quest menu. The core plugin tracks quests and provides progress cosmetics and rewards. A menu, scoreboard, or richer quest browser must come from another plugin or a compatible addon. See [Present quests to players](integrations/presentation.md).

## Current verification status

BetterDailyQuest 0.0.1 Beta has passed startup smoke checks on Paper 1.8.8 and Paper 26.1.2. These checks prove that the server reaches its ready state with BetterDailyQuest enabled and the canonical Stonebreaker configuration loaded; they do not yet prove every quest workflow on those versions. See the [compatibility reference](reference/compatibility.md).
