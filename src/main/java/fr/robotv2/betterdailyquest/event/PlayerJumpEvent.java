package fr.robotv2.betterdailyquest.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJumpEvent extends PlayerEvent {

    public static final HandlerList HANDLER_LIST = new HandlerList();

    private final int beforeValue;
    private final int newValue;

    public PlayerJumpEvent(@NotNull Player player, int beforeValue, int newValue) {
        super(player);
        this.beforeValue = beforeValue;
        this.newValue = newValue;
    }

    public int getDiff() {
        return newValue - beforeValue;
    }

    public int getBeforeValue() {
        return beforeValue;
    }

    public int getNewValue() {
        return newValue;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
