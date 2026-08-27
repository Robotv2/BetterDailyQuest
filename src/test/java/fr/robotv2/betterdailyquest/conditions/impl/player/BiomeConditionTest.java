package fr.robotv2.betterdailyquest.conditions.impl.player;

import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BiomeConditionTest {

    private static final Set<String> SUPPORTED_BIOMES = Set.of("FOREST", "PLAINS", "DESERT");

    @Mock
    private RunningQuestContext<?, ?> context;

    @Mock
    private Player player;

    @Mock
    private Location location;

    @Mock
    private Block block;

    @Test
    void allowsListedBiomesCaseInsensitively() {
        BiomeCondition condition = condition(List.of("forest", "PLAINS"));
        givenPlayerBiome("forest");

        assertTrue(condition.isMet(context));
    }

    @Test
    void rejectsUnlistedBiome() {
        BiomeCondition condition = condition(List.of("PLAINS"));
        givenPlayerBiome("desert");

        assertFalse(condition.isMet(context));
    }

    @Test
    void exposesConfiguredErrorMessage() {
        YamlConfiguration configuration = configuration(List.of("PLAINS"));
        configuration.set("biomes.error-message", "Go to a plains biome.");

        assertEquals("Go to a plains biome.", new BiomeCondition(
                "biomes", configuration, SUPPORTED_BIOMES, ignored -> "PLAINS").callback());
    }

    @Test
    void invalidConfigurationIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new BiomeCondition("biomes", new YamlConfiguration(),
                        SUPPORTED_BIOMES, ignored -> "PLAINS"));
        assertThrows(IllegalArgumentException.class,
                () -> condition(Collections.emptyList()));
        assertThrows(IllegalArgumentException.class,
                () -> condition(List.of(" ")));
        assertThrows(IllegalArgumentException.class,
                () -> condition(List.of("NOT_A_BIOME")));
    }

    private String currentBiome;

    private void givenPlayerBiome(String biomeName) {
        currentBiome = biomeName;
        when(context.getInitiator()).thenReturn(player);
        when(player.getLocation()).thenReturn(location);
        when(location.getBlock()).thenReturn(block);
    }

    private BiomeCondition condition(List<String> biomes) {
        return new BiomeCondition(
                "biomes", configuration(biomes), SUPPORTED_BIOMES, ignored -> currentBiome);
    }

    private YamlConfiguration configuration(List<String> biomes) {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("biomes.required", biomes);
        return configuration;
    }
}
