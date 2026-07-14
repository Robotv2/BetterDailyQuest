# Reward does not run

## Confirm completion happened

Task rewards require that task to transition from unfinished to complete. Quest rewards require every task to be complete. Repeating activity after completion does not rerun rewards.

## Check the action

1. Prefix is one of `[player]`, `[console]`, `[message]`, `[close]`, or `[sound]`.
2. Prefix and argument are separated by a space, except `[close]`.
3. Command arguments do not include a leading slash.
4. The command exists and works when run manually by the same sender.
5. Sound identifier and expression are valid for the server version.
6. Required placeholders are available in task versus quest context.

## Sender differences

- `[player]` requires the completing player to be online and have permission for the command.
- `[console]` has console authority; test the exact expanded command carefully.
- `[message]`, `[close]`, and `[sound]` are skipped if the player is offline.

Unknown prefixes produce `Unknown action prefix` in the log. Command-specific failures are usually logged by the target command's plugin.

Related: [Reward-action reference](../reference/actions.md)
