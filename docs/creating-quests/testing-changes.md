# Test changes safely

**Outcome:** Prove new content on a test player before exposing it to the live group.

## Safe workflow

1. Copy production configuration and storage to a private test server.
2. Back up the files you will edit.
3. Add or change one quest concept at a time.
4. Run `bdq reload` and inspect every load message.
5. Manually assign the quest with `bdq give <group> <quest> <player>`.
6. Exercise each task target and each negative case.
7. Confirm task rewards, quest rewards, cosmetics, reroll eligibility, and completion history.
8. Restart the server and confirm progress persists.
9. Test a scheduled refresh with a temporary schedule only on the test server.

## Minimum content checklist

- [ ] Quest ID is globally unique and stable.
- [ ] Referenced group exists.
- [ ] Every task ID is numeric and unique inside the quest.
- [ ] Task types and targets exist on every supported server version.
- [ ] Conditions reject and accept the intended activity.
- [ ] Console rewards are safe if a player name contains unexpected characters.
- [ ] Repeatability and assignment limits match the design.
- [ ] Removing or renaming the quest will not strand active production assignments.

## Production rollout

Deploy content during a maintenance window, retain the backup, reload once, and watch logs plus one test player before announcing the quest.

Related: [Debug logs](../administration/debugging.md) · [Content-loading problems](../troubleshooting/content-loading.md)
