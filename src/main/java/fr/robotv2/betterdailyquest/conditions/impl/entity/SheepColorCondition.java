package fr.robotv2.betterdailyquest.conditions.impl.entity;

import com.google.common.base.Enums;
import fr.robotv2.betterdailyquest.conditions.AbstractCondition;
import fr.robotv2.betterdailyquest.conditions.Condition;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import fr.robotv2.betterdailyquest.quest.context.entity.EntityContext;
import org.bukkit.DyeColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;

import java.util.Objects;

public class SheepColorCondition extends AbstractCondition {

    private final DyeColor color;

    public SheepColorCondition(String key, ConfigurationSection parent) {
        super(key, parent);
        final String dyeColorString = Objects.requireNonNull(parent.getString(key));
        this.color = Enums.getIfPresent(DyeColor.class, dyeColorString).orNull();
    }

    @Override
    public boolean isMet(RunningQuestContext<?, ?> context) {
        if(context instanceof EntityContext<?> entityContext) {
            final Entity entity = entityContext.getEntity();
            if(entity instanceof Sheep sheep && this.color != null) {
                return sheep.getColor() == this.color;
            }
        }

        return true;
    }
}
