package fr.robotv2.betterdailyquest.conditions.impl.player;

import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import fr.robotv2.betterdailyquest.util.placeholder.Placeholders;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceholderConditionTest {

    @Mock
    private RunningQuestContext<?, ?> context;

    @Mock
    private Player player;

    @ParameterizedTest
    @CsvSource({
            "MORE, 11, true",
            "MORE, 10, false",
            "MORE_EQUAL, 10, true",
            "MORE_EQUAL, 9, false",
            "EQUAL, 10.0, true",
            "EQUAL, 11, false",
            "LESS_EQUAL, 10, true",
            "LESS_EQUAL, 11, false",
            "LESS, 9, true",
            "LESS, 10, false"
    })
    void numericalComparatorsReturnTheirResult(String comparator, String actual, boolean expected) {
        PlaceholderCondition condition = condition("10", comparator);
        when(context.getInitiator()).thenReturn(player);

        try (MockedStatic<Placeholders> placeholders = mockStatic(Placeholders.class)) {
            placeholders.when(() -> Placeholders.safePlaceholderAPI(player, "%balance%"))
                    .thenReturn(actual);

            if(expected) {
                assertTrue(condition.isMet(context));
            } else {
                assertFalse(condition.isMet(context));
            }
        }
    }

    @Test
    void orderedComparatorRejectsNonNumericResolvedValue() {
        PlaceholderCondition condition = condition("10", "MORE");
        when(context.getInitiator()).thenReturn(player);

        try (MockedStatic<Placeholders> placeholders = mockStatic(Placeholders.class)) {
            placeholders.when(() -> Placeholders.safePlaceholderAPI(player, "%balance%"))
                    .thenReturn("unknown");

            assertFalse(condition.isMet(context));
        }
    }

    @Test
    void stringEqualityRemainsExact() {
        PlaceholderCondition condition = condition("ready", "EQUAL");
        when(context.getInitiator()).thenReturn(player);

        try (MockedStatic<Placeholders> placeholders = mockStatic(Placeholders.class)) {
            placeholders.when(() -> Placeholders.safePlaceholderAPI(player, "%balance%"))
                    .thenReturn("Ready");

            assertFalse(condition.isMet(context));
        }
    }

    @Test
    void everyPlaceholderEntryMustMatch() {
        YamlConfiguration configuration = configuration("10", "MORE");
        configuration.set("placeholders.state.placeholder", "%state%");
        configuration.set("placeholders.state.match", "ready");
        PlaceholderCondition condition = new PlaceholderCondition("placeholders", configuration);
        when(context.getInitiator()).thenReturn(player);

        try (MockedStatic<Placeholders> placeholders = mockStatic(Placeholders.class)) {
            placeholders.when(() -> Placeholders.safePlaceholderAPI(player, "%balance%"))
                    .thenReturn("11");
            placeholders.when(() -> Placeholders.safePlaceholderAPI(player, "%state%"))
                    .thenReturn("waiting");

            assertFalse(condition.isMet(context));
        }
    }

    @Test
    void invalidConfigurationIsRejected() {
        YamlConfiguration missingPlaceholder = configuration("10", "MORE");
        missingPlaceholder.set("placeholders.balance.placeholder", null);
        assertThrows(IllegalArgumentException.class,
                () -> new PlaceholderCondition("placeholders", missingPlaceholder));

        YamlConfiguration missingMatch = configuration("10", "MORE");
        missingMatch.set("placeholders.balance.match", null);
        assertThrows(IllegalArgumentException.class,
                () -> new PlaceholderCondition("placeholders", missingMatch));

        assertThrows(IllegalArgumentException.class,
                () -> condition("10", "UNKNOWN"));
        assertThrows(IllegalArgumentException.class,
                () -> condition("ready", "MORE"));
    }

    private PlaceholderCondition condition(String match, String comparator) {
        return new PlaceholderCondition("placeholders", configuration(match, comparator));
    }

    private YamlConfiguration configuration(String match, String comparator) {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("placeholders.balance.placeholder", "%balance%");
        configuration.set("placeholders.balance.match", match);
        configuration.set("placeholders.balance.comparator", comparator);
        return configuration;
    }
}
