# Reloads and restarts

## `bdq reload`

Use the plugin command after changing:

- `config.yml` messages, cosmetics, or time format;
- quest group YAML;
- quest YAML;
- addon configuration when that addon supports the reload hook.

The command reloads global configuration, stops existing group schedules, reloads groups and quests, and calls addon reload hooks.

## Full server restart

Restart after:

- replacing the BetterDailyQuest JAR;
- adding, removing, or replacing an addon JAR;
- changing server Java or server software;
- investigating class-loading or library problems;
- changing storage infrastructure where a clean connection boundary matters.

## Content-removal warning

Reloading after deleting or renaming a quest does not automatically translate stored assignments. Those assignments may become unavailable. Clear or refresh affected assignments as part of the content migration plan.

Avoid server-wide `/reload`; plugin ecosystems generally cannot guarantee safe class and task cleanup under it.

!!! warning "Check the shutdown log on the latest Paper release"
    A verified Paper 26.1.2 smoke run emitted a cron4j `zip file closed` warning after BetterDailyQuest disabled. Do not treat it as a startup failure, but preserve the complete shutdown log and review the [known limitation](../reference/known-limitations.md#latest-paper-may-log-a-scheduler-warning-during-shutdown).
