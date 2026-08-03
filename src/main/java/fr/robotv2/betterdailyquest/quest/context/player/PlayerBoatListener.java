package fr.robotv2.betterdailyquest.quest.context.player;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.event.PlayerBoatEvent;
import fr.robotv2.betterdailyquest.quest.context.QuestProgressionEnhancer;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import fr.robotv2.betterdailyquest.quest.type.QuestTypes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class PlayerBoatListener extends QuestProgressionEnhancer {

    public PlayerBoatListener(BetterDailyQuest plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBoat(PlayerBoatEvent event) {
        updateQuestProgress(new RunningQuestContext<Void, PlayerBoatEvent>(
                event.getPlayer(), QuestTypes.BOAT_TYPE, event, null, event.getDiff() / 100D
        ) {});
    }
}
