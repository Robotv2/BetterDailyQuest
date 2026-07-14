# PlaceholderAPI

## What core BetterDailyQuest exposes

When PlaceholderAPI is installed and enabled, BetterDailyQuest registers one expansion automatically:

```text
%betterdailyquest_group_reset_<group>%
```

For the `daily` group:

```text
%betterdailyquest_group_reset_daily%
```

It returns the formatted time until the next scheduled quest group refresh. It returns `Invalid group` for an unknown group and `No reset scheduled.` when the group has no `automatic-reset` schedule.

## Install

1. Stop the server.
2. Install a PlaceholderAPI release compatible with the server.
3. Start the server and confirm PlaceholderAPI enables before BetterDailyQuest finishes enabling.
4. Put the placeholder into a PlaceholderAPI-aware scoreboard, menu, hologram, or chat plugin.
5. Verify the rendered value for a real group.

No separate BetterDailyQuest eCloud expansion download is required.

## Scope warning

Placeholders such as `%quest_name%` and `%task_progress%` are internal BetterDailyQuest replacements for rewards and cosmetics. They are not general PlaceholderAPI expansion values. See [Placeholder scopes](../reference/placeholders.md).
