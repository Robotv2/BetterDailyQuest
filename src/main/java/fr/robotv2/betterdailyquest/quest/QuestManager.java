package fr.robotv2.betterdailyquest.quest;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.group.QuestGroup;
import fr.robotv2.betterdailyquest.group.QuestGroupManager;
import fr.robotv2.betterdailyquest.quest.options.Optionnable;
import fr.robotv2.betterdailyquest.storage.model.QuestPlayer;
import fr.robotv2.betterdailyquest.util.FileUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class QuestManager {

    private static final SplittableRandom QUEST_RANDOM = new SplittableRandom();

    private final Table<String, String, Quest> quests;
    private final List<String> errors;

    private QuestManager(Table<String, String, Quest> quests, List<String> errors) {
        this.quests = ImmutableTable.copyOf(quests);
        this.errors = List.copyOf(errors);
    }

    public static QuestManager load(BetterDailyQuest plugin, File questFolder, QuestGroupManager groupManager) {
        final Table<String, String, Quest> quests = HashBasedTable.create();
        final Map<String, String> questGroupsByQuestId = new HashMap<>();
        final List<String> errors = new ArrayList<>();

        if(!questFolder.exists()) {
            questFolder.mkdirs();
            setupDefaultQuests(Objects.requireNonNull(plugin, "plugin"));
        }

        FileUtil.iterateFiles(questFolder, file -> loadFile(
                plugin, file, groupManager, quests, questGroupsByQuestId, errors
        ));
        return new QuestManager(quests, errors);
    }

    public Quest fromId(@NotNull String id, @NotNull String groupId) {
        return quests.get(id, groupId);
    }

    @UnmodifiableView
    public Collection<Quest> getQuests() {
        return this.quests.values();
    }

    @UnmodifiableView
    public List<Quest> getQuests(QuestGroup group) {
        return List.copyOf(quests.column(group.getGroupId()).values());
    }

    @Nullable
    public Quest getRandomQuest(QuestGroup group) {
        final List<Quest> quests = this.getQuests(group);
        final int size = quests.size();

        if(quests.isEmpty()) {
            return null; // no quest for this reset id.
        }

        return quests.get(QUEST_RANDOM.nextInt(size));
    }

    @Nullable
    public Quest getRandomQuest(QuestPlayer player, QuestGroup group) {
        final List<Quest> quests = new ArrayList<>(this.getQuests(group));

        // remove all quest that the player has already done and are not repeatable
        quests.removeIf((quest) -> player.hasCompletedQuest(quest.getQuestId()) && !quest.getOptionValue(Optionnable.Option.REPEATABLE));
        // do not check if the player already has this quest
        quests.removeIf(player::hasQuest);

        final int size = quests.size();

        if(quests.isEmpty()) {
            return null; // no quest for this reset id.
        }

        return quests.get(QUEST_RANDOM.nextInt(size));
    }

    public Set<Quest> getNRandomQuests(QuestGroup group, int n) {

        final List<Quest> quests = this.getQuests(group);
        final int size = quests.size();
        final Set<Quest> randomQuests = new HashSet<>();

        if (quests.isEmpty() || n <= 0) {
            return randomQuests;
        }

        n = Math.min(n, size);

        while (randomQuests.size() < n) {
            Quest randomQuest = getRandomQuest(group);
            if (randomQuest != null) {
                randomQuests.add(randomQuest);
            }
        }

        return randomQuests;
    }

    private static void loadFile(BetterDailyQuest plugin, File file, QuestGroupManager groups,
                                 Table<String, String, Quest> quests, Map<String, String> questIds,
                                 List<String> errors) {
        if(!file.getName().endsWith(".yml")) {
            return;
        }

        final YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            errors.add("Quest file '" + file.getName() + "': " + exception.getMessage());
            return;
        }

        if(configuration.isConfigurationSection("quests")) {
            final ConfigurationSection section = Objects.requireNonNull(configuration.getConfigurationSection("quests"));
            section.getKeys(false).forEach(id -> loadQuest(
                    plugin, id, section.getConfigurationSection(id), groups, quests, questIds, errors
            ));
        } else {
            loadQuest(plugin, FileUtil.getFileNameWithoutExtension(file).toLowerCase(Locale.ROOT), configuration,
                    groups, quests, questIds, errors);
        }
    }

    private static void loadQuest(BetterDailyQuest plugin, String questId, @NotNull ConfigurationSection section,
                                  QuestGroupManager groups, Table<String, String, Quest> quests,
                                  Map<String, String> questIds, List<String> errors) {
        try {
            final String configuredGroupId = section.getString("group");
            final QuestGroup group = Objects.requireNonNull(
                    groups.getGroup(configuredGroupId),
                    "Unknown quest group '" + configuredGroupId + "'."
            );
            final Quest quest = new Quest(plugin, questId, section, group);
            final String groupId = quest.getQuestGroup().getGroupId();
            final String existingGroup = questIds.putIfAbsent(quest.getQuestId().toLowerCase(Locale.ROOT), groupId);
            if(existingGroup != null) {
                errors.add("Duplicate quest id '" + quest.getQuestId() + "' in groups '" + existingGroup + "' and '" + groupId + "'.");
                return;
            }
            quests.put(quest.getQuestId(), quest.getQuestGroup().getGroupId(), quest);
            if(plugin != null) {
                plugin.getLogger().info(questId + " has been loaded successfully.");
            }
        } catch (Exception exception) {
            errors.add("Quest '" + questId + "': " + exception.getMessage());
        }
    }

    public List<String> getErrors() {
        return errors;
    }

    private static void setupDefaultQuests(BetterDailyQuest plugin) {
        plugin.saveResource("quests" + File.separator + "daily-quests.yml", false);
        plugin.saveResource("quests" + File.separator + "weekly-quests.yml", false);
        plugin.saveResource("quests" + File.separator + "monthly-quests.yml", false);
    }

}
