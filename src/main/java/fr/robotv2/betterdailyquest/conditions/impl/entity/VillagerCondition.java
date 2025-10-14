package fr.robotv2.betterdailyquest.conditions.impl.entity;

import fr.robotv2.betterdailyquest.conditions.AbstractCondition;
import fr.robotv2.betterdailyquest.conditions.Condition;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import fr.robotv2.betterdailyquest.quest.context.entity.EntityContext;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class VillagerCondition extends AbstractCondition {

    private final Set<Villager.Profession> professions = new HashSet<>();
    private final Set<Villager.Type> types = new HashSet<>();
    private final int requiredLevel;

    public VillagerCondition(String key, ConfigurationSection parent) {
        super(key, parent);
        final ConfigurationSection child = Objects.requireNonNull(parent.getConfigurationSection(key));
        for(String professionString : child.getStringList("required_professions")) {
            final Villager.Profession profession = Villager.Profession.valueOf(professionString);
            professions.add(profession);
        }

        for(String typeString : child.getStringList("required_types")) {
            final Villager.Type type = Villager.Type.valueOf(typeString);
            types.add(type);
        }

        this.requiredLevel = child.getInt("required_level", Integer.MIN_VALUE);
    }

    @Override
    public boolean isMet(RunningQuestContext<?, ?> context) {
        if(context instanceof EntityContext<?> entityContext) {
            final Entity entity = entityContext.getEntity();

            if(!(entity instanceof Villager villager)) {
                return true;
            }

            if(!professions.isEmpty() && !professions.contains(villager.getProfession())) {
                return false;
            }

            if(!types.isEmpty() && !types.contains(villager.getVillagerType())) {
                return false;
            }

            if(requiredLevel != Integer.MIN_VALUE && villager.getVillagerLevel() < requiredLevel) {
                return false;
            }

            return true;
        }

        return true;
    }
}
