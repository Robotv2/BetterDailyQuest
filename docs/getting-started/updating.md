# Update safely

**Outcome:** Replace BetterDailyQuest without losing configuration or player progress.

1. Read the target release notes for config, storage, Java, and server-version changes.
2. Stop the server cleanly so online player state is saved.
3. Back up `plugins/BetterDailyQuest/` and the configured database.
4. Remove the old BetterDailyQuest JAR. Keep only one plugin JAR in `plugins/`.
5. Add the new release JAR and start the server.
6. Verify startup, quest group loading, quest loading, database connection, and one test-player assignment.
7. Keep the backup until the server has completed a normal quest group refresh and restart.

!!! warning "Do not use `/reload` for JAR replacement"
    The BetterDailyQuest `reload` command reloads its configuration and content. It does not unload Java classes or replace the running JAR.

If the update fails, stop the server before restoring the JAR, config, and database as one matching set.

Related: [Storage and backups](../administration/storage-backups.md) · [Startup failures](../troubleshooting/startup.md)
