package fr.robotv2.betterdailyquest;

import fr.robotv2.betterdailyquest.cron.CronJob;
import fr.robotv2.betterdailyquest.group.QuestGroup;
import fr.robotv2.placeholderannotationlib.annotations.Expansion;
import fr.robotv2.placeholderannotationlib.annotations.Placeholder;
import fr.robotv2.placeholderannotationlib.api.BasePlaceholderExpansion;
import fr.robotv2.placeholderannotationlib.api.PlaceholderAnnotationProcessor;

@Expansion(
        identifier = "betterdailyquest",
        author = "Robotv2",
        version = "0.0.2",
        persist = true
)
public class BetterDailyQuestClipPlaceholder extends BasePlaceholderExpansion {

    private final BetterDailyQuest plugin;

    public BetterDailyQuestClipPlaceholder(BetterDailyQuest plugin, PlaceholderAnnotationProcessor processor) {
        super(processor);
        this.plugin = plugin;
        processor.registerValueResolver(QuestGroup.class, (actor, param) -> plugin.getQuestGroupManager().getGroup(param));
    }

    // %betterdailyquest_group_reset_{group}%
    @Placeholder({"group", "reset"})
    public String getFormattedRemainingTime(QuestGroup group) {
        if(group == null) {
            return "Invalid group";
        }

        final CronJob cronJob = group.getCronJob();
        if(cronJob == null) {
            return "No reset scheduled.";
        }

        final long millis = cronJob.getNextExecution() - System.currentTimeMillis();
        return plugin.getQuestConfiguration().getTimeFormatConfiguration().format(millis);
    }
}
