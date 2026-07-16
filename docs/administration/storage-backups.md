---
description: Configure BetterDailyQuest SQLite or MariaDB storage and create safe backups.
---

# Configure storage and backups

BDQ stores players, assignments, task progress, reroll counts, and completion history.

## SQLite

SQLite is the default and needs no external database server.

```yaml
database:
  type: SQLITE
```

The database file is `plugins/BetterDailyQuest/data.database`. SQLite is the simplest choice for one server.

## MariaDB

```yaml
database:
  type: MARIADB
  mariadb:
    host: "127.0.0.1"
    port: 3306
    database: "betterdailyquest"
    username: "betterdailyquest"
    password: "replace-with-a-secret"
```

Create the database and a user before starting BDQ. Give the user only the database access it needs. Never commit real passwords or post them in public logs.

## Backup procedure

1. Stop the server cleanly so online player data is saved.
2. Back up `config.yml`, `groups`, `quests`, and addon data.
3. For SQLite, copy `data.database` while the server is stopped.
4. For MariaDB, create a consistent database dump.
5. Record the BDQ release and server version with the backup.
6. Test a restore on a private server.

## Restore procedure

1. Stop the server.
2. Keep a copy of the current failed state for investigation.
3. Restore the plugin files and database from the same backup time.
4. Start the private test server first.
5. Check one player's assignments and completion history.

## Changing storage type

Changing `database.type` does not copy data between SQLite and MariaDB. Treat this as a data migration. Keep both databases until the new system has been checked.

## Storage problems

For SQLite, check folder permissions, free disk space, and filesystem errors. For MariaDB, check the host, port, database, user access, firewall, connection limits, and MariaDB logs.

Do not switch storage types only to hide a connection error. Production data remains in the old database.
