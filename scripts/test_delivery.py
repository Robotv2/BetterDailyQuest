import unittest

import check_delivery


class DeliveryPolicyTest(unittest.TestCase):
    def test_accepts_normal_and_release_branches(self) -> None:
        for branch in ("fix/mysql-progress", "build/delivery-gate", "release/v1.2.3"):
            check_delivery.validate_branch(branch)

    def test_rejects_agent_named_branch(self) -> None:
        with self.assertRaisesRegex(SystemExit, "Invalid branch"):
            check_delivery.validate_branch("codex/mysql-progress")

    def test_rejects_version_change_on_implementation_branch(self) -> None:
        with self.assertRaisesRegex(SystemExit, "release/v1.2.4"):
            check_delivery.validate_change_set(
                "fix/mysql-progress", {"CHANGELOG.md", "gradle.properties"}, "1.2.4"
            )

    def test_allows_unchanged_version_source_migration(self) -> None:
        check_delivery.validate_change_set(
            "build/delivery-gate",
            {"build.gradle", "gradle.properties", "scripts/check_delivery.py"},
            "1.2.3",
            version_changed=False,
        )

    def test_release_pull_request_is_metadata_only(self) -> None:
        check_delivery.validate_change_set(
            "release/v1.2.4", {"CHANGELOG.md", "gradle.properties"}, "1.2.4"
        )
        with self.assertRaisesRegex(SystemExit, "may change only"):
            check_delivery.validate_change_set(
                "release/v1.2.4",
                {"CHANGELOG.md", "gradle.properties", "src/main/Fix.java"},
                "1.2.4",
            )

    def test_detects_generated_files_and_secrets(self) -> None:
        paths = [
            "src/Main.java",
            "build/plugin.jar",
            "scripts/__pycache__/tool.pyc",
            ".env",
        ]
        self.assertEqual(paths[1:], check_delivery.prohibited_files(paths))


if __name__ == "__main__":
    unittest.main()
