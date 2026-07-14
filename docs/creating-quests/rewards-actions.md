# Rewards and actions

Rewards are action lists. Task rewards run when one task completes; quest rewards run when every task in the assignment is complete.

## Message rewards

```yaml
task_rewards:
  - "[message] &aTask complete."

rewards:
  - "[message] &6Quest complete."
```

## Console reward

```yaml
rewards:
  - "[console] give %player% diamond 1"
```

The console action runs with server-console authority. Treat quest files as privileged configuration and review every command before deployment.

## Execution order

Actions run in listed order. A failing or unknown prefix is logged; it does not become a console command automatically.

Supported prefixes are `[player]`, `[console]`, `[message]`, `[close]`, and `[sound]`. See the [reward-action reference](../reference/actions.md) for arguments and constraints.

## Placeholders

Reward text can use internal quest/task placeholders appropriate to the event, including `%player%`, `%quest_id%`, `%quest_name%`, `%task_progress%`, and `%task_required%`.

These are not automatically PlaceholderAPI placeholders. See [Placeholder scopes](../reference/placeholders.md).
