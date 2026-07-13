package fr.robotv2.betterdailyquest.quest.options;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class QuestOption implements Optionnable {

    private static final String SEQUENTIAL_TASKS_KEY = "sequential-tasks";
    private static final String AUTOMATICALLY_GIVEN_KEY = "automatically-given";
    private static final String NEED_STARTING_KEY = "need-starting";
    private static final String REPEATABLE_KEY = "repeatable";

    private final Map<Option, Boolean> options = new EnumMap<>(Option.class);

    public QuestOption(final @Nullable ConfigurationSection section) {
        if(section == null) {
            return;
        }

        if(section.isBoolean(SEQUENTIAL_TASKS_KEY)) {
            options.put(Option.DEPENDANT_TASK, section.getBoolean(SEQUENTIAL_TASKS_KEY));
        }

        if(section.isBoolean(AUTOMATICALLY_GIVEN_KEY)) {
            options.put(Option.AUTOMATICALLY_GIVEN, section.getBoolean(AUTOMATICALLY_GIVEN_KEY));
        }

        if(section.isBoolean(NEED_STARTING_KEY)) {
            options.put(Option.NEED_STARTING, section.getBoolean(NEED_STARTING_KEY));
        }

        if(section.isBoolean(REPEATABLE_KEY)) {
            options.put(Option.REPEATABLE, section.getBoolean(REPEATABLE_KEY));
        }
    }

    @Override
    public boolean isOptionSet(Option option) {
        return options.containsKey(option);
    }

    @Override
    public boolean getOptionValue(Option option) {
        return isOptionSet(option) ? options.get(option) : option.getDefaultValue();
    }
}
