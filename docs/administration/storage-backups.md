# Storage and backups

BetterDailyQuest supports SQLite and MariaDB storage. It stores players, quest assignments, task progress, reroll counts, and completion history.

## SQLite

SQLite is the default and needs no external database server:

```yaml
database:
  type: SQLITE
```

The default database file is `plugins/BetterDailyQuest/data.database`. It is suitable for one server and the simplest first installation.

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

Create the database and a least-privilege database user before starting the plugin. Do not commit real credentials or paste them into public support logs.

## Backup procedure

1. Stop the server cleanly so online player data is saved.
2. Back up `config.yml`, `groups/`, `quests/`, and addon data.
3. For SQLite, copy `data.database` while the server is stopped.
4. For MariaDB, use a consistent database dump from the database server.
5. Record the BetterDailyQuest release tag and server version with the backup.
6. Periodically restore the backup to a private server and verify one player assignment.

## Changing storage type

Changing `database.type` does not document or guarantee data transfer between engines. Treat a storage move as a migration project: preserve both stores, test the exact release, and do not delete the old database until player history and assignments are verified.

Related: [Storage troubleshooting](../troubleshooting/storage-integrations.md)
