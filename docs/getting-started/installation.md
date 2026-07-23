---
description: Install BetterDailyQuest and confirm that the plugin starts without errors.
---

# Install BetterDailyQuest

**Result:** BetterDailyQuest starts and creates its default files.<br>
**Time:** About 5 minutes.<br>
**You need:** Server files, console access, and the release JAR.

## Install the plugin

1. Stop the server.
2. Download the JAR attached to [BetterDailyQuest 0.0.7 Beta](https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.7).
3. Put the JAR directly in the server `plugins` folder.
4. Make sure there is only one BetterDailyQuest JAR in that folder.
5. Allow the server to access the internet for the first startup.
6. Start the server.
7. Wait until the server reports that startup is complete.

## Check the installation

Run this command in the console:

```text
bdq
```

The command should show the running BetterDailyQuest version.

The first startup creates files similar to these:

```text
plugins/BetterDailyQuest/
├── config.yml
├── data.database
├── groups/
│   └── daily.yml
└── quests/
    └── daily-quests.yml
```

`data.database` is created when SQLite starts. An `addons` folder can appear after you install an addon.

!!! success "Installation checkpoint"
    The console shows that BetterDailyQuest was enabled, the server reaches its ready state, and no enable error appears.

## If the plugin does not start

Check the first error that mentions BetterDailyQuest. Common causes are:

- the server is using the wrong Java version;
- the JAR is damaged or not from a release;
- two BetterDailyQuest JARs are installed;
- the server cannot reach JitPack or Maven Central on first startup;
- `config.yml` contains an invalid database setup;
- the server cannot write to the plugin folder.

Run `java -version` in the same environment that starts the server. If needed, test the same JAR with fresh default files on a private server. Do not delete the production database while testing.

## Next step

[Create your first quest](first-quest.md).
