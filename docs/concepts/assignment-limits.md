# Assignment limits and completion history

## Assignment limit

An assignment limit is the number of assignments a group should contain for a player when BetterDailyQuest fills that group.

The lookup order is:

1. Ask Vault for the player's primary role when Vault and a compatible permissions provider are available.
2. Use the matching entry under `assignment-limits`.
3. Otherwise use `global-assignment-limit`.

Without Vault, BetterDailyQuest uses the role name `default`. An `assignment-limits.default` entry therefore works as an explicit default role override; if it is absent, the global limit is used.

Manual `give` is an administrator action and is not a substitute for automatic limit enforcement.

## Completion history

When a player completes a quest assignment, BetterDailyQuest records its Quest ID in completion history.

- A non-repeatable quest in completion history is excluded from future random selection and manual giving.
- A repeatable quest may be assigned again after completion.
- Quest IDs are compared without case and must be globally unique across groups.

Removing a completion record is not exposed as a core administrator command. Plan repeatability before launching a group.

Related: [Options and inheritance](options-inheritance.md) · [Vault player roles](../integrations/vault.md)
