package fr.robotv2.betterdailyquest.command;

import fr.robotv2.betterdailyquest.configurations.messages.MessageConfiguration;
import fr.robotv2.betterdailyquest.event.QuestStartEvent;
import fr.robotv2.betterdailyquest.quest.Quest;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BetterDailyQuestCommandTest {

    @Mock
    private DatabaseManager databaseManager;

    @Mock
    private ColorProvider colorProvider;

    @Mock
    private CommandSender sender;

    @Mock
    private Player target;

    @Mock
    private QuestPlayer questPlayer;

    @Mock
    private ActiveQuest activeQuest;

    @Mock
    private Quest quest;

    @Mock
    private Consumer<Event> eventDispatcher;

    private MessageConfiguration.CommandMessages messages;

    @BeforeEach
    void setUp() {
        messages = new MessageConfiguration(new YamlConfiguration()).getCommandMessages();
        when(colorProvider.colorize(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(target.getName()).thenReturn("ExamplePlayer");
        when(databaseManager.getCachedQuestPlayer(target)).thenReturn(questPlayer);
    }

    @Test
    void startTransitionsWaitingAssignmentAndFiresEventOnce() {
        when(questPlayer.getActiveQuest("stonebreaker")).thenReturn(activeQuest);
        when(activeQuest.getQuest()).thenReturn(quest);
        when(activeQuest.getQuestId()).thenReturn("stonebreaker");

        start("stonebreaker", false);

        verify(activeQuest).setStarted(true);
        verify(eventDispatcher, times(1)).accept(any(QuestStartEvent.class));
    }

    @Test
    void startEventContainsAssignmentQuestAndPlayer() {
        when(questPlayer.getActiveQuest("stonebreaker")).thenReturn(activeQuest);
        when(activeQuest.getQuest()).thenReturn(quest);
        when(activeQuest.getQuestId()).thenReturn("stonebreaker");

        start("stonebreaker", false);

        org.mockito.ArgumentCaptor<Event> eventCaptor = org.mockito.ArgumentCaptor.forClass(Event.class);
        verify(eventDispatcher).accept(eventCaptor.capture());
        QuestStartEvent event = (QuestStartEvent) eventCaptor.getValue();
        assertSame(quest, event.getQuest());
        assertSame(activeQuest, event.getActiveQuest());
        assertSame(target, event.getPlayer());
    }

    @Test
    void missingAssignmentDoesNotMutateOrFireEvent() {
        when(questPlayer.getActiveQuest("missing")).thenReturn(null);

        start("missing", false);

        verify(activeQuest, never()).setStarted(true);
        verify(eventDispatcher, never()).accept(any());
    }

    @Test
    void unloadedPlayerDoesNotMutateOrFireEvent() {
        when(databaseManager.getCachedQuestPlayer(target)).thenReturn(null);

        start("stonebreaker", false);

        verify(activeQuest, never()).setStarted(true);
        verify(eventDispatcher, never()).accept(any());
    }

    @Test
    void completedAssignmentDoesNotMutateOrFireEvent() {
        when(questPlayer.getActiveQuest("stonebreaker")).thenReturn(activeQuest);
        when(activeQuest.isDone()).thenReturn(true);

        assertStartRejected();
    }

    @Test
    void alreadyStartedAssignmentDoesNotMutateOrFireEvent() {
        when(questPlayer.getActiveQuest("stonebreaker")).thenReturn(activeQuest);
        when(activeQuest.isStarted()).thenReturn(true);

        assertStartRejected();
    }

    @Test
    void unavailableAssignmentDoesNotMutateOrFireEvent() {
        when(questPlayer.getActiveQuest("stonebreaker")).thenReturn(activeQuest);
        when(activeQuest.getQuest()).thenReturn(null);
        when(activeQuest.getQuestId()).thenReturn("stonebreaker");

        assertStartRejected();
    }

    @Test
    void startOthersUsesTheSelectedTarget() {
        when(questPlayer.getActiveQuest("stonebreaker")).thenReturn(activeQuest);
        when(activeQuest.getQuest()).thenReturn(quest);
        when(activeQuest.getQuestId()).thenReturn("stonebreaker");

        start("stonebreaker", true);

        verify(databaseManager).getCachedQuestPlayer(target);
        verify(activeQuest).setStarted(true);
    }

    private void assertStartRejected() {
        start("stonebreaker", false);

        verify(activeQuest, never()).setStarted(true);
        verify(eventDispatcher, never()).accept(any());
    }

    private void start(String questId, boolean isOthers) {
        BetterDailyQuestCommand.handleStart(
                sender,
                target,
                questId,
                isOthers,
                databaseManager,
                messages,
                colorProvider,
                eventDispatcher
        );
    }
}
