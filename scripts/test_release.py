import tempfile
import unittest
from pathlib import Path

import release


class ReleaseMetadataTest(unittest.TestCase):
    def setUp(self) -> None:
        self.temp_dir = tempfile.TemporaryDirectory()
        root = Path(self.temp_dir.name)
        self.original_properties = release.GRADLE_PROPERTIES
        self.original_changelog = release.CHANGELOG
        release.GRADLE_PROPERTIES = root / "gradle.properties"
        release.CHANGELOG = root / "CHANGELOG.md"
        release.GRADLE_PROPERTIES.write_text("version=1.2.3\n", encoding="utf-8")
        release.CHANGELOG.write_text(
            "# Changelog\n\n"
            "## [Unreleased]\n\n"
            "### Fixed\n\n- A visible defect.\n\n"
            "## [1.2.3] - 2026-01-01\n\n- Previous release.\n\n"
            "[Unreleased]: https://github.com/Robotv2/BetterDailyQuest/compare/v1.2.3...HEAD\n"
            "[1.2.3]: https://github.com/Robotv2/BetterDailyQuest/releases/tag/v1.2.3\n",
            encoding="utf-8",
        )

    def tearDown(self) -> None:
        release.GRADLE_PROPERTIES = self.original_properties
        release.CHANGELOG = self.original_changelog
        self.temp_dir.cleanup()

    def test_prepare_moves_unreleased_notes_and_updates_version(self) -> None:
        release.prepare("1.2.4", "2026-01-02")

        self.assertEqual("1.2.4", release.project_version())
        changelog = release.CHANGELOG.read_text(encoding="utf-8")
        self.assertIn("## [Unreleased]\n\n## [1.2.4] - 2026-01-02", changelog)
        self.assertIn("compare/v1.2.4...HEAD", changelog)
        release.validate()

    def test_prepare_rejects_empty_unreleased_section(self) -> None:
        changelog = release.CHANGELOG.read_text(encoding="utf-8")
        changelog = changelog.replace(
            "### Fixed\n\n- A visible defect.\n\n", "", 1
        )
        release.CHANGELOG.write_text(changelog, encoding="utf-8")

        with self.assertRaisesRegex(SystemExit, "Add release notes"):
            release.prepare("1.2.4", "2026-01-02")

    def test_prepare_rejects_non_increasing_version(self) -> None:
        with self.assertRaisesRegex(SystemExit, "must be newer"):
            release.prepare("1.2.3", "2026-01-02")


if __name__ == "__main__":
    unittest.main()
