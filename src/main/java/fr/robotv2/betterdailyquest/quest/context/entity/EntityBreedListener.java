package fr.robotv2.betterdailyquest.quest.context.entity;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.quest.context.QuestProgressionEnhancer;
import fr.robotv2.betterdailyquest.quest.type.QuestTypes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityBreedEvent;

public class EntityBreedListener extends QuestProgressionEnhancer {

    public EntityBreedListener(BetterDailyQuest plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityBreed(final EntityBreedEvent event) {
        if(!(event.getBreeder() instanceof Player)) return;
        updateQuestProgress(new EntityContext<>(
                (Player) event.getBreeder(),
                QuestTypes.BREED_TYPE,
                event,
                event.getEntity()
        ));
    }
}
