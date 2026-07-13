package fr.robotv2.betterdailyquest.group;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestGroupTest {

    @Test
    void globalAssignmentLimitSetsGlobalAssignmentLimit() {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("global-assignment-limit", 7);

        QuestGroup group = new QuestGroup("daily", configuration);

        assertEquals(7, group.getGlobalAssignation());
    }

    @Test
    void legacyGlobalAssignationIsIgnored() {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("global-assignation", 7);

        QuestGroup group = new QuestGroup("daily", configuration);

        assertEquals(0, group.getGlobalAssignation());
    }

    @Test
    void assignmentLimitsSetsRoleAssignmentLimits() {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("assignment-limits.vip", 10);

        QuestGroup group = new QuestGroup("daily", configuration);

        assertEquals(10, group.getRoleAssignation("vip").orElseThrow());
    }

    @Test
    void legacyAssignationsAreIgnored() {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("assignations.vip", 10);

        QuestGroup group = new QuestGroup("daily", configuration);

        assertTrue(group.getRoleAssignation("vip").isEmpty());
    }
}
