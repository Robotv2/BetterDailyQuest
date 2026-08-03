package fr.robotv2.betterdailyquest.quest;

import fr.robotv2.betterdailyquest.conditions.ConditionManager;
import fr.robotv2.betterdailyquest.group.QuestGroupManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class QuestManagerTest {

    @TempDir
    Path tempDirectory;

    @Test
    void rejectsDuplicateQuestIdsCaseInsensitivelyAcrossGroups() throws IOException {
        QuestGroupManager groups = groups("daily", "weekly");
        Path quests = Files.createDirectory(tempDirectory.resolve("duplicates"));
        writeQuest(quests.resolve("daily.yml"), "break-block-1", "daily", task("JUMP"));
        writeQuest(quests.resolve("weekly.yml"), "BREAK-BLOCK-1", "weekly", task("JUMP"));

        QuestManager manager = load(quests, groups);

        assertFalse(manager.getErrors().isEmpty());
        assertEquals(1, manager.getQuests().size());
    }

    @Test
    void invalidCandidateDoesNotChangeTheLoadedSnapshot() throws IOException {
        QuestGroupManager groups = groups("daily");
        Path liveDirectory = Files.createDirectory(tempDirectory.resolve("live"));
        writeQuest(liveDirectory.resolve("quest.yml"), "test-quest", "daily", task("JUMP"));
        Path candidateDirectory = Files.createDirectory(tempDirectory.resolve("candidate"));
        writeQuest(candidateDirectory.resolve("quest.yml"), "test-quest", "daily", task("NOT_A_TYPE"));

        QuestManager live = load(liveDirectory, groups);
        QuestManager candidate = load(candidateDirectory, groups);

        assertTrue(live.getErrors().isEmpty());
        assertNotNull(live.fromId("test-quest", "daily"));
        assertFalse(candidate.getErrors().isEmpty());
        assertNotNull(live.fromId("test-quest", "daily"));
    }

    @Test
    void rejectsTargetsForTargetlessTaskTypes() throws IOException {
        QuestManager candidate = candidateQuest("""
                task_type: JUMP
                required_amount: 1
                required_target: STONE
                """);

        assertFalse(candidate.getErrors().isEmpty());
        assertNull(candidate.fromId("test-quest", "daily"));
    }

    @Test
    void rejectsUnknownConditions() {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("conditions.not_registered", true);
        ConditionManager conditions = new ConditionManager(null);

        assertThrows(NullPointerException.class, () -> conditions.toInstance(
                configuration.getConfigurationSection("conditions"), "not_registered"
        ));
    }

    private QuestManager candidateQuest(String taskYaml) throws IOException {
        QuestGroupManager groups = groups("daily");
        Path quests = Files.createDirectory(tempDirectory.resolve("quests-" + System.nanoTime()));
        writeQuest(quests.resolve("quest.yml"), "test-quest", "daily", taskYaml);
        return load(quests, groups);
    }

    private QuestGroupManager groups(String... ids) throws IOException {
        Path directory = Files.createDirectory(tempDirectory.resolve("groups-" + System.nanoTime()));
        StringBuilder yaml = new StringBuilder("groups:\n");
        for(String id : ids) {
            yaml.append("  ").append(id).append(":\n    global-assignment-limit: 1\n");
        }
        Files.writeString(directory.resolve("groups.yml"), yaml);
        return QuestGroupManager.load(null, directory.toFile());
    }

    private static QuestManager load(Path directory, QuestGroupManager groups) {
        try(MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            bukkit.when(Bukkit::getBukkitVersion).thenReturn("1.21.8-R0.1-SNAPSHOT");
            return QuestManager.load(null, directory.toFile(), groups);
        }
    }

    private static String task(String type) {
        return "task_type: " + type + "\nrequired_amount: 1\n";
    }

    private static void writeQuest(Path file, String id, String group, String task) throws IOException {
        Files.writeString(file, """
                quests:
                  %s:
                    group: %s
                    tasks:
                      1:
                %s
                """.formatted(id, group, task.indent(8)));
    }
}
