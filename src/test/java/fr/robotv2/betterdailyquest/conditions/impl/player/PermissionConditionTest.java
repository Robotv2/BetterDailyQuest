package fr.robotv2.betterdailyquest.conditions.impl.player;

import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PermissionConditionTest {

    @Mock
    private RunningQuestContext<?, ?> context;

    @Mock
    private Player player;

    @Test
    void allModeRequiresEveryPermission() {
        PermissionCondition condition = condition("ALL");
        when(context.getInitiator()).thenReturn(player);
        when(player.hasPermission("quest.one")).thenReturn(true);
        when(player.hasPermission("quest.two")).thenReturn(false);

        assertFalse(condition.isMet(context));
    }

    @Test
    void anyModeRequiresOnePermission() {
        PermissionCondition condition = condition("any");
        when(context.getInitiator()).thenReturn(player);
        when(player.hasPermission("quest.one")).thenReturn(false);
        when(player.hasPermission("quest.two")).thenReturn(true);

        assertTrue(condition.isMet(context));
    }

    @Test
    void omittedModeDefaultsToAll() {
        PermissionCondition condition = condition(null);
        when(context.getInitiator()).thenReturn(player);
        when(player.hasPermission("quest.one")).thenReturn(true);
        when(player.hasPermission("quest.two")).thenReturn(false);

        assertFalse(condition.isMet(context));
    }

    @Test
    void exposesConfiguredErrorMessage() {
        YamlConfiguration configuration = configuration("ALL");
        configuration.set("permissions.error-message", "Missing permission.");

        assertEquals("Missing permission.", new PermissionCondition("permissions", configuration).callback());
    }

    @Test
    void invalidConfigurationIsRejected() {
        YamlConfiguration missing = new YamlConfiguration();
        assertThrows(IllegalArgumentException.class,
                () -> new PermissionCondition("permissions", missing));

        YamlConfiguration empty = configuration("ALL");
        empty.set("permissions.required", Collections.emptyList());
        assertThrows(IllegalArgumentException.class,
                () -> new PermissionCondition("permissions", empty));

        YamlConfiguration blank = configuration("ALL");
        blank.set("permissions.required", Collections.singletonList(" "));
        assertThrows(IllegalArgumentException.class,
                () -> new PermissionCondition("permissions", blank));

        assertThrows(IllegalArgumentException.class,
                () -> condition("SOME"));
    }

    private PermissionCondition condition(String mode) {
        return new PermissionCondition("permissions", configuration(mode));
    }

    private YamlConfiguration configuration(String mode) {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("permissions.required", Arrays.asList("quest.one", "quest.two"));
        if(mode != null) {
            configuration.set("permissions.mode", mode);
        }
        return configuration;
    }
}
