package fr.robotv2.betterdailyquest.quest.context.player;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.event.PlayerMinecartEvent;
import fr.robotv2.betterdailyquest.quest.context.QuestProgressionEnhancer;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import fr.robotv2.betterdailyquest.quest.type.QuestTypes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class PlayerMinecartListener extends QuestProgressionEnhancer {

    public PlayerMinecartListener(BetterDailyQuest plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMinecart(PlayerMinecartEvent event) {
        updateQuestProgress(new RunningQuestContext<Void, PlayerMinecartEvent>(
                event.getPlayer(), QuestTypes.MINECART_TYPE, event, null, event.getDiff() / 100D
        ) {});
    }
}
