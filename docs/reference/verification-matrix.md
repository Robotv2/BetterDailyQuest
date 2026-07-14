# Administrator-surface verification matrix

This matrix records what the current documentation baseline can prove. It prevents a configured or source-visible feature from being described as a completed administrator workflow without evidence.

## Evidence levels

| Level | Meaning |
| --- | --- |
| Source-confirmed | The current implementation reads or exposes the surface as documented |
| Unit-tested | Automated tests exercise the stated rule without a full server |
| Startup-verified | A real server enabled the plugin and reached ready state |
| Workflow-verified | A real player/server sequence produced the documented outcome |
| Release-gated | Requires the tagged production JAR or an unresolved product behavior |

## Baseline

| Surface | Requirement | Observed behavior | Evidence | Documentation |
| --- | --- | --- | --- | --- |
| Plugin startup | Valid release JAR and supported server Java | Creates default config, group, quest, and SQLite database; registers commands/listeners | Startup-verified on Paper 1.8.8 and 26.1.2 | [Installation](../getting-started/installation.md) |
| Global config | Required `cosmetics`, `time_format`, and `messages` shape | Loads debug, database through Anchor, cosmetics, time labels, and command messages | Source-confirmed; startup-verified defaults | [`config.yml`](config.md) |
| Quest group loading | Valid `.yml` in `groups/` | File name or `groups` wrapper supplies quest group IDs; IDs resolve without case | Source-confirmed | [Quest group schema](pool-schema.md) |
| Quest loading | Existing group and globally unique Quest ID | Invalid quest is skipped with warnings; later duplicate ID is skipped | Unit-tested and source-confirmed | [Quest schema](quest-schema.md) |
| Quest options | Quest override, then group, then plugin default | `repeatable`, `sequential-tasks`, `need-starting`; group filling uses `automatically-given` | Unit-tested and source-confirmed | [Options](../concepts/options-inheritance.md) |
| Completion history | Completed assignment | Quest ID recorded and used to exclude non-repeatable quests | Unit-tested and source-confirmed | [Limits and history](../concepts/assignment-limits.md) |
| Task progress | Started assignment and matching event/type/condition/target | Adds event amount, runs completion events and rewards | Source-confirmed; Stonebreaker workflow release-gated | [Task types](task-types.md) |
| Sequential tasks | `sequential-tasks: true` | Stops evaluation after the first unfinished earlier task | Unit-tested and source-confirmed | [Sequential tasks](../creating-quests/sequential-tasks.md) |
| Rewards | Known action prefix | Runs listed actions in order with event placeholders | Source-confirmed; release screenshot gated | [Actions](actions.md) |
| Cosmetics | Configured event section | First present task/quest/group/global section wins | Source-confirmed | [`config.yml`](config.md) |
| Commands | Online, loaded target and permission | Reload, give, clear, restart, self/staff reroll, complete | Source-confirmed | [Commands](commands-permissions.md) |
| SQLite | `database.type: SQLITE` | Connects and creates `data.database` plus required tables | Startup-verified | [Storage](../administration/storage-backups.md) |
| MariaDB | Valid host, credentials, query-capable backend | Connects and initializes repositories | Source-confirmed; release environment verification pending | [Storage](../administration/storage-backups.md) |
| Vault | Enabled Vault and provider with primary-role support | Selects role limit; otherwise uses `default` then global limit | Source-confirmed; integration workflow pending | [Vault](../integrations/vault.md) |
| PlaceholderAPI expansion | Enabled PlaceholderAPI | Exposes group reset countdown only | Source-confirmed; integration workflow pending | [Placeholders](placeholders.md) |
| Addons | Trusted JAR with exactly one addon class | Loads direct child JAR during plugin load and invokes lifecycle hooks | Source-confirmed; addon-specific behavior external | [Addons](../integrations/addons.md) |
| `need-starting` | External start behavior | Creates a not-started assignment; core has no start command | Release-gated product gap | [Known limitations](known-limitations.md) |
| Numerical placeholder comparator | Placeholder condition | Comparator result is followed by exact text equality | Release-gated product gap | [Conditions](conditions.md) |
| Public screenshots | Tagged production JAR | Must show startup, progress, completion, and reward from matching release | Release-gated | [Compatibility](compatibility.md) |

## Baseline identity

This inventory was authored against repository commit `e80b675`. The public site must be rebuilt from a release tag; the matrix should be re-audited whenever administrator-visible behavior changes.
