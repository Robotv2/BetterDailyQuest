---
description: Test BetterDailyQuest content safely before using it on a production server.
---

# Test quest changes

**Result:** New quest content works with a test player before players see it in production.

## Test workflow

1. Copy the production setup to a private test server.
2. Back up the files and database you will use.
3. Change one quest idea at a time.
4. Run `bdq reload`.
5. Read every warning for the changed group and quest.
6. Give the quest to a test player with `bdq give`.
7. If it uses `need-starting: true`, confirm progress is ignored before `bdq start`, then start it.
8. Test actions that should add progress.
9. Test actions that should not add progress.
10. Check task rewards, quest rewards, and cosmetics.
11. Restart the server and check saved progress and started state.
12. Test reset or reroll behavior when the quest uses it.

## Content checklist

- [ ] The Quest ID is unique and will stay stable.
- [ ] The referenced group exists.
- [ ] Every task ID is a unique number.
- [ ] Task types and targets exist on supported server versions.
- [ ] Conditions pass and fail in the correct situations.
- [ ] Console commands are safe after placeholders are replaced.
- [ ] Repeatability and assignment limits match the intended design.
- [ ] Waiting assignments ignore progress until they are started.
- [ ] Removing old content will not leave broken saved assignments.

## Production rollout

Use a maintenance window. Keep a backup, reload once, and watch the full log. Test one real assignment before announcing the new content.

If the reload fails, keep the first error and restore the matching files and database together. Do not keep reloading an unknown broken state.

## Next step

Use the [Quest recipe book](recipes.md) or look up exact values in the [Reference](../reference/index.md).
