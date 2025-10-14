package fr.robotv2.betterdailyquest.conditions;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCondition implements Condition {

    private final String callback;

    public AbstractCondition(String key, ConfigurationSection section) {
        this.callback = section.getString(key + ".error-message", null);
    }

    @Override
    @Nullable
    public String callback() {
        return callback;
    }
}
