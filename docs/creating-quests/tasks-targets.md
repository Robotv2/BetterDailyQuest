# Tasks and targets

A task answers three questions:

1. Which activity counts? `task_type`
2. What object or value must match? `required_target` or `required_targets`
3. How much is needed? `required_amount`

## Basic task

```yaml
tasks:
  1:
    task_type: BREAK
    required_amount: 10
    required_targets:
      - STONE
      - COBBLESTONE
```

## Target forms

| Form | Meaning |
| --- | --- |
| `required_target: STONE` | One allowed target |
| `required_targets: [STONE, COBBLESTONE]` | Any listed target |
| `required_targets: ["*"]` | Every target of the required type |
| `required_targets: ["*", "!TNT"]` | Every target except excluded values |
| `required_targets: ["TAG:LOGS"]` | Values provided by the matching XSeries tag |

Target names must be valid for the server version. A value that exists only in a newer Minecraft release cannot load on an older server.

Some task types, such as `CARVE`, `MILK`, and `SHEAR`, do not need a target. `LOCATION` uses a `required_location` section instead.

## Amount ranges

Most task types accept a fixed number or a range string:

```yaml
required_amount: "8-12"
```

The assignment chooses a value in that range when its task progress is created. Use fixed values in tutorials and ranges only when varied effort is intentional.

Related: [Task type matrix](../reference/task-types.md)
