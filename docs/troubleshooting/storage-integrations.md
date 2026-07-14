# Storage or integration problems

## SQLite

- Confirm the server process can write `plugins/BetterDailyQuest/`.
- Stop the server before copying or replacing `data.database`.
- Check disk space and filesystem errors.
- Restore config and database from the same backup point.

## MariaDB

- Verify host, port, database, username, and redacted password separately.
- Confirm the database allows connections from the Minecraft server host.
- Confirm the user can create/use required tables and read/write/delete rows.
- Check connection limits, TLS requirements, firewall, and MariaDB logs.

Do not switch to SQLite merely to hide a MariaDB connection failure if production data remains in MariaDB.

## Vault

Confirm Vault and the permissions provider both enable. If BetterDailyQuest cannot obtain a primary role, it uses `default`; verify the resulting fallback limit.

## PlaceholderAPI

Confirm PlaceholderAPI enables and the placeholder references a real group with a schedule. `Invalid group` and `No reset scheduled.` are BetterDailyQuest results, not PlaceholderAPI parser failures.

## Addons

Restart after changing addon JARs. Keep exactly one addon entry class per JAR and use an addon version explicitly compatible with the current BetterDailyQuest release.

Related: [Storage and backups](../administration/storage-backups.md) · [Integrations](../integrations/index.md)
