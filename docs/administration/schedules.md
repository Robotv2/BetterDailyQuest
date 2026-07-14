# Scheduled quest group refreshes

Set `automatic-reset` on a quest group to remove that group's assignments on a cron4j schedule.

```yaml
automatic-reset: "0 0 * * *"
```

The five fields are minute, hour, day of month, month, and day of week. The scheduler uses the server JVM's default time zone.

| Goal | Schedule |
| --- | --- |
| Every day at midnight | `0 0 * * *` |
| Every day at 06:30 | `30 6 * * *` |
| Every Monday at midnight | `0 0 * * 1` |
| First day of each month at midnight | `0 0 1 * *` |

## Refresh behavior

1. Loaded assignments in the group are removed.
2. Stored assignments in the group are deleted.
3. If `automatically-given: true`, loaded players are filled to their current assignment limit.
4. Offline players are evaluated when their data loads after joining; expired assignments are removed before filling.

## Safe schedule change

Test the expression on a private server, confirm the server time zone, back up storage, then run `bdq reload`. A reload stops old group schedulers and creates schedules from the new group files.

Use `%betterdailyquest_group_reset_<group>%` to display the next refresh through PlaceholderAPI.

Related: [Refresh, restart, and reroll](../concepts/replacement-actions.md)
