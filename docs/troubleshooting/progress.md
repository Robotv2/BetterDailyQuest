# Player activity does not progress

Check the evaluation chain in order.

1. **Loaded player:** The player has completed data loading.
2. **Assignment exists:** Use administrator knowledge or assignment commands to confirm the Quest ID.
3. **Assignment is started:** Keep `need-starting: false` in core setups.
4. **Assignment is unfinished:** Completed tasks do not receive more progress.
5. **Quest still exists:** The stored Quest ID and group still match configured content.
6. **Sequential order:** Earlier tasks are complete when `sequential-tasks: true`.
7. **Task type:** The actual event matches the configured type.
8. **Condition:** Every quest-level and task-level condition passes.
9. **Target:** Material, entity, damage cause, or location matches exactly.
10. **Event is allowed:** Another plugin did not cancel the interaction.

## High-value tests

- Temporarily use one fixed, common target such as `STONE` on a private server.
- Remove conditions one at a time on the test copy.
- Turn on `debug`, reload, reproduce once, then inspect logs.
- For `LOCATION`, confirm world, coordinates, and radius and move across a full-block boundary.
- For `CRAFT`, remember progress counts craft events, not output stack size.

Do not weaken production protection plugins to make cancelled actions count.
