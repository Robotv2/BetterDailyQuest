# Assignment lifecycle

A quest assignment moves through three practical states.

| State | Meaning | Can receive progress? |
| --- | --- | --- |
| Not started | The assignment exists but is waiting for an external start action | No |
| Started | Matching player activity can update unfinished tasks | Yes |
| Completed | Every task is complete and quest rewards have run | No |

Most administrators should keep `need-starting: false`. New assignments then enter the started state immediately.

!!! warning "Starting is not a complete core workflow"
    The core plugin can create a not-started assignment when `need-starting: true`, but it currently provides no player or administrator command to start it. This option is excluded from guided setups until an end-to-end workflow is available.

## Progress evaluation

For each started assignment, BetterDailyQuest checks:

1. Does the quest still exist in the configured group?
2. Do quest-level progress conditions pass?
3. Is the task eligible under sequential ordering?
4. Does the task type match the player activity?
5. Do task-level progress conditions pass?
6. Does the activity match the task target?
7. If yes, add progress and check for task and quest completion.

Task rewards run when that task completes. Quest rewards run after all tasks in the assignment are complete.

Related: [Progress conditions](../creating-quests/progress-conditions.md) · [Rewards and actions](../creating-quests/rewards-actions.md)
