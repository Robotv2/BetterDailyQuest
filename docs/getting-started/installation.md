# Install and verify startup

**Outcome:** BetterDailyQuest enables without a plugin-loading error.<br>
**Estimated time:** 5 minutes.<br>
**You need:** Server file access, console access, and a release JAR.

## Install

1. Stop the server cleanly.
2. Download the JAR attached to the [latest GitHub Release](https://github.com/Robotv2/BetterDailyQuest/releases/latest).
3. Place the JAR directly inside the server's `plugins` directory.
4. Start the server.
5. Wait until the console reports that the server is ready.

## Verify

Run this command from the server console:

```text
bdq
```

The response should identify BetterDailyQuest and its version. The first startup also creates:

```text
plugins/BetterDailyQuest/
├── config.yml
├── groups/
│   └── daily.yml
├── quests/
│   └── daily-quests.yml
└── addons/                 # Created when addons are installed
```

<div class="bdq-checkpoint" markdown>

**Checkpoint:** The console contains `Enabling BetterDailyQuest`, reaches the server-ready message, and does not contain `Error occurred while enabling BetterDailyQuest`.

</div>

!!! danger "Do not continue past an enable failure"
    Quest files cannot be tested while the plugin is disabled. Follow [Startup failures](../troubleshooting/startup.md) first.

## Next step

[Create the Stonebreaker quest](first-quest/index.md).
