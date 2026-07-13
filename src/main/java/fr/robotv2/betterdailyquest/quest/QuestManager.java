package fr.robotv2.betterdailyquest.quest;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.group.QuestGroup;
import fr.robotv2.betterdailyquest.quest.options.Optionnable;
import fr.robotv2.betterdailyquest.storage.model.QuestPlayer;
import fr.robotv2.betterdailyquest.util.FileUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.File;
import java.util.*;

public class QuestManager {

    private static final SplittableRandom QUEST_RANDOM = new SplittableRandom();

    private final BetterDailyQuest plugin;
    private final File questFolder;
    private final Table<String, String, Quest> quests;
    private final Map<String, String> questGroupsByQuestId;

    public QuestManager(BetterDailyQuest plugin, File questFolder) {
        this.plugin = plugin;
        this.questFolder = questFolder;
        this.quests = HashBasedTable.create();
        this.questGroupsByQuestId = new HashMap<>();
    }

    public Quest fromId(@NotNull String id, @NotNull String groupId) {
        return quests.get(id, groupId);
    }

    public void clearQuests() {
        quests.clear();
        questGroupsByQuestId.clear();
    }

    @UnmodifiableView
    public Collection<Quest> getQuests() {
        return Collections.unmodifiableCollection(this.quests.values());
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

    public void loadQuests() {

        clearQuests();

        if(!questFolder.exists()) {
            questFolder.mkdirs();
            setupDefaultQuests();
        }

        FileUtil.iterateFiles(questFolder, (file) -> {
            final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            if(configuration.isConfigurationSection("quests")) {
                final ConfigurationSection questSection = Objects.requireNonNull(configuration.getConfigurationSection("quests"));
                questSection.getKeys(false).forEach((key) -> loadQuest(key, questSection.getConfigurationSection(key)));
            } else {
                loadQuest(FileUtil.getFileNameWithoutExtension(file).toLowerCase(), configuration);
            }
        });
    }

    private void loadQuest(String questId, @NotNull ConfigurationSection section) {
        try {
            final Quest quest = new Quest(plugin, questId, section);
            final String groupId = quest.getQuestGroup().getGroupId();
            final String existingGroup = registerQuestId(quest.getQuestId(), groupId);
            if(existingGroup != null) {
                plugin.getLogger().warning(" ");
                plugin.getLogger().warning(" WARNING - " + questId);
                plugin.getLogger().warning("Duplicate quest id '" + quest.getQuestId() + "' found in group '" + groupId + "'.");
                plugin.getLogger().warning("Quest ids must be globally unique. This quest id is already registered in group '" + existingGroup + "'.");
                plugin.getLogger().warning("This duplicate quest will not be loaded.");
                plugin.getLogger().warning(" ");
                return;
            }
            quests.put(quest.getQuestId(), quest.getQuestGroup().getGroupId(), quest);
            this.plugin.getLogger().info(questId + " has been loaded successfully.");
        } catch (Exception exception) {
            plugin.getLogger().warning(" ");
            plugin.getLogger().warning(" WARNING - " + questId);
            plugin.getLogger().warning("An error occurred while loading quest '" + questId + "'");
            plugin.getLogger().warning("Error's message: " + exception.getMessage());
            plugin.getLogger().warning(" ");
            plugin.getLogger().warning("This quest will not be loaded. Please fix it and then reload");
            plugin.getLogger().warning("the plugin.");
            plugin.getLogger().warning(" ");
        }
    }

    private void setupDefaultQuests() {
        plugin.saveResource("quests" + File.separator + "daily-quests.yml", false);
    }

    @Nullable
    String registerQuestId(String questId, String groupId) {
        return questGroupsByQuestId.putIfAbsent(questId.toLowerCase(Locale.ROOT), groupId);
    }
}
