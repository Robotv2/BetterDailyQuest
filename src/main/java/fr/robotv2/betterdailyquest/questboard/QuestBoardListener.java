package fr.robotv2.betterdailyquest.questboard;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.configurations.messages.MessageConfiguration;
import fr.robotv2.betterdailyquest.quest.QuestAssignmentStarter;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import fr.robotv2.betterdailyquest.util.color.ColorProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.function.Supplier;

public class QuestBoardListener implements Listener {

    private final QuestAssignmentStarter starter;
    private final Supplier<MessageConfiguration.CommandMessages> messages;
    private final ColorProvider colorProvider;

    public QuestBoardListener(BetterDailyQuest plugin) {
        this(plugin.getQuestAssignmentStarter(),
                () -> plugin.getQuestConfiguration().getMessageConfiguration().getCommandMessages(),
                plugin.getColorProvider());
    }

    QuestBoardListener(QuestAssignmentStarter starter, Supplier<MessageConfiguration.CommandMessages> messages,
                       ColorProvider colorProvider) {
        this.starter = starter;
        this.messages = messages;
        this.colorProvider = colorProvider;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        QuestBoard board = getBoard(event.getInventory());
        if(board == null) return;
        event.setCancelled(true);

        if(event.getClick() != ClickType.LEFT || event.getRawSlot() < 0 || event.getRawSlot() >= board.getInventory().getSize()) {
            return;
        }

        String questId = board.getQuestId(event.getRawSlot());
        if(questId == null) return;
        ActiveQuest assignment = board.getAssignment(questId);
        if(assignment == null || assignment.isStarted() || assignment.isDone() || !board.isAvailable(assignment)) {
            return;
        }

        if(!board.getPlayer().hasPermission("betterdailyquest.command.start")) {
            board.getPlayer().sendMessage(colorProvider.colorize(messages.get().getQuestBoardStartDenied()));
            return;
        }

        if(starter.start(board.getPlayer(), board.getPlayer(), questId, false)) {
            board.refresh();
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if(getBoard(event.getInventory()) != null) {
            event.setCancelled(true);
        }
    }

    private static QuestBoard getBoard(Inventory inventory) {
        return inventory.getHolder() instanceof QuestBoard board ? board : null;
    }
}
