package fr.robotv2.betterdailyquest.configurations;

import fr.robotv2.betterdailyquest.group.QuestGroup;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import fr.robotv2.betterdailyquest.storage.model.QuestPlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QuestBoardConfigurationTest {

    @Test
    void acceptsValidConfigurationAndLoadedGroupCoverage() throws Exception {
        QuestBoardConfiguration board = board(validYaml());
        board.validateGroups(List.of(group("daily")));

        assertTrue(board.isEnabled());
        assertEquals(List.of(10, 11, 12), board.getGroups().iterator().next().getSlots());
    }

    @Test
    void rejectsInvalidRowsAndMaterials() throws Exception {
        YamlConfiguration yaml = validYaml();
        yaml.set("quest-board.rows", 7);
        yaml.set("quest-board.title", "This inventory title is longer than thirty-two characters");
        yaml.set("quest-board.filler-item.material", "NOT_A_MATERIAL");

        assertFalse(board(yaml).isEnabled());
    }

    @Test
    void rejectsOutOfBoundsDuplicateAndOverlappingSlots() throws Exception {
        YamlConfiguration yaml = validYaml();
        yaml.set("quest-board.groups.daily.slots", List.of(10, 10, 60));
        yaml.set("quest-board.groups.weekly.slots", List.of(10));
        yaml.set("quest-board.groups.weekly.quest-item.material", "PAPER");
        yaml.set("quest-board.groups.weekly.quest-item.name", "Weekly");
        yaml.set("quest-board.groups.weekly.empty-item.material", "BARRIER");
        yaml.set("quest-board.groups.weekly.empty-item.name", "Empty");

        assertFalse(board(yaml).isEnabled());
    }

    @Test
    void rejectsMissingLoadedGroupLayout() throws Exception {
        QuestBoardConfiguration board = board(validYaml());
        board.validateGroups(List.of(group("daily"), group("weekly")));

        assertFalse(board.isEnabled());
    }

    @Test
    void rejectsAssignmentOverflowAndAssignmentsWithoutALayout() throws Exception {
        QuestBoardConfiguration board = board(validYaml());
        QuestPlayer player = new QuestPlayer(UUID.randomUUID(), "Player");
        player.addActiveQuests(List.of(assignment("daily", "a"), assignment("daily", "b"), assignment("daily", "c"), assignment("daily", "d")));

        assertEquals("daily", board.findOverflow(player).orElseThrow());

        QuestPlayer stalePlayer = new QuestPlayer(UUID.randomUUID(), "Stale");
        stalePlayer.addActiveQuest(assignment("removed-group", "old"));
        assertEquals("removed-group", board.findOverflow(stalePlayer).orElseThrow());
    }

    @Test
    void resolvesEveryAssignmentStatus() throws Exception {
        QuestBoardConfiguration board = board(validYaml());
        ActiveQuest waiting = assignment("daily", "waiting");
        ActiveQuest started = assignment("daily", "started");
        ActiveQuest completed = assignment("daily", "completed");
        when(started.isStarted()).thenReturn(true);
        when(completed.isDone()).thenReturn(true);

        assertEquals("&eWaiting", board.getStatus(waiting, true));
        assertEquals("&aIn progress", board.getStatus(started, true));
        assertEquals("&aCompleted", board.getStatus(completed, true));
        assertEquals("&cUnavailable", board.getStatus(waiting, false));
    }

    private static QuestBoardConfiguration board(YamlConfiguration yaml) {
        return new QuestBoardConfiguration(yaml.getConfigurationSection("quest-board"));
    }

    private static QuestGroup group(String id) {
        QuestGroup group = mock(QuestGroup.class);
        when(group.getGroupId()).thenReturn(id);
        return group;
    }

    private static ActiveQuest assignment(String groupId, String questId) {
        ActiveQuest assignment = mock(ActiveQuest.class);
        when(assignment.getGroupId()).thenReturn(groupId);
        when(assignment.getQuestId()).thenReturn(questId);
        return assignment;
    }

    private static YamlConfiguration validYaml() throws Exception {
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.loadFromString("""
                quest-board:
                  title: '&6Quest Board'
                  rows: 6
                  filler-item:
                    material: BLACK_STAINED_GLASS_PANE
                    name: ' '
                  status:
                    waiting: '&eWaiting'
                    started: '&aIn progress'
                    completed: '&aCompleted'
                    unavailable: '&cUnavailable'
                  groups:
                    daily:
                      slots: [10, 11, 12]
                      quest-item:
                        material: PAPER
                        name: '%quest_name%'
                        lore: ['%quest_description%', '%task_descriptions%', '%quest_status%']
                      empty-item:
                        material: BARRIER
                        name: 'Empty'
                """);
        return yaml;
    }
}
