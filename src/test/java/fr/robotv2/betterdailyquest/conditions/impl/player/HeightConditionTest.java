package fr.robotv2.betterdailyquest.conditions.impl.player;

import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HeightConditionTest {

    @Mock
    private RunningQuestContext<?, ?> context;

    @Mock
    private Player player;

    @Mock
    private Location location;

    @Test
    void boundedRangeIncludesBothBoundaries() {
        HeightCondition condition = condition(-64, 32);

        givenPlayerHeight(-64);
        assertTrue(condition.isMet(context));
        givenPlayerHeight(32);
        assertTrue(condition.isMet(context));
    }

    @Test
    void boundedRangeRejectsValuesOutsideIt() {
        HeightCondition condition = condition(-64, 32);

        givenPlayerHeight(-65);
        assertFalse(condition.isMet(context));
        givenPlayerHeight(33);
        assertFalse(condition.isMet(context));
    }

    @Test
    void supportsSingleBoundary() {
        HeightCondition minimum = condition(64, null);
        HeightCondition maximum = condition(null, 32);

        givenPlayerHeight(64);
        assertTrue(minimum.isMet(context));
        givenPlayerHeight(63);
        assertFalse(minimum.isMet(context));
        givenPlayerHeight(32);
        assertTrue(maximum.isMet(context));
        givenPlayerHeight(33);
        assertFalse(maximum.isMet(context));
    }

    @Test
    void exposesConfiguredErrorMessage() {
        YamlConfiguration configuration = configuration(null, 32);
        configuration.set("height.error-message", "Go underground.");

        assertEquals("Go underground.", new HeightCondition("height", configuration).callback());
    }

    @Test
    void invalidConfigurationIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new HeightCondition("height", new YamlConfiguration()));
        assertThrows(IllegalArgumentException.class,
                () -> new HeightCondition("height", configuration(null, null)));
        assertThrows(IllegalArgumentException.class,
                () -> new HeightCondition("height", configuration("low", 32)));
        assertThrows(IllegalArgumentException.class,
                () -> new HeightCondition("height", configuration(-64, "high")));
        assertThrows(IllegalArgumentException.class,
                () -> new HeightCondition("height", configuration(33, 32)));
    }

    private void givenPlayerHeight(int height) {
        when(context.getInitiator()).thenReturn(player);
        when(player.getLocation()).thenReturn(location);
        when(location.getBlockY()).thenReturn(height);
    }

    private HeightCondition condition(Integer minimum, Integer maximum) {
        return new HeightCondition("height", configuration(minimum, maximum));
    }

    private YamlConfiguration configuration(Object minimum, Object maximum) {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.createSection("height");
        if(minimum != null) {
            configuration.set("height.minimum", minimum);
        }
        if(maximum != null) {
            configuration.set("height.maximum", maximum);
        }
        return configuration;
    }
}
