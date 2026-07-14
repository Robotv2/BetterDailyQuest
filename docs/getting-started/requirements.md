# Requirements and compatibility

## Outcome

Know whether your server and optional integrations are ready before installing BetterDailyQuest.

## Required

- A Bukkit-compatible server distribution capable of loading the release JAR.
- The Java version required by that server distribution.
- File access to the server's `plugins` directory.
- The tagged JAR from [BetterDailyQuest 0.0.1 Beta](https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.1).

BetterDailyQuest shades its required runtime libraries into the release JAR. Do not install Anchor separately unless a release note explicitly instructs you to.

## Optional integrations

| Integration | Needed for | Core fallback |
| --- | --- | --- |
| PlaceholderAPI | Quest-group-refresh placeholder and placeholder-based progress conditions | Placeholder strings remain unchanged; other core features continue |
| Vault plus a permissions provider | Role-specific assignment limits | The `default` role is used |
| BetterDailyQuest addon | Addon-specific commands, conditions, presentation, or content behavior | Only core behavior is available |

## Compatibility claims

Only combinations exercised by repeatable checks are listed as verified. A startup check means the plugin enabled and the server reached ready state; it is not the same as full gameplay verification.

[View the compatibility matrix](../reference/compatibility.md)

## Next step

[Install and verify startup](installation.md).
