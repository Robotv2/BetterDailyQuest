package fr.robotv2.betterdailyquest.quest;

import fr.robotv2.betterdailyquest.configurations.messages.MessageConfiguration;
import fr.robotv2.betterdailyquest.event.QuestStartEvent;
import fr.robotv2.betterdailyquest.storage.DatabaseManager;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import fr.robotv2.betterdailyquest.storage.model.QuestPlayer;
import fr.robotv2.betterdailyquest.util.color.ColorProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestAssignmentStarterTest {

    @Mock private DatabaseManager databaseManager;
    @Mock private ColorProvider colorProvider;
    @Mock private CommandSender sender;
    @Mock private Player target;
    @Mock private QuestPlayer questPlayer;
    @Mock private ActiveQuest activeQuest;
    @Mock private Quest quest;
    @Mock private Consumer<Event> eventDispatcher;

    private QuestAssignmentStarter starter;

    @BeforeEach
    void setUp() {
        MessageConfiguration.CommandMessages messages = new MessageConfiguration(new YamlConfiguration()).getCommandMessages();
        starter = new QuestAssignmentStarter(databaseManager, messages, colorProvider, eventDispatcher);
        when(colorProvider.colorize(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(target.getName()).thenReturn("ExamplePlayer");
        when(databaseManager.getCachedQuestPlayer(target)).thenReturn(questPlayer);
    }

    @Test
    void waitingAssignmentTransitionsOnlyOnceAndFiresOneEvent() {
        when(questPlayer.getActiveQuest("stonebreaker")).thenReturn(activeQuest);
        when(activeQuest.getQuest()).thenReturn(quest);
        when(activeQuest.getQuestId()).thenReturn("stonebreaker");
        when(activeQuest.isStarted()).thenReturn(false, true);

        assertTrue(start("stonebreaker"));
        assertFalse(start("stonebreaker"));

        verify(activeQuest, times(1)).setStarted(true);
        verify(eventDispatcher, times(1)).accept(any(QuestStartEvent.class));
    }

    @Test
    void eventContainsAssignmentQuestAndPlayer() {
        when(questPlayer.getActiveQuest("stonebreaker")).thenReturn(activeQuest);
        when(activeQuest.getQuest()).thenReturn(quest);
        when(activeQuest.getQuestId()).thenReturn("stonebreaker");

        assertTrue(start("stonebreaker"));

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventDispatcher).accept(eventCaptor.capture());
        QuestStartEvent event = (QuestStartEvent) eventCaptor.getValue();
        assertSame(quest, event.getQuest());
        assertSame(activeQuest, event.getActiveQuest());
        assertSame(target, event.getPlayer());
    }

    @Test
    void unloadedPlayerIsRejectedWithoutMutation() {
        when(databaseManager.getCachedQuestPlayer(target)).thenReturn(null);
        assertRejected("stonebreaker");
    }

    @Test
    void missingAssignmentIsRejectedWithoutMutation() {
        when(questPlayer.getActiveQuest("missing")).thenReturn(null);
        assertRejected("missing");
    }

    @Test
    void completedAssignmentIsRejectedWithoutMutation() {
        when(questPlayer.getActiveQuest("stonebreaker")).thenReturn(activeQuest);
        when(activeQuest.isDone()).thenReturn(true);
        assertRejected("stonebreaker");
    }

    @Test
    void startedAssignmentIsRejectedWithoutMutation() {
        when(questPlayer.getActiveQuest("stonebreaker")).thenReturn(activeQuest);
        when(activeQuest.isStarted()).thenReturn(true);
        assertRejected("stonebreaker");
    }

    @Test
    void unavailableAssignmentIsRejectedWithoutMutation() {
        when(questPlayer.getActiveQuest("stonebreaker")).thenReturn(activeQuest);
        when(activeQuest.getQuest()).thenReturn(null);
        when(activeQuest.getQuestId()).thenReturn("stonebreaker");
        assertRejected("stonebreaker");
    }

    private void assertRejected(String questId) {
        assertFalse(start(questId));
        verify(activeQuest, never()).setStarted(true);
        verify(eventDispatcher, never()).accept(any());
    }

    private boolean start(String questId) {
        return starter.start(sender, target, questId, false);
    }
}
