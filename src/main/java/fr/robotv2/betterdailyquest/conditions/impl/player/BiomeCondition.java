package fr.robotv2.betterdailyquest.conditions.impl.player;

import com.cryptomorin.xseries.base.XRegistry;
import fr.robotv2.betterdailyquest.conditions.AbstractCondition;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BiomeCondition extends AbstractCondition {

    private final Set<String> biomes = new HashSet<>();
    private final Function<Biome, String> biomeNameResolver;

    public BiomeCondition(String key, ConfigurationSection parent) {
        this(key, parent, supportedBiomeNames(), XRegistry::getBukkitName);
    }

    BiomeCondition(String key, ConfigurationSection parent, Set<String> supportedBiomes,
                   Function<Biome, String> biomeNameResolver) {
        super(key, parent);
        this.biomeNameResolver = biomeNameResolver;
        final ConfigurationSection child = parent.getConfigurationSection(key);
        if(child == null) {
            throw new IllegalArgumentException("Biomes condition must be a section.");
        }

        final List<String> required = child.getStringList("required");
        if(required.isEmpty() || required.stream().anyMatch(String::isBlank)) {
            throw new IllegalArgumentException("Biomes condition requires at least one non-empty biome.");
        }

        for(String value : required) {
            final String biome = normalize(value);
            if(!supportedBiomes.contains(biome)) {
                throw new IllegalArgumentException("Unknown or unsupported biome '" + value + "'.");
            }
            biomes.add(biome);
        }
    }

    @Override
    public boolean isMet(RunningQuestContext<?, ?> context) {
        final Biome biome = context.getInitiator().getLocation().getBlock().getBiome();
        return biomes.contains(normalize(biomeNameResolver.apply(biome)));
    }

    private static Set<String> supportedBiomeNames() {
        try {
            final Object[] values = (Object[]) Biome.class.getMethod("values").invoke(null);
            return Arrays.stream(values)
                    .map(XRegistry::getBukkitName)
                    .map(BiomeCondition::normalize)
                    .collect(Collectors.toSet());
        } catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            throw new IllegalStateException("Could not read the biomes supported by this server.", exception);
        }
    }

    private static String normalize(String value) {
        final int namespaceSeparator = value.indexOf(':');
        final String name = namespaceSeparator >= 0 ? value.substring(namespaceSeparator + 1) : value;
        return name.toUpperCase(Locale.ROOT);
    }
}
