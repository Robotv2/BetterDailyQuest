package fr.robotv2.betterdailyquest.conditions.impl.player;

import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameModeConditionTest {

    @Mock
    private RunningQuestContext<?, ?> context;

    @Mock
    private Player player;

    @Test
    void allowsListedGameModesCaseInsensitively() {
        GameModeCondition condition = condition(List.of("survival", "ADVENTURE"));
        when(context.getInitiator()).thenReturn(player);
        when(player.getGameMode()).thenReturn(GameMode.ADVENTURE);

        assertTrue(condition.isMet(context));
    }

    @Test
    void rejectsUnlistedGameMode() {
        GameModeCondition condition = condition(List.of("SURVIVAL"));
        when(context.getInitiator()).thenReturn(player);
        when(player.getGameMode()).thenReturn(GameMode.CREATIVE);

        assertFalse(condition.isMet(context));
    }

    @Test
    void exposesConfiguredErrorMessage() {
        YamlConfiguration configuration = configuration(List.of("SURVIVAL"));
        configuration.set("game_modes.error-message", "Use Survival mode.");

        assertEquals("Use Survival mode.", new GameModeCondition("game_modes", configuration).callback());
    }

    @Test
    void invalidConfigurationIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameModeCondition("game_modes", new YamlConfiguration()));
        assertThrows(IllegalArgumentException.class,
                () -> condition(Collections.emptyList()));
        assertThrows(IllegalArgumentException.class,
                () -> condition(List.of(" ")));
        assertThrows(IllegalArgumentException.class,
                () -> condition(List.of("UNKNOWN")));
    }

    private GameModeCondition condition(List<String> gameModes) {
        return new GameModeCondition("game_modes", configuration(gameModes));
    }

    private YamlConfiguration configuration(List<String> gameModes) {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("game_modes.required", gameModes);
        return configuration;
    }
}
