package fr.robotv2.betterdailyquest.storage.model;

import fr.robotv2.betterdailyquest.storage.dto.ActiveQuestDto;
import fr.robotv2.betterdailyquest.group.QuestGroup;
import fr.robotv2.betterdailyquest.quest.Quest;
import fr.robotv2.betterdailyquest.quest.options.Optionnable;
import fr.robotv2.betterdailyquest.quest.task.Task;
import fr.robotv2.betterdailyquest.util.Range;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ActiveQuestTest {

    @Test
    void newAssignmentAndTasksAreDirtyUntilTheirFirstSave() {
        Player player = mock(Player.class);
        Quest quest = mock(Quest.class);
        QuestGroup group = mock(QuestGroup.class);
        Task task = mock(Task.class);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(quest.getQuestId()).thenReturn("stonebreaker");
        when(quest.getQuestGroup()).thenReturn(group);
        when(quest.getTasks()).thenReturn(List.of(task));
        when(quest.getOptionValue(Optionnable.Option.NEED_STARTING)).thenReturn(false);
        when(group.getGroupId()).thenReturn("daily");
        when(group.getNextReset()).thenReturn(System.currentTimeMillis() + 60_000);
        when(task.getTaskId()).thenReturn(1);
        when(task.getRequiredAmount()).thenReturn(new Range(10));

        ActiveQuest activeQuest = new ActiveQuest(player, quest);

        assertTrue(activeQuest.isDirty());
        assertTrue(activeQuest.getTasks().stream().allMatch(ActiveTask::isDirty));
    }

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
