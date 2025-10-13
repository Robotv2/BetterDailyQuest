package fr.robotv2.betterdailyquest.quest.context.entity;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.quest.context.QuestProgressionEnhancer;
import fr.robotv2.betterdailyquest.quest.type.QuestTypes;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityKillListener extends QuestProgressionEnhancer {

    public EntityKillListener(BetterDailyQuest plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityKill(final EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();
        final Player killer = entity.getKiller();
        if(killer == null) return;
        updateQuestProgress(new EntityContext<>(
                killer,
                QuestTypes.KILL_TYPE,
                event,
                entity
        ));
    }
}
