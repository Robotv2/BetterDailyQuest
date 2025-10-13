package fr.robotv2.betterdailyquest.quest.context.block;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.quest.context.QuestProgressionEnhancer;
import fr.robotv2.betterdailyquest.quest.type.QuestTypes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener extends QuestProgressionEnhancer {

    public BlockBreakListener(BetterDailyQuest plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {
        updateQuestProgress(new BlockContext<>(event.getPlayer(), QuestTypes.BREAK_TYPE, event, event.getBlock()));
    }
}
