package fr.robotv2.betterdailyquest.quest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class QuestManagerTest {

    @Test
    void rejectsDuplicateQuestIdsAcrossQuestPools() {
        QuestManager questManager = new QuestManager(null, null);

        assertNull(questManager.registerQuestId("break-block-1", "daily"));
        assertEquals("daily", questManager.registerQuestId("break-block-1", "weekly"));
    }

    @Test
    void treatsQuestIdsAsCaseInsensitive() {
        QuestManager questManager = new QuestManager(null, null);

        assertNull(questManager.registerQuestId("break-block-1", "daily"));
        assertEquals("daily", questManager.registerQuestId("BREAK-BLOCK-1", "weekly"));
    }

    @Test
    void allowsDifferentQuestIdsInAnyQuestPool() {
        QuestManager questManager = new QuestManager(null, null);

        assertNull(questManager.registerQuestId("break-block-1", "daily"));
        assertNull(questManager.registerQuestId("fish-1", "daily"));
        assertNull(questManager.registerQuestId("mine-1", "weekly"));
    }
}
