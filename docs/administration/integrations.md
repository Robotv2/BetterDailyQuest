---
description: Connect BetterDailyQuest to Vault, PlaceholderAPI, presentation plugins, and addons.
---

# Use integrations

BDQ works without optional integrations. Add one only when it solves a clear need.

| Need | Integration |
| --- | --- |
| Role-based assignment limits | Vault and a permissions provider |
| Reset time in another plugin | PlaceholderAPI |
| Menu, scoreboard, or quest browser | A compatible presentation plugin or addon |
| Extra BDQ behavior | A compatible addon |

## Vault role limits

Vault lets BDQ ask a permissions provider for the player's primary role.

```yaml
global-assignment-limit: 1
assignment-limits:
  default: 1
  vip: 3
```

Role names ignore case. If Vault or the provider cannot return a role, BDQ uses `default`. If that key is missing, it uses the global limit.

Test with at least one normal player and one player in a special role.

## PlaceholderAPI reset time

BDQ registers this placeholder when PlaceholderAPI is ready:

```text
%betterdailyquest_group_reset_<group>%
```

Example:

```text
%betterdailyquest_group_reset_daily%
```

It returns `Invalid group` for an unknown group and `No reset scheduled.` when the group has no schedule.

No separate eCloud expansion is needed. Internal values such as `%quest_name%` are not public PlaceholderAPI values.

## Presentation plugins

Core BDQ has no quest menu and does not display the `description` or `task_description` fields. Before using a presentation layer, check:

- exact BDQ version support;
- how players find active assignments;
- which descriptions and progress values are displayed;
- added commands and permissions;
- behavior when the presentation plugin is disabled.

## Addons

Put trusted addon JARs in `plugins/BetterDailyQuest/addons/`.

1. Confirm the addon supports your BDQ and server versions.
2. Stop the server.
3. Add the JAR.
4. Start the server and check its load message.
5. Follow the addon guide for its own settings.

Restart after adding, removing, or replacing an addon JAR. `bdq reload` cannot load new Java classes.

Addons can listen for `QuestStartEvent` after a waiting assignment changes to its started state. The event exposes the configured quest, the player-specific assignment, and the player. It is not cancellable.

Addons run with server-plugin access. Install only trusted files and include them in backups.
