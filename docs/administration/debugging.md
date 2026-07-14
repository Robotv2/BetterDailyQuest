# Debug logs

Set this in `config.yml` when investigating assignment or player-load behavior:

```yaml
debug: true
```

Then run `bdq reload` and reproduce the problem with one test player. Debug output can include group fill decisions and player-data timing.

## High-signal messages

| Message pattern | Meaning |
| --- | --- |
| `<quest> has been loaded successfully` | Quest parsing and registration succeeded |
| `Duplicate quest id` | A later conflicting quest was skipped |
| `This quest will not be loaded` | Quest construction failed; inspect the preceding error message |
| `Failed to initialize database manager` | Storage configuration or connection prevented startup |
| `Data of player ... have been loaded` | Player data reached the in-memory cache |
| `Failed to save data` | Progress may not have persisted; protect the current logs and database |

Disable debug logging after collecting the evidence to keep production logs readable.

Never publish database passwords, full connection strings, player IP addresses, or unrelated plugin secrets in a support report.

Related: [Ask for help](../troubleshooting/support-report.md)
