package fr.robotv2.betterdailyquest.conditions.impl.player;

import fr.robotv2.betterdailyquest.conditions.AbstractCondition;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.Set;

public class WorldCondition extends AbstractCondition {

    private final Set<String> worlds;

    public WorldCondition(String key, ConfigurationSection parent) {
        super(key, parent);
        this.worlds = new HashSet<>(parent.getStringList(key));
    }

    @Override
    public boolean isMet(RunningQuestContext<?, ?> context) {
        final String worldName = context.getInitiator().getWorld().getName();
        return worlds.contains(worldName);
    }
}
