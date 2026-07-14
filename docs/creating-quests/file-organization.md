# Organize quest files

BetterDailyQuest reads group files from `plugins/BetterDailyQuest/groups/` and quest files from `plugins/BetterDailyQuest/quests/`.

## Recommended layout

```text
plugins/BetterDailyQuest/
├── groups/
│   ├── daily.yml
│   ├── weekly.yml
│   └── events.yml
└── quests/
    ├── daily-gathering.yml
    ├── daily-combat.yml
    ├── weekly.yml
    └── events/
        └── summer.yml
```

Use lowercase file names with hyphens. Organize by group and theme, but keep every Quest ID globally unique.

## One group per file

The file name becomes the group ID when the file has no `groups` wrapper:

```yaml
# groups/daily.yml -> group ID "daily"
options:
  repeatable: false
automatic-reset: "0 0 * * *"
```

Multiple groups may share a file under a `groups` section, but separate files make reviews, links, and troubleshooting easier.

## One or many quests per file

Use a `quests` wrapper when storing multiple quests:

```yaml
quests:
  stonebreaker:
    group: daily
    tasks: {}
  lumber-day:
    group: daily
    tasks: {}
```

Use `.yml` files only. Do not place notes, backups, or unrelated files in the quest directory; move backups outside the live plugin folder.

Related: [Quest group schema](../reference/pool-schema.md) · [Quest schema](../reference/quest-schema.md)
