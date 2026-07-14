# Present quests to players

BetterDailyQuest core has no quest menu or player command that lists every assignment. It provides:

- progress action bars and titles;
- task and quest reward messages;
- administrator assignment commands;
- a PlaceholderAPI group-refresh countdown;
- content fields and an addon system that other software may consume.

## Choose a presentation layer

A presentation layer may be a compatible BetterDailyQuest addon or another plugin configured with available placeholders and commands. Before committing to it, verify:

- exact BetterDailyQuest release compatibility;
- how players discover active assignments;
- whether quest and task descriptions are actually displayed;
- how players trigger any addon-specific assignment-start behavior;
- permissions and commands added by the integration;
- behavior when the integration is missing or disabled.

!!! warning "Descriptions are metadata, not a built-in screen"
    The bundled quest format can contain `description` and `task_description`, but core BetterDailyQuest does not display them. Do not promise players a description screen until the chosen presentation layer proves it.

Keep the core [progress cosmetics](../creating-quests/messages-cosmetics.md) useful even when the external menu is temporarily unavailable.
