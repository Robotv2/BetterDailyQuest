package fr.robotv2.betterdailyquest.quest.context.player;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.event.PlayerJumpEvent;
import fr.robotv2.betterdailyquest.quest.context.QuestProgressionEnhancer;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import fr.robotv2.betterdailyquest.quest.type.QuestTypes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class PlayerJumpListener extends QuestProgressionEnhancer {

    public PlayerJumpListener(BetterDailyQuest plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJump(PlayerJumpEvent event) {
        updateQuestProgress(new RunningQuestContext<Void, PlayerJumpEvent>(
                event.getPlayer(), QuestTypes.JUMP_TYPE, event, null, event.getDiff()
        ) {});
    }
}
