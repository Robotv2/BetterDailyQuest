package fr.robotv2.betterdailyquest.quest.context.player;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.event.PlayerWalkEvent;
import fr.robotv2.betterdailyquest.quest.context.QuestProgressionEnhancer;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import fr.robotv2.betterdailyquest.quest.type.QuestTypes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class PlayerWalkListener extends QuestProgressionEnhancer {

    public PlayerWalkListener(BetterDailyQuest plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWalk(PlayerWalkEvent event) {
        updateQuestProgress(new RunningQuestContext<Void, PlayerWalkEvent>(
                event.getPlayer(), QuestTypes.WALK_TYPE, event, null, event.getDiff() / 100D
        ) {});
    }
}
