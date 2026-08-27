package fr.robotv2.betterdailyquest.conditions.impl.player;

import fr.robotv2.betterdailyquest.conditions.AbstractCondition;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import org.bukkit.configuration.ConfigurationSection;

public class HeightCondition extends AbstractCondition {

    private final Integer minimum;
    private final Integer maximum;

    public HeightCondition(String key, ConfigurationSection parent) {
        super(key, parent);
        final ConfigurationSection child = parent.getConfigurationSection(key);
        if(child == null) {
            throw new IllegalArgumentException("Height condition must be a section.");
        }
        if(!child.contains("minimum") && !child.contains("maximum")) {
            throw new IllegalArgumentException("Height condition requires a minimum or maximum.");
        }
        if(child.contains("minimum") && !child.isInt("minimum")) {
            throw new IllegalArgumentException("Height condition minimum must be an integer.");
        }
        if(child.contains("maximum") && !child.isInt("maximum")) {
            throw new IllegalArgumentException("Height condition maximum must be an integer.");
        }

        this.minimum = child.isInt("minimum") ? child.getInt("minimum") : null;
        this.maximum = child.isInt("maximum") ? child.getInt("maximum") : null;
        if(minimum != null && maximum != null && minimum > maximum) {
            throw new IllegalArgumentException("Height condition minimum cannot be greater than maximum.");
        }
    }

    @Override
    public boolean isMet(RunningQuestContext<?, ?> context) {
        final int height = context.getInitiator().getLocation().getBlockY();
        return (minimum == null || height >= minimum) && (maximum == null || height <= maximum);
    }
}
