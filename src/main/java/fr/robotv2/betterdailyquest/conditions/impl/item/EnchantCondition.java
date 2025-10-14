package fr.robotv2.betterdailyquest.conditions.impl.item;

import fr.robotv2.betterdailyquest.conditions.AbstractCondition;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import fr.robotv2.betterdailyquest.quest.context.item.ItemContext;
import fr.robotv2.betterdailyquest.quest.type.QuestTypes;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EnchantCondition extends AbstractCondition {

    private final int requiredLevel;
    private final Set<Enchantment> enchants = new HashSet<>();

    public EnchantCondition(String key, ConfigurationSection parent) {
        super(key, parent);
        this.requiredLevel = parent.getInt(key + ".required_level", Integer.MIN_VALUE);
        for(String enchantString : parent.getStringList(key + ".required_types")) {
            final Enchantment enchantment = Enchantment.getByName(enchantString);
            assert enchantment != null : "Enchantment " + enchantString + " is not a valid key.";
            enchants.add(enchantment);
        }
    }

    @Override
    public boolean isMet(RunningQuestContext<?, ?> context) {
        if(context.getType() == QuestTypes.ENCHANT_TYPE
                && context.getTriggeredEvent() instanceof EnchantItemEvent enchantItemEvent) {
            return checkEnchantment(enchantItemEvent.getEnchantsToAdd().entrySet());
        }

        if(context instanceof ItemContext<?> itemContext) {
            return checkEnchantment(itemContext.getItem().getEnchantments().entrySet());
        }

        return true;
    }

    private boolean checkEnchantment(Set<Map.Entry<Enchantment, Integer>> entries) {
        for (Map.Entry<Enchantment, Integer> entry : entries) {
            if (isEnchantPresent(entry.getKey()) && isLevelSufficient(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    private boolean isEnchantPresent(Enchantment enchantment) {
        return enchants.isEmpty() || enchants.contains(enchantment);
    }

    private boolean isLevelSufficient(int level) {
        return requiredLevel == Integer.MIN_VALUE || level >= requiredLevel;
    }
}
