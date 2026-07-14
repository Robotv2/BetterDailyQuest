# Prepare a useful support report

Include enough evidence to reproduce the administrator outcome without exposing secrets.

## Environment

- BetterDailyQuest release tag and JAR file name
- Server distribution and exact build
- Minecraft version
- Java version from the server process
- Storage type
- Relevant Vault, permissions, PlaceholderAPI, presentation, and addon versions

## Problem

- What you expected
- What happened instead
- Exact steps from a clean start or player join
- Whether bundled defaults reproduce it
- Whether the same behavior occurs after a clean restart

## Evidence

- Full first relevant exception, including causes
- BetterDailyQuest log lines before and after the failure
- Minimal redacted `config.yml`, group, and quest YAML
- Exact command and sender type, with player names anonymized if needed
- Screenshot only when it shows an in-game or console state that text cannot capture

## Remove secrets

Redact database passwords, private hostnames, IP addresses, tokens, webhook URLs, and unrelated player data. Preserve YAML indentation and key names when redacting values.

[Open a BetterDailyQuest issue](https://github.com/Robotv2/BetterDailyQuest/issues/new)
