from __future__ import annotations

import re
import sys
from collections.abc import Iterator
from pathlib import Path
from urllib.parse import unquote, urlsplit

import yaml


ROOT = Path(__file__).resolve().parents[1]
DOCS = ROOT / "docs"
MKDOCS = ROOT / "mkdocs.yml"

MARKDOWN_LINK = re.compile(r"!?\[[^\]]*]\(([^)]+)\)")
MARKDOWN_IMAGE = re.compile(r"!\[([^\]]*)]\(([^)]+)\)")
VERSIONED_RELEASE_LINK = re.compile(
    r"https://github\.com/Robotv2/BetterDailyQuest/releases/tag/v\d+\.\d+\.\d+"
)
HEADING = re.compile(r"^(#{1,6})\s+(.+?)\s*$")
FRONT_MATTER = re.compile(r"\A---\s*\n(.*?)\n---\s*\n", re.DOTALL)

TASK_TYPE = re.compile(r'new QuestType<>\("([A-Z_]+)"')
CONDITION = re.compile(r'registerCondition\("([a-z_]+)"')
ACTION = re.compile(r'case "(\[[a-z]+])"')
SUBCOMMAND = re.compile(r'@Subcommand\("([a-z-]+)"\)')
PERMISSION = re.compile(r'@CommandPermission\("([a-z.]+)"\)')

BANNED_KEYS = {"assignations", "global-assignation", "dependant-tasks"}


class MkDocsLoader(yaml.SafeLoader):
    """Read MkDocs Python-name tags without importing their objects."""


MkDocsLoader.add_multi_constructor(
    "tag:yaml.org,2002:python/name:",
    lambda _loader, suffix, _node: suffix,
)


def markdown_lines(path: Path) -> Iterator[str]:
    """Yield Markdown lines outside fenced code blocks."""
    in_fence = False
    for line in path.read_text(encoding="utf-8").splitlines():
        if line.lstrip().startswith("```"):
            in_fence = not in_fence
            continue
        if not in_fence:
            yield line


def slugify(value: str) -> str:
    value = re.sub(r"<[^>]+>", "", value)
    value = re.sub(r"[`*_~]", "", value).strip().lower()
    value = re.sub(r"[^\w\- ]", "", value, flags=re.UNICODE)
    return re.sub(r"[\s\-]+", "-", value).strip("-")


def plain_title(value: str) -> str:
    return re.sub(r"[`*_~]", "", value).strip()


def anchors(path: Path) -> set[str]:
    found: set[str] = set()
    counts: dict[str, int] = {}
    for line in markdown_lines(path):
        match = HEADING.match(line)
        if not match:
            continue
        base = slugify(match.group(2))
        count = counts.get(base, 0)
        counts[base] = count + 1
        found.add(base if count == 0 else f"{base}_{count}")
    return found


def page_title(path: Path) -> str | None:
    for line in markdown_lines(path):
        match = HEADING.match(line)
        if match and len(match.group(1)) == 1:
            return plain_title(match.group(2))
    return None


def front_matter(path: Path) -> dict[str, object]:
    match = FRONT_MATTER.match(path.read_text(encoding="utf-8"))
    if not match:
        return {}
    value = yaml.safe_load(match.group(1))
    return value if isinstance(value, dict) else {}


def resolve_markdown(source: Path, target: str) -> tuple[Path, str]:
    parsed = urlsplit(unquote(target.strip().strip("<>")))
    path_text = parsed.path
    if path_text.startswith("/"):
        candidate = DOCS / path_text.lstrip("/")
    else:
        candidate = source.parent / path_text
    if not path_text:
        candidate = source
    if candidate.is_dir():
        candidate /= "index.md"
    elif candidate.suffix == "":
        directory_index = candidate / "index.md"
        candidate = directory_index if directory_index.exists() else candidate.with_suffix(".md")
    return candidate.resolve(), parsed.fragment


