package fr.robotv2.betterdailyquest.storage.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestPlayerTest {

    @Test
    void recordQuestCompletionUpdatesCompletionHistoryAndMarksPlayerDirty() {
        QuestPlayer player = new QuestPlayer(UUID.randomUUID(), "Paula");

        player.recordQuestCompletion("break-block-1");
        player.recordQuestCompletion("break-block-1");

        assertEquals(2, player.getQuestDone().get("break-block-1"));
        assertTrue(player.isDirty());
    }

    @Test
    void hasCompletedQuestUsesCompletionHistory() {
        QuestPlayer player = new QuestPlayer(UUID.randomUUID(), "Paula");

        player.recordQuestCompletion("break-block-1");

        assertTrue(player.hasCompletedQuest("break-block-1"));
    }
}
