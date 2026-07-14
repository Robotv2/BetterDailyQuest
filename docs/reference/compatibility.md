# Compatibility

Compatibility claims distinguish startup from gameplay verification.

## Current development evidence

| Server | Java used by smoke check | Evidence date | Startup | Golden-path gameplay |
| --- | --- | --- | --- | --- |
| Paper 1.8.8 | Java 17 | 2026-07-13 | Verified: plugin enabled and server reached ready state | Not yet verified on a tagged release |
| Paper 26.1.2 build 74 | Java 25 | 2026-07-13 | Verified: plugin enabled and server reached ready state | Not yet verified on a tagged release |

The plugin bytecode targets Java 17. The server may require a newer Java runtime, as current Paper does.

## What startup verification proves

- The server accepted the plugin JAR.
- BetterDailyQuest enabled.
- Bundled defaults loaded far enough for the server to reach ready state.

It does not prove every task event, material/entity identifier, optional integration, MariaDB environment, addon, update path, or stored-data migration.

## Release publication rule

Before this page becomes a public compatibility promise for a tag:

1. Run startup checks using the tagged JAR.
2. Complete the Stonebreaker golden path.
3. Verify persistence across restart.
4. Test optional integrations before marking them verified.
5. Capture screenshots from that exact release candidate.

Combinations not listed are unverified, not automatically incompatible.
