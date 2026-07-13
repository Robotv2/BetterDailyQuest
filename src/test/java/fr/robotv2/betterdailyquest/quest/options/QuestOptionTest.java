package fr.robotv2.betterdailyquest.quest.options;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestOptionTest {

    @Test
    void sequentialTasksSetsDependentTaskOption() {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("sequential-tasks", true);

        QuestOption option = new QuestOption(configuration);

        assertTrue(option.getOptionValue(Optionnable.Option.DEPENDANT_TASK));
    }

    @Test
    void legacyDependantTasksIsIgnored() {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("dependant-tasks", true);

        QuestOption option = new QuestOption(configuration);

        assertFalse(option.isOptionSet(Optionnable.Option.DEPENDANT_TASK));
        assertFalse(option.getOptionValue(Optionnable.Option.DEPENDANT_TASK));
    }
}
