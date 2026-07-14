# Quest group or quest will not load

## Quest group checks

1. File is inside `plugins/BetterDailyQuest/groups/` and uses `.yml`.
2. YAML uses spaces, not tabs.
3. Cron text has five valid fields if `automatic-reset` is present.
4. Canonical keys are used: `sequential-tasks`, `assignment-limits`, and `global-assignment-limit`.

## Quest checks

1. Quest is inside `plugins/BetterDailyQuest/quests/`.
2. Quest ID is globally unique, ignoring case.
3. `group` names an already loaded group.
4. Every task ID is numeric.
5. `task_type` appears in the [task-type reference](../reference/task-types.md).
6. Targets exist on the running server version.
7. `LOCATION` references an already loaded world.
8. Condition shapes match the [condition reference](../reference/conditions.md).

## Use the warning block

When a quest fails, BetterDailyQuest logs the Quest ID, the error message, and `This quest will not be loaded`. Fix the first error for that quest, reload, and confirm a success line.

`Duplicate quest id` means the later definition was skipped. Search every file for that ID and keep one canonical definition.

## Prevent repeat failures

Keep backups outside live `groups/` and `quests/` folders. Test content with [the safe content workflow](../creating-quests/testing-changes.md).
