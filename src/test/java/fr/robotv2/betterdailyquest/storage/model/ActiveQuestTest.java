package fr.robotv2.betterdailyquest.storage.model;

import fr.robotv2.betterdailyquest.storage.dto.ActiveQuestDto;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActiveQuestTest {

    @Test
    void startingAssignmentMarksItDirtyAndSurvivesDtoRoundTrip() {
        ActiveQuest activeQuest = new ActiveQuest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "stonebreaker",
                "daily",
                System.currentTimeMillis() + 60_000,
                false,
                0
        );

        assertFalse(activeQuest.isStarted());
        assertFalse(activeQuest.isDirty());

        activeQuest.setStarted(true);

        assertTrue(activeQuest.isStarted());
        assertTrue(activeQuest.isDirty());

        ActiveQuest restored = new ActiveQuest(new ActiveQuestDto(activeQuest), Collections.emptySet());

        assertTrue(restored.isStarted());
    }
}
