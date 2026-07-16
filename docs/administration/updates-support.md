---
description: Reload and update BetterDailyQuest safely, collect debug logs, and prepare support data.
---

# Reload, update, and collect support data

Use the right action for the kind of change you made.

## Use `bdq reload`

Use the plugin command after changing:

- `config.yml` messages, cosmetics, or time format;
- quest group YAML;
- quest YAML;
- addon settings when the addon supports reloads.

BDQ stops old group schedules, reloads its configuration and content, then calls addon reload hooks.

## Restart the server

Use a full server restart after:

- replacing the BDQ JAR;
- adding, removing, or replacing an addon JAR;
- changing Java or server software;
- changing storage infrastructure;
- investigating class-loading errors.

Do not use the server-wide `/reload` command for plugin updates.

## Update safely

1. Read the release notes.
2. Stop the server.
3. Back up the BDQ folder and database.
4. Remove the old BDQ JAR.
5. Add one new release JAR.
6. Start the server and check groups, quests, storage, and one assignment.
7. Keep the backup until a normal restart and group refresh succeed.

If the update fails, stop the server before restoring the old JAR, files, and database as one matching set.

## Debug one problem

Set `debug: true` in `config.yml`, run `bdq reload`, and reproduce the problem once. Save the full log around the first BDQ error. Turn debug mode off after the test.

Useful messages include quest load success, duplicate Quest IDs, database initialization failures, player-data load completion, and failed data saves.

## Prepare support data

Include:

- BDQ release and JAR name;
- server software, build, and Minecraft version;
- Java version used by the server process;
- storage type;
- relevant integration and addon versions;
- expected and actual results;
- exact steps to reproduce;
- the first full exception;
- minimal related YAML.

Remove database passwords, private hostnames, IP addresses, tokens, webhook URLs, and unrelated player data. Keep YAML indentation when replacing a private value.

[Open a BetterDailyQuest issue](https://github.com/Robotv2/BetterDailyQuest/issues/new)
