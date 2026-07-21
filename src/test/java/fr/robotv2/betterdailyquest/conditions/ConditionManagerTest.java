package fr.robotv2.betterdailyquest.conditions;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ConditionManagerTest {

    @Test
    void invalidConditionConfigurationIsPropagated() {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("placeholders.balance.placeholder", "%balance%");
        configuration.set("placeholders.balance.match", "10");
        configuration.set("placeholders.balance.comparator", "UNKNOWN");

        ConditionManager manager = new ConditionManager(null);

        assertThrows(IllegalArgumentException.class,
                () -> manager.toInstance(configuration, "placeholders"));
    }
}
