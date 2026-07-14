from __future__ import annotations

import re
import sys
from pathlib import Path
from urllib.parse import unquote, urlsplit

import yaml


ROOT = Path(__file__).resolve().parents[1]
DOCS = ROOT / "docs"
MARKDOWN_LINK = re.compile(r"!?\[[^\]]*]\(([^)]+)\)")
HEADING = re.compile(r"^(#{1,6})\s+(.+?)\s*$")


def slugify(value: str) -> str:
    value = re.sub(r"<[^>]+>", "", value)
    value = re.sub(r"[`*_~]", "", value).strip().lower()
    value = re.sub(r"[^\w\- ]", "", value, flags=re.UNICODE)
    return re.sub(r"[\s\-]+", "-", value).strip("-")


def anchors(path: Path) -> set[str]:
    found: set[str] = set()
    counts: dict[str, int] = {}
    for line in path.read_text(encoding="utf-8").splitlines():
        match = HEADING.match(line)
        if not match:
            continue
        base = slugify(match.group(2))
        count = counts.get(base, 0)
        counts[base] = count + 1
        found.add(base if count == 0 else f"{base}_{count}")
    return found


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
                errors.append(f"{source.relative_to(ROOT)}: missing anchor #{fragment} in {candidate.relative_to(ROOT)}")
    return errors


def validate_yaml() -> list[str]:
    errors: list[str] = []
    for path in sorted((DOCS / "examples").rglob("*.yml")):
        try:
            value = yaml.safe_load(path.read_text(encoding="utf-8"))
            if not isinstance(value, dict):
                errors.append(f"{path.relative_to(ROOT)}: expected a YAML mapping")
        except yaml.YAMLError as exc:
            errors.append(f"{path.relative_to(ROOT)}: invalid YAML: {exc}")
    return errors


def validate_required_files() -> list[str]:
    required = [
        DOCS / "index.md",
        DOCS / "reference" / "verification-matrix.md",
        DOCS / "reference" / "known-limitations.md",
        DOCS / "examples" / "stonebreaker" / "groups" / "daily.yml",
        DOCS / "examples" / "stonebreaker" / "quests" / "stonebreaker.yml",
    ]
    return [f"Missing required file: {path.relative_to(ROOT)}" for path in required if not path.is_file()]


def main() -> int:
    errors = validate_required_files() + validate_yaml() + validate_links()
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
