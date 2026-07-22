package fr.robotv2.betterdailyquest.questboard;

import fr.robotv2.betterdailyquest.configurations.messages.MessageConfiguration;
import fr.robotv2.betterdailyquest.quest.QuestAssignmentStarter;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import fr.robotv2.betterdailyquest.util.color.ColorProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestBoardListenerTest {

    @Mock private MessageConfiguration.CommandMessages messages;
    @Mock private ColorProvider colorProvider;
    @Mock private QuestAssignmentStarter starter;
    @Mock private QuestBoard board;
    @Mock private Inventory topInventory;
    @Mock private Player player;
    @Mock private ActiveQuest assignment;
    @Mock private InventoryClickEvent click;
    @Mock private InventoryDragEvent drag;

    private QuestBoardListener listener;

    @BeforeEach
    void setUp() {
        listener = new QuestBoardListener(starter, () -> messages, colorProvider);
        lenient().when(topInventory.getHolder()).thenReturn(board);
        lenient().when(board.getInventory()).thenReturn(topInventory);
        lenient().when(topInventory.getSize()).thenReturn(54);
        lenient().when(board.getPlayer()).thenReturn(player);
        lenient().when(board.getQuestId(10)).thenReturn("stonebreaker");
        lenient().when(board.getAssignment("stonebreaker")).thenReturn(assignment);
        lenient().when(board.isAvailable(assignment)).thenReturn(true);
        lenient().when(messages.getQuestBoardStartDenied()).thenReturn("Denied");
        lenient().when(colorProvider.colorize(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(click.getInventory()).thenReturn(topInventory);
        lenient().when(click.getRawSlot()).thenReturn(10);
        lenient().when(click.getClick()).thenReturn(ClickType.LEFT);
        lenient().when(drag.getInventory()).thenReturn(topInventory);
    }

    @Test
    void cancelsEveryClickAndDragWhileBoardIsOpen() {
        when(click.getClick()).thenReturn(ClickType.NUMBER_KEY);

        listener.onClick(click);
        listener.onDrag(drag);

        verify(click).setCancelled(true);
        verify(drag).setCancelled(true);
        verify(click, never()).getView();
        verify(drag, never()).getView();
        verify(starter, never()).start(player, player, "stonebreaker", false);
    }

    @Test
    void deniedWaitingClickDoesNotStart() {
        when(player.hasPermission("betterdailyquest.command.start")).thenReturn(false);

        listener.onClick(click);

        verify(click).setCancelled(true);
        verify(player).sendMessage("Denied");
        verify(starter, never()).start(player, player, "stonebreaker", false);
        verify(board, never()).refresh();
    }

    @Test
    void startedAssignmentIsDisplayOnly() {
        when(assignment.isStarted()).thenReturn(true);

        listener.onClick(click);

        verify(starter, never()).start(player, player, "stonebreaker", false);
        verify(board, never()).refresh();
    }

    @Test
    void completedAssignmentIsDisplayOnly() {
        when(assignment.isDone()).thenReturn(true);

        listener.onClick(click);

        verify(starter, never()).start(player, player, "stonebreaker", false);
        verify(board, never()).refresh();
    }

    @Test
    void staleAndUnavailableAssignmentsDoNothing() {
        when(board.getAssignment("stonebreaker")).thenReturn(null);
        listener.onClick(click);
        verify(starter, never()).start(player, player, "stonebreaker", false);

        when(board.getAssignment("stonebreaker")).thenReturn(assignment);
        when(board.isAvailable(assignment)).thenReturn(false);
        listener.onClick(click);
        verify(starter, never()).start(player, player, "stonebreaker", false);
    }

    @Test
    void successfulStartRefreshesTheExistingBoard() {
        when(player.hasPermission("betterdailyquest.command.start")).thenReturn(true);
        when(starter.start(player, player, "stonebreaker", false)).thenReturn(true);

        listener.onClick(click);

        verify(starter).start(player, player, "stonebreaker", false);
        verify(board).refresh();
    }

    @Test
    void rejectedStartDoesNotRefresh() {
        when(player.hasPermission("betterdailyquest.command.start")).thenReturn(true);
        when(starter.start(player, player, "stonebreaker", false)).thenReturn(false);

        listener.onClick(click);

        verify(board, never()).refresh();
    }
}
