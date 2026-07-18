package fr.robotv2.betterdailyquest.event;

import fr.robotv2.betterdailyquest.quest.Quest;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class QuestStartEvent extends QuestEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ActiveQuest activeQuest;
    private final Player player;

    public QuestStartEvent(Quest quest, ActiveQuest activeQuest, Player player) {
        super(quest);
        this.activeQuest = activeQuest;
        this.player = player;
    }

    public ActiveQuest getActiveQuest() {
        return activeQuest;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
