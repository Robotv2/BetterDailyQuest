package fr.robotv2.betterdailyquest.quest.context.player;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.event.PlayerSwimEvent;
import fr.robotv2.betterdailyquest.quest.context.QuestProgressionEnhancer;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import fr.robotv2.betterdailyquest.quest.type.QuestTypes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class PlayerSwimListener extends QuestProgressionEnhancer {

    public PlayerSwimListener(BetterDailyQuest plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSwim(PlayerSwimEvent event) {
        updateQuestProgress(new RunningQuestContext<Void, PlayerSwimEvent>(
                event.getPlayer(), QuestTypes.SWIM_TYPE, event, null, event.getDiff() / 100D
        ) {});
    }
}
