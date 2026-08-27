package fr.robotv2.betterdailyquest.conditions.impl.player;

import fr.robotv2.betterdailyquest.conditions.AbstractCondition;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class GameModeCondition extends AbstractCondition {

    private final Set<GameMode> gameModes = EnumSet.noneOf(GameMode.class);

    public GameModeCondition(String key, ConfigurationSection parent) {
        super(key, parent);
        final ConfigurationSection child = parent.getConfigurationSection(key);
        if(child == null) {
            throw new IllegalArgumentException("Game modes condition must be a section.");
        }

        final List<String> required = child.getStringList("required");
        if(required.isEmpty() || required.stream().anyMatch(String::isBlank)) {
            throw new IllegalArgumentException("Game modes condition requires at least one non-empty game mode.");
        }

        for(String value : required) {
            try {
                gameModes.add(GameMode.valueOf(value.toUpperCase(Locale.ROOT)));
            } catch(IllegalArgumentException exception) {
                throw new IllegalArgumentException("Unknown game mode '" + value + "'.", exception);
            }
        }
    }

    @Override
    public boolean isMet(RunningQuestContext<?, ?> context) {
        return gameModes.contains(context.getInitiator().getGameMode());
    }
}
