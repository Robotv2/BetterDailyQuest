package fr.robotv2.betterdailyquest.conditions.impl.player;

import fr.robotv2.betterdailyquest.conditions.AbstractCondition;
import fr.robotv2.betterdailyquest.quest.context.RunningQuestContext;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.List;

public class PermissionCondition extends AbstractCondition {

    private final List<String> permissions;
    private final PermissionMode mode;

    public PermissionCondition(String key, ConfigurationSection parent) {
        super(key, parent);
        final ConfigurationSection child = parent.getConfigurationSection(key);
        if(child == null) {
            throw new IllegalArgumentException("Permissions condition must be a section.");
        }
        this.permissions = child.getStringList("required");
        if(permissions.isEmpty() || permissions.stream().anyMatch(String::isBlank)) {
            throw new IllegalArgumentException("Permissions condition requires at least one non-empty permission.");
        }
        this.mode = PermissionMode.fromName(child.getString("mode", PermissionMode.ALL.name()));
    }

    @Override
    public boolean isMet(RunningQuestContext<?, ?> context) {
        if(mode == PermissionMode.ANY) {
            return permissions.stream().anyMatch(context.getInitiator()::hasPermission);
        }
        return permissions.stream().allMatch(context.getInitiator()::hasPermission);
    }

    private enum PermissionMode {
        ALL,
        ANY;

        private static PermissionMode fromName(String value) {
            return Arrays.stream(values())
                    .filter(mode -> mode.name().equalsIgnoreCase(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown permissions mode '" + value + "'."));
        }
    }
}