def validate_links() -> list[str]:
    errors: list[str] = []
    docs_root = DOCS.resolve()
    for source in sorted(DOCS.rglob("*.md")):
        text = source.read_text(encoding="utf-8")
        if VERSIONED_RELEASE_LINK.search(text):
            errors.append(
                f"{source.relative_to(ROOT)}: use the releases page instead of a version-specific release link"
            )
        for match in MARKDOWN_LINK.finditer(text):
            target = match.group(1).split(maxsplit=1)[0]
            if target.startswith(("http://", "https://", "mailto:")):
                continue
            candidate, fragment = resolve_markdown(source, target)
            if docs_root not in candidate.parents and candidate != docs_root:
                errors.append(f"{source.relative_to(ROOT)}: link escapes docs: {target}")
                continue
            if not candidate.is_file():
                errors.append(f"{source.relative_to(ROOT)}: missing link target: {target}")
                continue
            if fragment and candidate.suffix == ".md" and fragment not in anchors(candidate):
                errors.append(
                    f"{source.relative_to(ROOT)}: missing anchor #{fragment} in {candidate.relative_to(ROOT)}"
                )

        for alt, target in MARKDOWN_IMAGE.findall(text):
            if not alt.strip():
                errors.append(f"{source.relative_to(ROOT)}: image has empty alt text: {target}")
    return errors


def nav_entries(value: object) -> Iterator[tuple[str, str]]:
    if isinstance(value, list):
        for item in value:
            yield from nav_entries(item)
    elif isinstance(value, dict):
        for label, target in value.items():
            if isinstance(target, str) and target.endswith(".md"):
                yield str(label), target
            else:
                yield from nav_entries(target)


def validate_navigation() -> list[str]:
    errors: list[str] = []
    config = yaml.load(MKDOCS.read_text(encoding="utf-8"), Loader=MkDocsLoader)
    entries = list(nav_entries(config.get("nav", [])))
    seen: set[str] = set()

    for label, target in entries:
        if target in seen:
            errors.append(f"mkdocs.yml: duplicate navigation target: {target}")
        seen.add(target)

        path = DOCS / target
        if not path.is_file():
            errors.append(f"mkdocs.yml: missing navigation target: {target}")
            continue

        title = page_title(path)
        if title is None:
            errors.append(f"{path.relative_to(ROOT)}: expected one H1 title")
        elif title != plain_title(label):
            errors.append(
                f"{path.relative_to(ROOT)}: H1 '{title}' does not match navigation label '{plain_title(label)}'"
            )

    pages = {str(path.relative_to(DOCS)).replace("\\", "/") for path in DOCS.rglob("*.md")}
    for orphan in sorted(pages - seen):
        errors.append(f"docs/{orphan}: page is not present in mkdocs navigation")

    for path in sorted(DOCS.rglob("*.md")):
        description = front_matter(path).get("description")
        if not isinstance(description, str) or not description.strip():
            errors.append(f"{path.relative_to(ROOT)}: missing front-matter description")

        h1_count = sum(
            1
            for line in markdown_lines(path)
            if (match := HEADING.match(line)) and len(match.group(1)) == 1
        )
        if h1_count != 1:
            errors.append(f"{path.relative_to(ROOT)}: expected one H1 title, found {h1_count}")

    return errors


def walk_keys(value: object) -> Iterator[str]:
    if isinstance(value, dict):
        for key, child in value.items():
            yield str(key)
            yield from walk_keys(child)
    elif isinstance(value, list):
        for child in value:
            yield from walk_keys(child)


def source_task_types() -> set[str]:
    path = ROOT / "src/main/java/fr/robotv2/betterdailyquest/quest/type/QuestTypes.java"
    return set(TASK_TYPE.findall(path.read_text(encoding="utf-8")))


