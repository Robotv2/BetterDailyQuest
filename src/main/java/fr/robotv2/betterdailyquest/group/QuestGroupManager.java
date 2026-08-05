package fr.robotv2.betterdailyquest.group;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.util.FileUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class QuestGroupManager {

    private final Map<String, QuestGroup> groups;
    private final List<String> errors;

    private QuestGroupManager(Map<String, QuestGroup> groups, List<String> errors) {
        this.groups = Map.copyOf(groups);
        this.errors = List.copyOf(errors);
    }

    public static QuestGroupManager load(BetterDailyQuest plugin, File groupFolder) {
        final Map<String, QuestGroup> groups = new HashMap<>();
        final List<String> errors = new ArrayList<>();

        if(!groupFolder.exists()) {
            groupFolder.mkdirs();
            setupDefaultGroups(Objects.requireNonNull(plugin, "plugin"));
        }

        FileUtil.iterateFiles(groupFolder, file -> loadFile(file, groups, errors));
        return new QuestGroupManager(groups, errors);
    }

    @UnmodifiableView
    public Collection<QuestGroup> getGroups() {
        return groups.values();
    }

    @Nullable
    @Contract("null -> null")
    public QuestGroup getGroup(@Nullable String groupId) {
        return groupId == null ? null : groups.get(groupId.toLowerCase(Locale.ROOT));
    }

    public List<String> getErrors() {
        return errors;
    }

    public void startCronJobs() {
        final List<QuestGroup> started = new ArrayList<>();
        try {
            for(QuestGroup group : groups.values()) {
                group.startCronJob();
                started.add(group);
            }
        } catch (RuntimeException exception) {
            started.forEach(QuestGroup::stopCronJob);
            throw exception;
        }
    }

    public void stopCronJobs() {
        groups.values().forEach(QuestGroup::stopCronJob);
    }

    private static void loadFile(File file, Map<String, QuestGroup> groups, List<String> errors) {
        if(!file.getName().endsWith(".yml")) {
            return;
        }

        final YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            errors.add("Group file '" + file.getName() + "': " + exception.getMessage());
            return;
        }

        if(configuration.isConfigurationSection("groups")) {
            final ConfigurationSection section = Objects.requireNonNull(configuration.getConfigurationSection("groups"));
            section.getKeys(false).forEach(id -> loadGroup(id, section.getConfigurationSection(id), groups, errors));
        } else {
            loadGroup(FileUtil.getFileNameWithoutExtension(file), configuration, groups, errors);
        }
    }

    private static void loadGroup(String id, ConfigurationSection section, Map<String, QuestGroup> groups, List<String> errors) {
        try {
            final QuestGroup group = new QuestGroup(id, Objects.requireNonNull(section));
            if(groups.putIfAbsent(id.toLowerCase(Locale.ROOT), group) != null) {
                errors.add("Duplicate quest group id '" + id + "'.");
            }
        } catch (Exception exception) {
            errors.add("Quest group '" + id + "': " + exception.getMessage());
        }
    }

    private static void setupDefaultGroups(BetterDailyQuest plugin) {
        plugin.saveResource("groups" + File.separator + "daily.yml", false);
        plugin.saveResource("groups" + File.separator + "weekly.yml", false);
        plugin.saveResource("groups" + File.separator + "monthly.yml", false);
    }
}
