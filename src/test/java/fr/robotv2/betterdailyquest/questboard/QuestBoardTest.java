package fr.robotv2.betterdailyquest.questboard;

import fr.robotv2.betterdailyquest.BetterDailyQuestConfiguration;
import fr.robotv2.betterdailyquest.configurations.QuestBoardConfiguration;
import fr.robotv2.betterdailyquest.configurations.time.TimeFormatConfiguration;
import fr.robotv2.betterdailyquest.group.QuestGroup;
import fr.robotv2.betterdailyquest.quest.Quest;
import fr.robotv2.betterdailyquest.quest.task.Task;
import fr.robotv2.betterdailyquest.quest.type.QuestType;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import fr.robotv2.betterdailyquest.storage.model.ActiveTask;
import fr.robotv2.betterdailyquest.storage.model.QuestPlayer;
import fr.robotv2.betterdailyquest.util.color.ColorProvider;
import fr.robotv2.betterdailyquest.util.placeholder.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestBoardTest {

    @Mock private BetterDailyQuestConfiguration pluginConfiguration;
    @Mock private QuestBoardConfiguration boardConfiguration;
    @Mock private TimeFormatConfiguration timeFormat;
    @Mock private ColorProvider colorProvider;
    @Mock private Player player;
    @Mock private QuestPlayer questPlayer;
    @Mock private Inventory inventory;
    @Mock private PluginManager pluginManager;
    @Mock private Quest quest;
    @Mock private QuestGroup group;
    @Mock private ActiveQuest activeQuest;
    @Mock private Task firstTask;
    @Mock private Task secondTask;
    @Mock private QuestType<?> taskType;
    @Mock private ActiveTask firstActiveTask;
    @Mock private ActiveTask secondActiveTask;

    private QuestBoard board;

    @BeforeEach
    void setUp() {
        lenient().when(pluginConfiguration.getQuestBoardConfiguration()).thenReturn(boardConfiguration);
        lenient().when(pluginConfiguration.getTimeFormatConfiguration()).thenReturn(timeFormat);
        lenient().when(timeFormat.format(anyLong())).thenReturn("1h");
        lenient().when(colorProvider.colorize(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(boardConfiguration.getStatus(activeQuest, true)).thenReturn("Waiting");
        lenient().when(player.getName()).thenReturn("Player");
        lenient().when(quest.getQuestId()).thenReturn("stonebreaker");
        lenient().when(quest.getQuestName()).thenReturn("Stonebreaker");
        lenient().when(quest.getQuestGroup()).thenReturn(group);
        lenient().when(group.getGroupId()).thenReturn("daily");
        lenient().when(quest.getDescription()).thenReturn(List.of("Description"));
        lenient().when(quest.getTasks()).thenReturn(List.of(secondTask, firstTask));
        task(firstTask, firstActiveTask, 1, "First task");
        task(secondTask, secondActiveTask, 2, "Second task");
        lenient().when(activeQuest.getQuestId()).thenReturn("stonebreaker");
        lenient().when(activeQuest.getGroupId()).thenReturn("daily");
        lenient().when(activeQuest.getTasks()).thenReturn(java.util.Set.of(firstActiveTask, secondActiveTask));
        lenient().when(activeQuest.getActiveTask(1)).thenReturn(firstActiveTask);
        lenient().when(activeQuest.getActiveTask(2)).thenReturn(secondActiveTask);
        lenient().when(firstActiveTask.getProgress()).thenReturn(BigDecimal.ONE);
        lenient().when(firstActiveTask.getRequired()).thenReturn(BigDecimal.TEN);
        lenient().when(secondActiveTask.getProgress()).thenReturn(BigDecimal.valueOf(2));
        lenient().when(secondActiveTask.getRequired()).thenReturn(BigDecimal.TEN);
        board = new QuestBoard(player, questPlayer, pluginConfiguration, colorProvider, null, null, inventory);
    }

    @Test
    void ordersAssignmentsCaseInsensitivelyWithoutDroppingCompletedOnes() {
        ActiveQuest zulu = org.mockito.Mockito.mock(ActiveQuest.class);
        ActiveQuest alpha = org.mockito.Mockito.mock(ActiveQuest.class);
        when(zulu.getQuestId()).thenReturn("Zulu");
        when(alpha.getQuestId()).thenReturn("alpha");

        List<ActiveQuest> ordered = QuestBoard.sortedAssignments(List.of(zulu, alpha));

        assertEquals(2, ordered.size());
        assertSame(alpha, ordered.get(0));
        assertSame(zulu, ordered.get(1));
    }

    @Test
    void expandsDescriptionMarkersAndOrdersTaskDescriptionsNumerically() {
        try(MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            bukkit.when(Bukkit::getPluginManager).thenReturn(pluginManager);
            when(pluginManager.isPluginEnabled("PlaceholderAPI")).thenReturn(false);

            assertEquals(
                    List.of("Description", "", "First task", "Second task"),
                    board.expandLore(List.of("%quest_description%", "", "%task_descriptions%"), quest, activeQuest)
            );
        }
    }

    @Test
    void unavailableQuestKeepsItsIdAndUsesUnavailableStatus() {
        when(boardConfiguration.getStatus(activeQuest, false)).thenReturn("Unavailable");
        try(MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            bukkit.when(Bukkit::getPluginManager).thenReturn(pluginManager);
            when(pluginManager.isPluginEnabled("PlaceholderAPI")).thenReturn(false);

            assertEquals("stonebreaker | stonebreaker | Unavailable",
                    board.render("%quest_id% | %quest_name% | %quest_status%", null, activeQuest, null, null));
        }
    }

    @Test
    void missingQuestNameFallsBackToQuestIdInSharedPlaceholder() {
        when(quest.getQuestName()).thenReturn(null);

        assertEquals("stonebreaker", Placeholders.QUEST_PLACEHOLDER.apply("%quest_name%", quest));
    }

    @Test
    void rendersDatabaseDecimalsAsPlayerFacingNumbers() {
        when(firstActiveTask.getProgress()).thenReturn(new BigDecimal("1.0000000000"));
        when(firstActiveTask.getRequired()).thenReturn(new BigDecimal("25.0000000000"));

        try(MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            bukkit.when(Bukkit::getPluginManager).thenReturn(pluginManager);
            when(pluginManager.isPluginEnabled("PlaceholderAPI")).thenReturn(false);

            assertEquals("1/25", board.render(
                    "%task_progress%/%task_required%", quest, activeQuest, firstTask, firstActiveTask));

            when(firstActiveTask.getProgress()).thenReturn(new BigDecimal("2E+10"));
            assertEquals("20000000000", board.render(
                    "%task_progress%", quest, activeQuest, firstTask, firstActiveTask));
        }
    }

    private void task(Task task, ActiveTask activeTask, int id, String description) {
        lenient().when(task.getTaskId()).thenReturn(id);
        lenient().when(task.getTaskDescription()).thenReturn(description);
        lenient().when(task.getParent()).thenReturn(quest);
        lenient().doReturn(taskType).when(task).getType();
        lenient().when(taskType.getLiteral()).thenReturn("BREAK");
        lenient().when(activeTask.isDone()).thenReturn(false);
    }
}
