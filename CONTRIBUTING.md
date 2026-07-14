# Contributing to BetterDailyQuest

## Administrator-visible changes

Update the administrator guide in the same pull request whenever a change affects:

- configuration keys, accepted values, defaults, or inheritance;
- commands, arguments, permissions, or messages;
- task types, targets, progress conditions, rewards, or placeholders;
- integrations, storage, compatibility, or operational behavior;
- setup, update, backup, reload, refresh, reroll, or troubleshooting procedures.

Update guided content and the corresponding reference page. Add or update a tested example when behavior is configuration-driven. Do not document intended behavior as available behavior.

## Local documentation checks

```powershell
python -m pip install -r requirements-docs.txt
python scripts/validate_docs.py
$env:NO_MKDOCS_2_WARNING = "true"
python -m mkdocs build --strict
```

The public site deploys from a published GitHub release tag only after the release contains at least one JAR asset.
