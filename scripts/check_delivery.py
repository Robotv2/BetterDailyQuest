#!/usr/bin/env python3
"""Check machine-enforceable contribution and repository policies."""

from __future__ import annotations

import argparse
import re
import subprocess
from pathlib import PurePosixPath

NORMAL_BRANCH = re.compile(
    r"^(feat|fix|docs|refactor|test|build|chore)/[a-z0-9][a-z0-9-]*$"
)
RELEASE_BRANCH = re.compile(r"^release/v(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)$")
RELEASE_FILES = {"CHANGELOG.md", "gradle.properties"}
IGNORED_PREFIXES = ("build/", ".gradle/", ".idea/")
SECRET_NAMES = {".env", "credentials.json", "id_rsa", "id_ed25519"}


def git(*arguments: str) -> list[str]:
    result = subprocess.run(
        ["git", *arguments], check=True, capture_output=True, text=True
    )
    return [line for line in result.stdout.splitlines() if line]


def validate_branch(branch: str) -> None:
    if NORMAL_BRANCH.fullmatch(branch) or RELEASE_BRANCH.fullmatch(branch):
        return
    raise SystemExit(
        "Invalid branch name. Use feat/*, fix/*, docs/*, refactor/*, test/*, "
        "build/*, chore/*, or release/v<major.minor.patch>."
    )


def validate_change_set(
    branch: str,
    changed: set[str],
    version: str,
    version_changed: bool | None = None,
) -> None:
    version_file_changed = "gradle.properties" in changed
    if version_changed is None:
        version_changed = version_file_changed
    release_match = RELEASE_BRANCH.fullmatch(branch)

    if version_file_changed and not version_changed:
        if branch.startswith("build/") and "build.gradle" in changed:
            return  # One-time or later migration of an unchanged version source.
        raise SystemExit("Implementation pull requests must not edit gradle.properties.")

    if version_changed:
        expected = f"release/v{version}"
        if branch != expected:
            raise SystemExit(
                f"Version {version} may be changed only on branch {expected}."
            )
        unexpected = changed - RELEASE_FILES
        if unexpected:
            raise SystemExit(
                "Release pull requests may change only CHANGELOG.md and "
                f"gradle.properties; found: {', '.join(sorted(unexpected))}"
            )
        if changed != RELEASE_FILES:
            raise SystemExit("A release pull request must update both release files.")
    elif release_match:
        raise SystemExit("A release branch must update gradle.properties.")


def version_at(ref: str) -> str:
    for path, pattern in (
        ("gradle.properties", r"^version=(.+)$"),
        ("build.gradle", r"^version\s*=\s*['\"](.+)['\"]$"),
    ):
        try:
            text = "\n".join(git("show", f"{ref}:{path}"))
        except subprocess.CalledProcessError:
            continue
        match = re.search(pattern, text, re.MULTILINE)
        if match:
            return match.group(1).strip()
    raise SystemExit(f"Cannot find project version at {ref}")


def prohibited_files(paths: list[str]) -> list[str]:
    prohibited: list[str] = []
    for raw_path in paths:
        path = PurePosixPath(raw_path)
        if raw_path.startswith(IGNORED_PREFIXES):
            prohibited.append(raw_path)
        elif "__pycache__" in path.parts or path.suffix in {".pyc", ".pyo"}:
            prohibited.append(raw_path)
        elif path.name in SECRET_NAMES or path.suffix == ".key":
            prohibited.append(raw_path)
    return prohibited


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--base", help="Base commit for pull-request changes")
    parser.add_argument("--branch", help="Pull-request head branch")
    args = parser.parse_args()

    tracked = git("ls-files")
    prohibited = prohibited_files(tracked)
    if prohibited:
        raise SystemExit("Prohibited tracked files:\n" + "\n".join(prohibited))

    if bool(args.base) != bool(args.branch):
        parser.error("--base and --branch must be provided together")
    if args.branch:
        validate_branch(args.branch)
        changed = set(git("diff", "--name-only", f"{args.base}...HEAD"))
        from release import project_version

        current_version = project_version()
        validate_change_set(
            args.branch,
            changed,
            current_version,
            version_changed=version_at(args.base) != current_version,
        )

    print("Delivery policy checks passed")


if __name__ == "__main__":
    main()
