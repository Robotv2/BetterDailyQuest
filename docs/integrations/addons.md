# Addons

BetterDailyQuest can load separately supplied addon JARs from `plugins/BetterDailyQuest/addons/`.

## Install an addon

1. Confirm the addon explicitly supports your BetterDailyQuest and server versions.
2. Stop the server.
3. Create `plugins/BetterDailyQuest/addons/` if it does not exist.
4. Place one trusted addon JAR directly in that directory.
5. Start the server and look for `Addon <name> (<version>) has been loaded successfully.`
6. Follow the addon's own administrator guide for its config, commands, and permissions.

Addon discovery happens during plugin load. A BetterDailyQuest config reload is not a substitute for restarting after adding, removing, or replacing an addon JAR.

## Failure signals

BetterDailyQuest skips an addon when it cannot find exactly one addon entry class or cannot instantiate it. Remove the failing JAR, restart, and report the full startup error to the addon author.

Addons execute code with server-plugin access. Install only artifacts from a trusted source and include their files in the backup plan.
