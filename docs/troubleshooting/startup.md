# Plugin does not start

## 1. Confirm the JAR

- Use the JAR attached to [BetterDailyQuest 0.0.1 Beta](https://github.com/Robotv2/BetterDailyQuest/releases/tag/v0.0.1).
- Keep exactly one BetterDailyQuest JAR in `plugins/`.
- Do not rename unrelated JARs to look like BetterDailyQuest.

## 2. Read the first plugin-specific failure

Search the startup log for:

```text
BetterDailyQuest
InvalidPluginException
UnsupportedClassVersionError
Failed to initialize database manager
Error occurred while enabling BetterDailyQuest
```

The first relevant exception usually identifies Java incompatibility, a damaged JAR, a missing shaded dependency, invalid required configuration, or database initialization failure. Later errors may be consequences.

## 3. Check Java and server compatibility

Run `java -version` in the same environment that launches the server. Compare the server, Java, and BetterDailyQuest release with the [compatibility matrix](../reference/compatibility.md).

## 4. Isolate configuration safely

Back up the whole plugin directory. On a private test copy, start the same JAR with freshly generated defaults. If defaults work, compare `config.yml`, groups, quests, addons, and database settings one group at a time.

## 5. Escalate with evidence

Provide the release tag, server build, Java version, full exception, redacted config, and whether defaults reproduce the problem. Use the [support-report checklist](support-report.md).
