package fr.robotv2.betterdailyquest.conditions.impl.player;

import fr.robotv2.betterdailyquest.conditions.AbstractCondition;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import fr.robotv2.betterdailyquest.util.NumberUtil;
import fr.robotv2.betterdailyquest.util.placeholder.Placeholders;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;


public class PlaceholderCondition extends AbstractCondition {

    private final List<PlaceholderConditionRecord> placeholderConditions = new ArrayList<>();

    public PlaceholderCondition(String key, ConfigurationSection parent) {
        super(key, parent);
        final ConfigurationSection child = Objects.requireNonNull(parent.getConfigurationSection(key));
        for(String placeholderKey : child.getKeys(false)) {
            final ConfigurationSection placeholderSection = child.getConfigurationSection(placeholderKey);
            if(placeholderSection == null) continue;
            final PlaceholderConditionRecord record = new PlaceholderConditionRecord(placeholderSection);
            placeholderConditions.add(record);
        }
    }

    @Override
    public boolean isMet(RunningQuestContext<?, ?> context) {

        for(PlaceholderConditionRecord record : placeholderConditions) {

            final String placeholder = Placeholders.safePlaceholderAPI(context.getInitiator(), record.placeholder);

            if(record.type == PlaceholderValueType.NUMERICAL && NumberUtil.isNumber(placeholder)) {
                final PlaceholderValueComparator comparator = record.comparator == null ? PlaceholderValueComparator.EQUAL : record.comparator;
                final double playerValue = NumberUtil.toNumber(placeholder).doubleValue();
                if(!comparator.function.apply(playerValue, record.matchValue)) {
                    return false;
                }
            }

            if(!record.match.equals(placeholder)) {
                return false;
            }
        }

        return true;
    }

    private enum PlaceholderValueType {
        NUMERICAL,
        STRING,
        ;
    }

    private enum PlaceholderValueComparator {

        MORE((playerValue, matchValue) -> playerValue > matchValue),
        MORE_EQUAL((playerValue, matchValue) -> playerValue >= matchValue),
        EQUAL(Objects::equals),
        LESS_EQUAL((playerValue, matchValue) -> playerValue <= matchValue),
        LESS(((playerValue, matchValue) -> playerValue < matchValue)),
        ;

        private final BiFunction<Double, Double, Boolean> function;
        private final static PlaceholderValueComparator[] VALUES = values();

        PlaceholderValueComparator(BiFunction<Double, Double, Boolean> function) {
            this.function = function;
        }

        private static PlaceholderValueComparator fromName(String value) {
            return Arrays.stream(VALUES).filter(valueComparator -> valueComparator.name().equalsIgnoreCase(value)).findFirst().orElse(null);
        }
    }

    private final static class PlaceholderConditionRecord {

        private final String placeholder;
        private final PlaceholderValueComparator comparator;
        private final String match;
        private final double matchValue;

        private final PlaceholderValueType type;

        private PlaceholderConditionRecord(ConfigurationSection child) {
            this(
                    child.getString("placeholder"),
                    PlaceholderValueComparator.fromName(child.getString("comparator")),
                    child.getString("match")
            );
        }

        private PlaceholderConditionRecord(String placeholder, PlaceholderValueComparator comparator, String match) {
            this.placeholder = placeholder;
            this.comparator = comparator;
            this.match = match;
            this.type = NumberUtil.isNumber(match) ? PlaceholderValueType.NUMERICAL : PlaceholderValueType.STRING;
            this.matchValue = type == PlaceholderValueType.NUMERICAL ? NumberUtil.toNumber(match).doubleValue() : 0;
        }
    }
}
