package fr.robotv2.betterdailyquest;

import fr.robotv2.betterdailyquest.configurations.cosmetics.CosmeticMap;
import fr.robotv2.betterdailyquest.configurations.cosmetics.Cosmeticable;
import fr.robotv2.betterdailyquest.configurations.QuestBoardConfiguration;
import fr.robotv2.betterdailyquest.configurations.messages.MessageConfiguration;
import fr.robotv2.betterdailyquest.configurations.time.TimeFormatConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class BetterDailyQuestConfiguration implements Cosmeticable {

    private boolean debug;

    private CosmeticMap cosmetics;

    private TimeFormatConfiguration timeFormatConfiguration;

    private MessageConfiguration messageConfiguration;

    private QuestBoardConfiguration questBoardConfiguration;

    public void loadConfiguration(@NotNull FileConfiguration configuration) {
        this.debug = configuration.getBoolean("debug");
        this.cosmetics = new CosmeticMap(configuration.getConfigurationSection("cosmetics"));
        this.timeFormatConfiguration = new TimeFormatConfiguration(configuration.getConfigurationSection("time_format"));
        this.messageConfiguration = new MessageConfiguration(configuration.getConfigurationSection("messages"));
        this.questBoardConfiguration = new QuestBoardConfiguration(configuration.getConfigurationSection("quest-board"));
    }

    public boolean isDebug() {
        return debug;
    }

    public TimeFormatConfiguration getTimeFormatConfiguration() {
        return timeFormatConfiguration;
    }

    public MessageConfiguration getMessageConfiguration() {
        return messageConfiguration;
    }

    public QuestBoardConfiguration getQuestBoardConfiguration() {
        return questBoardConfiguration;
    }

    @Override
    public CosmeticMap getCosmeticMap() {
        return cosmetics;
    }
}