def validate_examples() -> list[str]:
    errors: list[str] = []
    known_types = source_task_types()
    quest_ids: set[str] = set()

    for recipe in sorted(path for path in (DOCS / "examples").iterdir() if path.is_dir()):
        groups: set[str] = set()
        for path in sorted((recipe / "groups").glob("*.yml")):
            try:
                value = yaml.safe_load(path.read_text(encoding="utf-8"))
            except yaml.YAMLError as exc:
                errors.append(f"{path.relative_to(ROOT)}: invalid YAML: {exc}")
                continue
            if not isinstance(value, dict):
                errors.append(f"{path.relative_to(ROOT)}: expected a YAML mapping")
                continue
            groups.update(value.get("groups", {}).keys() if isinstance(value.get("groups"), dict) else [path.stem])
            banned = BANNED_KEYS.intersection(walk_keys(value))
            if banned:
                errors.append(f"{path.relative_to(ROOT)}: uses removed keys: {', '.join(sorted(banned))}")

        for path in sorted((recipe / "quests").glob("*.yml")):
            try:
                value = yaml.safe_load(path.read_text(encoding="utf-8"))
            except yaml.YAMLError as exc:
                errors.append(f"{path.relative_to(ROOT)}: invalid YAML: {exc}")
                continue
            quests = value.get("quests") if isinstance(value, dict) else None
            if not isinstance(quests, dict):
                errors.append(f"{path.relative_to(ROOT)}: expected a quests mapping")
                continue

            for quest_id, quest in quests.items():
                normalized_id = str(quest_id).lower()
                if normalized_id in quest_ids:
                    errors.append(f"{path.relative_to(ROOT)}: duplicate Quest ID: {quest_id}")
                quest_ids.add(normalized_id)

                if not isinstance(quest, dict):
                    errors.append(f"{path.relative_to(ROOT)}: quest {quest_id} must be a mapping")
                    continue
                if quest.get("group") not in groups:
                    errors.append(
                        f"{path.relative_to(ROOT)}: quest {quest_id} references missing recipe group {quest.get('group')}"
                    )
                tasks = quest.get("tasks")
                if not isinstance(tasks, dict) or not tasks:
                    errors.append(f"{path.relative_to(ROOT)}: quest {quest_id} needs tasks")
                    continue
                for task_id, task in tasks.items():
                    if not str(task_id).isdigit():
                        errors.append(f"{path.relative_to(ROOT)}: task ID must be numeric: {task_id}")
                    if not isinstance(task, dict) or task.get("task_type") not in known_types:
                        errors.append(
                            f"{path.relative_to(ROOT)}: task {task_id} has invalid type {task.get('task_type') if isinstance(task, dict) else None}"
                        )

            banned = BANNED_KEYS.intersection(walk_keys(value))
            if banned:
                errors.append(f"{path.relative_to(ROOT)}: uses removed keys: {', '.join(sorted(banned))}")

    return errors


def validate_catalog_coverage() -> list[str]:
    errors: list[str] = []
    checks = [
        (
            source_task_types(),
            " ".join(path.read_text(encoding="utf-8") for path in sorted((DOCS / "reference").glob("task-types*.md"))),
            "task type",
        ),
        (
            set(CONDITION.findall((ROOT / "src/main/java/fr/robotv2/betterdailyquest/conditions/ConditionManager.java").read_text(encoding="utf-8"))),
            (DOCS / "reference/conditions.md").read_text(encoding="utf-8"),
            "condition",
        ),
        (
            set(ACTION.findall((ROOT / "src/main/java/fr/robotv2/betterdailyquest/util/ListActionProcessor.java").read_text(encoding="utf-8"))),
            (DOCS / "reference/actions.md").read_text(encoding="utf-8"),
            "reward action",
        ),
    ]

    command_source = (ROOT / "src/main/java/fr/robotv2/betterdailyquest/command/BetterDailyQuestCommand.java").read_text(encoding="utf-8")
    command_reference = (DOCS / "reference/commands-permissions.md").read_text(encoding="utf-8")
    checks.append((set(SUBCOMMAND.findall(command_source)), command_reference, "subcommand"))
    checks.append((set(PERMISSION.findall(command_source)), command_reference, "permission"))

    for expected, text, label in checks:
        for value in sorted(expected):
            if value not in text:
                errors.append(f"Documentation is missing source-backed {label}: {value}")
    return errors


def validate_required_files() -> list[str]:
    required = [
        DOCS / "index.md",
        DOCS / "getting-started/first-quest.md",
        DOCS / "creating-quests/recipes.md",
        DOCS / "reference/task-types.md",
        DOCS / "reference/compatibility.md",
    ]
    recipes = {
        "stonebreaker": ("daily.yml", "stonebreaker.yml"),
        "ranch-hand": ("ranch.yml", "ranch-hand.yml"),
        "workshop-order": ("workshop.yml", "workshop-order.yml"),
        "wayfinder": ("exploration.yml", "wayfinder.yml"),
        "underground-miner": ("mining.yml", "underground-miner.yml"),
    }
    for recipe, (group_file, quest_file) in recipes.items():
        required.append(DOCS / "examples" / recipe / "groups" / group_file)
        required.append(DOCS / "examples" / recipe / "quests" / quest_file)
    return [f"Missing required file: {path.relative_to(ROOT)}" for path in required if not path.is_file()]


def main() -> int:
    errors = (
        validate_required_files()
        + validate_navigation()
        + validate_examples()
        + validate_catalog_coverage()
        + validate_links()
    )
    if errors:
        print("Documentation validation failed:", file=sys.stderr)
        for error in errors:
            print(f"- {error}", file=sys.stderr)
        return 1

    markdown_count = sum(1 for _ in DOCS.rglob("*.md"))
    yaml_count = sum(1 for _ in (DOCS / "examples").rglob("*.yml"))
    print(f"Validated {markdown_count} Markdown pages and {yaml_count} YAML examples.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
