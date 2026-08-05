package fr.robotv2.betterdailyquest.group;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.configurations.cosmetics.CosmeticMap;
import fr.robotv2.betterdailyquest.configurations.cosmetics.Cosmeticable;
import fr.robotv2.betterdailyquest.cron.CronJob;
import fr.robotv2.betterdailyquest.quest.options.QuestOption;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;

public class QuestGroup implements Cosmeticable {

    private static final String OPTIONS_KEY = "options";
    private static final String ASSIGNMENT_LIMITS_KEY = "assignment-limits";
    private static final String GLOBAL_ASSIGNMENT_LIMIT_KEY = "global-assignment-limit";
    private static final String AUTOMATIC_RESET_KEY = "automatic-reset";
    private static final String COSMETICS_KEY = "cosmetics";
    private static final String MAX_REROLLS_KEY = "max-rerolls";

    private final String groupId;

    private final QuestOption option;

    private final String cronSyntax;

    private final CronJob cronJob;

    private final int globalAssignmentLimit;

    private final Map<String, Integer> assignmentLimits;

    private final CosmeticMap cosmetics;

    private final int maxRerolls;

    public QuestGroup(final ConfigurationSection section) {
        this(section.getName(), section);
    }

    public QuestGroup(final String groupId, final ConfigurationSection section) {
        this.groupId = groupId;

        this.option = new QuestOption(section.getConfigurationSection(OPTIONS_KEY));
        this.globalAssignmentLimit = section.getInt(GLOBAL_ASSIGNMENT_LIMIT_KEY);

        this.cronSyntax = section.getString(AUTOMATIC_RESET_KEY);
        if(this.cronSyntax != null) {
            this.cronJob = new CronJob(this.cronSyntax, () -> BetterDailyQuest.instance().getResetHandler().reset(this));
            this.cronJob.prepare();
        } else {
            this.cronJob = null;
        }

        final ConfigurationSection assignmentLimitsSection = section.getConfigurationSection(ASSIGNMENT_LIMITS_KEY);
        if(assignmentLimitsSection != null) {
            this.assignmentLimits = new HashMap<>();
            assignmentLimitsSection.getKeys(false).forEach((key) -> this.assignmentLimits.put(key.toLowerCase(), assignmentLimitsSection.getInt(key)));
        } else {
            this.assignmentLimits = Collections.emptyMap();
        }

        this.cosmetics = new CosmeticMap(section.getConfigurationSection(COSMETICS_KEY));
        this.maxRerolls = section.getInt(MAX_REROLLS_KEY, 0);
    }


    public String getGroupId() {
        return groupId;
    }


    public QuestOption getOption() {
        return option;
    }


    public String getCronSyntax() {
        return cronSyntax;
    }

    public @Nullable CronJob getCronJob() {
        return cronJob;
    }

    public void stopCronJob() {
        if(cronJob != null) {
            cronJob.stop();
        }
    }

    public void startCronJob() {
        if(cronJob != null) {
            cronJob.start();
        }
    }

    public long getNextReset() {
        return cronJob != null ? cronJob.getNextExecution() : -1;
    }

    public int getGlobalAssignation() {
        return globalAssignmentLimit;
    }

    public OptionalInt getRoleAssignation(String role) {
        return assignmentLimits.containsKey(role.toLowerCase()) ? OptionalInt.of(assignmentLimits.get(role.toLowerCase())) : OptionalInt.empty();
    }

    public @UnmodifiableView Map<String, Integer> getRoleAssignations() {
        return Collections.unmodifiableMap(assignmentLimits);
    }

    public int getMaxRerolls() {
        return maxRerolls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestGroup group)) return false;
        return Objects.equals(groupId, group.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(groupId);
    }

    @Override
    public CosmeticMap getCosmeticMap() {
        return cosmetics;
    }
}
