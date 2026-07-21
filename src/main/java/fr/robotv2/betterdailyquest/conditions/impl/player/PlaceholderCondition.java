package fr.robotv2.betterdailyquest.conditions.impl.player;

import fr.robotv2.betterdailyquest.conditions.AbstractCondition;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import fr.robotv2.betterdailyquest.util.NumberUtil;
import fr.robotv2.betterdailyquest.util.placeholder.Placeholders;
import org.bukkit.configuration.ConfigurationSection;

import java.math.BigDecimal;
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
            if(placeholderSection == null) {
                throw new IllegalArgumentException("Placeholder condition '" + placeholderKey + "' must be a section.");
            }
            final PlaceholderConditionRecord record = new PlaceholderConditionRecord(placeholderSection);
            placeholderConditions.add(record);
        }
    }

    @Override
    public boolean isMet(RunningQuestContext<?, ?> context) {

        for(PlaceholderConditionRecord record : placeholderConditions) {

            final String placeholder = Placeholders.safePlaceholderAPI(context.getInitiator(), record.placeholder);
            if(!record.matches(placeholder)) {
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

        MORE((playerValue, matchValue) -> playerValue.compareTo(matchValue) > 0),
        MORE_EQUAL((playerValue, matchValue) -> playerValue.compareTo(matchValue) >= 0),
        EQUAL((playerValue, matchValue) -> playerValue.compareTo(matchValue) == 0),
        LESS_EQUAL((playerValue, matchValue) -> playerValue.compareTo(matchValue) <= 0),
        LESS((playerValue, matchValue) -> playerValue.compareTo(matchValue) < 0),
        ;

        private final BiFunction<BigDecimal, BigDecimal, Boolean> function;
        private final static PlaceholderValueComparator[] VALUES = values();

        PlaceholderValueComparator(BiFunction<BigDecimal, BigDecimal, Boolean> function) {
            this.function = function;
        }

        private static PlaceholderValueComparator fromName(String value) {
            return Arrays.stream(VALUES)
                    .filter(valueComparator -> valueComparator.name().equalsIgnoreCase(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown placeholder comparator '" + value + "'."));
        }
    }

    private final static class PlaceholderConditionRecord {

        private final String placeholder;
        private final PlaceholderValueComparator comparator;
        private final String match;
        private final BigDecimal matchValue;

        private final PlaceholderValueType type;

        private PlaceholderConditionRecord(ConfigurationSection child) {
            this(
                    requireValue(child, "placeholder", false),
                    PlaceholderValueComparator.fromName(child.getString("comparator", PlaceholderValueComparator.EQUAL.name())),
                    requireValue(child, "match", true)
            );
        }

        private PlaceholderConditionRecord(String placeholder, PlaceholderValueComparator comparator, String match) {
            this.placeholder = placeholder;
            this.comparator = comparator;
            this.match = match;
            this.type = NumberUtil.isNumber(match) ? PlaceholderValueType.NUMERICAL : PlaceholderValueType.STRING;
            if(type == PlaceholderValueType.STRING && comparator != PlaceholderValueComparator.EQUAL) {
                throw new IllegalArgumentException("Placeholder comparator '" + comparator + "' requires a numeric match value.");
            }
            this.matchValue = type == PlaceholderValueType.NUMERICAL ? new BigDecimal(match) : null;
        }

        private boolean matches(String value) {
            if(type == PlaceholderValueType.STRING) {
                return match.equals(value);
            }
            return NumberUtil.isNumber(value) && comparator.function.apply(new BigDecimal(value), matchValue);
        }

        private static String requireValue(ConfigurationSection section, String key, boolean allowEmpty) {
            final String value = section.getString(key);
            if(value == null || (!allowEmpty && value.isBlank())) {
                throw new IllegalArgumentException("Missing placeholder condition value '" + key + "'.");
            }
            return value;
        }
    }
}
