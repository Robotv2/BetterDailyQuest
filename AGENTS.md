# Agent instructions

Before modifying files, creating commits, opening pull requests, or preparing releases in this repository, load and follow `.agents/skills/delivery-gate/SKILL.md`.

The delivery gate is mandatory. In particular:

- use only the neutral branch names allowed by the delivery skill; never include an agent or model name;
- never create tags or GitHub releases manually;
- never change `gradle.properties` outside the automated release pull request;
- keep `CHANGELOG.md`, administrator documentation, tests, and implementation consistent;
- run the checks required by the skill before presenting work as complete;
- do not commit, push, merge, or release unless the user explicitly asks for that action.

Release details are in `RELEASING.md`.
