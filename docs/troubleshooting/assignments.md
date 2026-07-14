# Player does not receive a quest

## Confirm player data loaded

The target must be online and loaded. Look for the player-data load message. If commands say the player is not loaded, resolve database errors and let the player reconnect.

## Manual assignment

For `bdq give <group> <quest> <player>`, verify:

- group and quest both loaded;
- quest belongs to the supplied group;
- player does not already hold the quest;
- a non-repeatable Quest ID is not already in completion history.

## Automatic assignment

Verify:

- group has `automatically-given: true`;
- role or global assignment limit is greater than current assignments;
- enough eligible quests exist;
- Vault returns the expected primary role, or `default`/global fallback is correct;
- expired assignments were removed when the player loaded.

## Eligibility exhaustion

A player may have no eligible quest because every quest is already assigned or completed and non-repeatable. Add eligible content, make the design intentionally repeatable, or wait for an appropriate content change. Repeatedly reloading does not erase completion history.

Related: [Assignment limits and history](../concepts/assignment-limits.md)
