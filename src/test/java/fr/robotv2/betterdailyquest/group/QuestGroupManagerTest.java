package fr.robotv2.betterdailyquest.group;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class QuestGroupManagerTest {

    @TempDir
    Path tempDirectory;

    @Test
    void malformedCandidateDoesNotChangeTheLoadedSnapshot() throws IOException {
        Path liveDirectory = Files.createDirectory(tempDirectory.resolve("live"));
        writeGroup(liveDirectory, "daily");
        QuestGroupManager live = QuestGroupManager.load(null, liveDirectory.toFile());

        Path candidateDirectory = Files.createDirectory(tempDirectory.resolve("candidate"));
        Files.writeString(candidateDirectory.resolve("groups.yml"), "groups: [invalid");
        QuestGroupManager candidate = QuestGroupManager.load(null, candidateDirectory.toFile());

        assertFalse(candidate.getErrors().isEmpty());
        assertNotNull(live.getGroup("daily"));
        assertTrue(live.getErrors().isEmpty());
    }

    @Test
    void loadedManagersKeepIndependentSnapshots() throws IOException {
        Path liveDirectory = Files.createDirectory(tempDirectory.resolve("live"));
        writeGroup(liveDirectory, "daily");
        QuestGroupManager live = QuestGroupManager.load(null, liveDirectory.toFile());

        Path candidateDirectory = Files.createDirectory(tempDirectory.resolve("candidate"));
        writeGroup(candidateDirectory, "weekly");
        QuestGroupManager candidate = QuestGroupManager.load(null, candidateDirectory.toFile());

        assertNotNull(live.getGroup("daily"));
        assertNull(live.getGroup("weekly"));
        assertNull(candidate.getGroup("daily"));
        assertNotNull(candidate.getGroup("weekly"));
    }

    @Test
    void rejectsInvalidCronSyntax() throws IOException {
        Path directory = Files.createDirectory(tempDirectory.resolve("invalid-cron"));
        Files.writeString(directory.resolve("groups.yml"), """
                groups:
                  daily:
                    automatic-reset: not-a-cron
                """);

        QuestGroupManager groups = QuestGroupManager.load(null, directory.toFile());

        assertFalse(groups.getErrors().isEmpty());
        assertNull(groups.getGroup("daily"));
    }

    private static void writeGroup(Path directory, String id) throws IOException {
        Files.writeString(directory.resolve(id + ".yml"), """
                groups:
                  %s:
                    global-assignment-limit: 1
                """.formatted(id));
    }
}
