# Reward-action reference

Every action begins with a prefix followed by its argument.

| Prefix | Argument | Runs as | Offline behavior |
| --- | --- | --- | --- |
| `[player]` | Command without leading slash | Completing player | Skipped if player is not online |
| `[console]` | Command without leading slash | Server console | Runs even if player lookup is offline; `%player%` uses stored name |
| `[message]` | Message text | Direct message | Skipped if player is not online |
| `[close]` | No argument | Closes player inventory | Skipped if player is not online |
| `[sound]` | XSeries sound expression | Plays to completing player | Skipped if player is not online |

## Examples

```yaml
rewards:
  - "[console] give %player% diamond 1"
  - "[message] &6You earned a diamond."
  - "[sound] ENTITY_PLAYER_LEVELUP,1,1"
  - "[close]"
```

Actions run in list order. Prefix matching ignores case. Unknown prefixes produce a warning and do not execute.

## Security

- Quest files are privileged because `[console]` can run any server command.
- Do not insert untrusted placeholder output into privileged commands.
- Prefer console commands with explicit, bounded arguments.
- Exercise reward failure cases on a test server.

Applicable internal placeholders depend on whether the list is a task reward or quest reward. See [Placeholder scopes](placeholders.md).
