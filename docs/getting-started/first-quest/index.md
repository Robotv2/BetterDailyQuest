# First quest in 15 minutes

**Outcome:** Assign Stonebreaker, break ten matching blocks, and receive task and quest messages.<br>
**Estimated time:** 10–15 minutes after installation.<br>
**You need:** One online test player and permission to edit YAML and run administrator commands.

This path deliberately uses manual assignment, SQLite, and core features only. It avoids Vault, PlaceholderAPI, addons, menus, and incomplete assignment-start behavior.

## Route

1. [Create the `daily` quest group](create-pool.md).
2. [Create the `stonebreaker` quest](create-quest.md).
3. [Reload, assign, and verify it](verify.md).

## What you will prove

- Group and quest files load.
- The test player can receive an assignment.
- `BREAK` events update the matching task.
- Task and quest rewards run.

After this path, extend the same example through [Creating quests](../../creating-quests/index.md).
