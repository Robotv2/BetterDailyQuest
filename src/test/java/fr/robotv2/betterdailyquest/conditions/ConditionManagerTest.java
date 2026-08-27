package fr.robotv2.betterdailyquest.conditions;

import fr.robotv2.betterdailyquest.conditions.impl.player.BiomeCondition;
import fr.robotv2.betterdailyquest.conditions.impl.player.GameModeCondition;
import fr.robotv2.betterdailyquest.conditions.impl.player.HeightCondition;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConditionManagerTest {

    @Test
    void registersPlayerContextConditions() throws ReflectiveOperationException {
        ConditionManager manager = new ConditionManager(null);
        Field field = ConditionManager.class.getDeclaredField("conditions");
        field.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, Class<? extends Condition>> conditions =
                (Map<String, Class<? extends Condition>>) field.get(manager);

        assertEquals(GameModeCondition.class, conditions.get("game_modes"));
        assertEquals(BiomeCondition.class, conditions.get("biomes"));
        assertEquals(HeightCondition.class, conditions.get("height"));
    }

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
