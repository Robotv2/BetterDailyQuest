package fr.robotv2.betterdailyquest.quest.context.entity;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.quest.context.QuestProgressionEnhancer;
import fr.robotv2.betterdailyquest.quest.type.QuestTypes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTameEvent;

public class EntityTameListener extends QuestProgressionEnhancer {

    public EntityTameListener(BetterDailyQuest plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityTame(final EntityTameEvent event) {
        if(!(event.getOwner() instanceof Player)) return;
        updateQuestProgress(new EntityContext<>(
                (Player) event.getOwner(),
                QuestTypes.TAME_TYPE,
                event,
                event.getEntity()
        ));
    }
}
