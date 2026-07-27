package fr.robotv2.betterdailyquest.group;

import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.util.FileUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.File;
import java.util.*;

public class QuestGroupManager {

    private final Map<String, QuestGroup> groups = new HashMap<>();
    private final File groupFolder;

    public QuestGroupManager(File groupFolder) {
        this.groupFolder = groupFolder;
    }

    @UnmodifiableView
    public Collection<QuestGroup> getGroups() {
        return Collections.unmodifiableCollection(groups.values());
    }

    @Nullable
    @Contract("null -> null")
    public QuestGroup getGroup(@Nullable String groupId) {
        if(groupId == null) return null;
        return groups.get(groupId.toLowerCase());
    }

    public void loadGroups() {
        groups.clear();

        if(!groupFolder.exists()) {
            groupFolder.mkdirs();
            setupDefaultGroups();
        }

        FileUtil.iterateFiles(groupFolder, (file) -> {
            if(!file.getName().endsWith(".yml")) {
                return;
            }

            final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

            if(configuration.isConfigurationSection("groups")) {
                final ConfigurationSection groupSection = Objects.requireNonNull(configuration.getConfigurationSection("groups"));
                groupSection.getKeys(false).forEach((key) -> loadGroup(key, groupSection.getConfigurationSection(key)));
            } else {
                loadGroup(FileUtil.getFileNameWithoutExtension(file), configuration);
            }
        });
    }

    private void loadGroup(String groupId, ConfigurationSection section) {
        final QuestGroup group = new QuestGroup(groupId, section);
        this.groups.put(groupId.toLowerCase(), group);
    }

    private void setupDefaultGroups() {
        BetterDailyQuest.instance().saveResource("groups" + File.separator + "daily.yml", false);
        BetterDailyQuest.instance().saveResource("groups" + File.separator + "weekly.yml", false);
        BetterDailyQuest.instance().saveResource("groups" + File.separator + "monthly.yml", false);
    }
}
