#!/usr/bin/env python3
"""Prepare and validate BetterDailyQuest release metadata."""

from __future__ import annotations

import argparse
import datetime as dt
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
GRADLE_PROPERTIES = ROOT / "gradle.properties"
CHANGELOG = ROOT / "CHANGELOG.md"
SEMVER = re.compile(r"^(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)$")
HEADING = re.compile(r"^## \[(\d+\.\d+\.\d+)] - \d{4}-\d{2}-\d{2}$", re.MULTILINE)


def parse_version(value: str) -> tuple[int, int, int]:
    match = SEMVER.fullmatch(value)
    if not match:
        raise SystemExit(f"Invalid semantic version: {value!r}")
    return tuple(map(int, match.groups()))


def project_version() -> str:
    text = GRADLE_PROPERTIES.read_text(encoding="utf-8")
    match = re.search(r"^version=(.+)$", text, re.MULTILINE)
    if not match:
        raise SystemExit("gradle.properties must contain version=<major.minor.patch>")
    version = match.group(1).strip()
    parse_version(version)
    return version


def section(text: str, heading_pattern: str) -> str:
    match = re.search(heading_pattern, text, re.MULTILINE)
    if not match:
        raise SystemExit(f"Missing changelog heading matching {heading_pattern!r}")
    start = match.end()
    following = re.search(r"^## ", text[start:], re.MULTILINE)
    end = start + following.start() if following else len(text)
    return text[start:end].strip()


def validate() -> None:
    version = project_version()
    text = CHANGELOG.read_text(encoding="utf-8")
    headings = HEADING.findall(text)
    if not headings or headings[0] != version:
        found = headings[0] if headings else "none"
        raise SystemExit(
            f"Project version is {version}, but latest changelog release is {found}"
        )
    if not section(text, rf"^## \[{re.escape(version)}] - \d{{4}}-\d{{2}}-\d{{2}}$"):
        raise SystemExit(f"Changelog section {version} has no release notes")
    expected_link = (
        f"[{version}]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v{version}"
    )
    if expected_link not in text:
        raise SystemExit(f"Missing changelog link: {expected_link}")
    print(f"Release metadata is consistent for {version}")


def prepare(version: str, date: str) -> None:
    target = parse_version(version)
    current_text = GRADLE_PROPERTIES.read_text(encoding="utf-8")
    current = project_version()
    if target <= parse_version(current):
        raise SystemExit(f"Release version {version} must be newer than {current}")

    text = CHANGELOG.read_text(encoding="utf-8")
    unreleased = section(text, r"^## \[Unreleased]$")
    if not unreleased:
        raise SystemExit("Add release notes under [Unreleased] before preparing a release")
    if f"## [{version}]" in text or f"[{version}]:" in text:
        raise SystemExit(f"Changelog already contains {version}")

    text = text.replace(
        "## [Unreleased]",
        f"## [Unreleased]\n\n## [{version}] - {date}",
        1,
    )
    old_compare = (
        f"[Unreleased]: https://github.com/Robotv2/BetterDailyQuest/compare/v{current}...HEAD"
    )
    new_links = (
        f"[Unreleased]: https://github.com/Robotv2/BetterDailyQuest/compare/v{version}...HEAD\n"
        f"[{version}]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v{version}"
    )
    if old_compare not in text:
        raise SystemExit(f"Missing changelog comparison link: {old_compare}")
    text = text.replace(old_compare, new_links, 1)

    GRADLE_PROPERTIES.write_text(
        re.sub(r"^version=.+$", f"version={version}", current_text, count=1, flags=re.MULTILINE),
        encoding="utf-8",
    )
    CHANGELOG.write_text(text, encoding="utf-8")
    validate()


def notes(output: Path) -> None:
    version = project_version()
    text = CHANGELOG.read_text(encoding="utf-8")
    body = section(text, rf"^## \[{re.escape(version)}] - \d{{4}}-\d{{2}}-\d{{2}}$")
    output.write_text(body + "\n", encoding="utf-8")
    print(output)


def main() -> None:
    parser = argparse.ArgumentParser()
    subparsers = parser.add_subparsers(dest="command", required=True)
    subparsers.add_parser("validate")
    prepare_parser = subparsers.add_parser("prepare")
    prepare_parser.add_argument("version")
    prepare_parser.add_argument("--date", default=dt.date.today().isoformat())
    notes_parser = subparsers.add_parser("notes")
    notes_parser.add_argument("output", type=Path)
    args = parser.parse_args()

    if args.command == "validate":
        validate()
    elif args.command == "prepare":
        prepare(args.version, args.date)
    else:
        notes(args.output)


if __name__ == "__main__":
    main()
