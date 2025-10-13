package fr.robotv2.betterdailyquest.quest.context.player;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.event.PlayerMoveFullBlockEvent;
import fr.robotv2.betterdailyquest.quest.context.QuestProgressionEnhancer;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import fr.robotv2.betterdailyquest.quest.type.QuestTypes;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

public class PlayerLocationListener extends QuestProgressionEnhancer {

    public PlayerLocationListener(BetterDailyQuest plugin) {
        super(plugin);
    }

    public static final class LocationRunningContext extends RunningQuestContext<Location, PlayerMoveFullBlockEvent> {
        public LocationRunningContext(@NotNull Player initiator, @NotNull PlayerMoveFullBlockEvent triggered, @NotNull Location target) {
            super(initiator, QuestTypes.LOCATION_TYPE, triggered, target);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveFullBlockEvent event) {
        updateQuestProgress(new LocationRunningContext(event.getPlayer(), event, event.getTo()));
    }
}
