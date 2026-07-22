package fr.robotv2.betterdailyquest.questboard;

import com.cryptomorin.xseries.XMaterial;
import fr.robotv2.betterdailyquest.BetterDailyQuest;
import fr.robotv2.betterdailyquest.configurations.QuestBoardConfiguration;
import fr.robotv2.betterdailyquest.BetterDailyQuestConfiguration;
import fr.robotv2.betterdailyquest.configurations.QuestBoardConfiguration.GroupConfiguration;
import fr.robotv2.betterdailyquest.configurations.QuestBoardConfiguration.ItemConfiguration;
import fr.robotv2.betterdailyquest.quest.Quest;
import fr.robotv2.betterdailyquest.quest.QuestManager;
import fr.robotv2.betterdailyquest.quest.task.Task;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import fr.robotv2.betterdailyquest.storage.model.ActiveTask;
import fr.robotv2.betterdailyquest.storage.model.QuestPlayer;
import fr.robotv2.betterdailyquest.storage.DatabaseManager;
import fr.robotv2.betterdailyquest.util.color.ColorProvider;
import fr.robotv2.betterdailyquest.util.placeholder.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestBoard implements InventoryHolder {

    private static final Comparator<ActiveQuest> QUEST_ID_ORDER = Comparator.comparing(ActiveQuest::getQuestId, String.CASE_INSENSITIVE_ORDER);

    private final Player player;
    private final QuestPlayer questPlayer;
    private final BetterDailyQuestConfiguration pluginConfiguration;
    private final QuestBoardConfiguration configuration;
    private final ColorProvider colorProvider;
    private final QuestManager questManager;
    private final DatabaseManager databaseManager;
    private final Inventory inventory;
    private final Map<Integer, String> questIdsBySlot = new HashMap<>();

    public QuestBoard(BetterDailyQuest plugin, Player player, QuestPlayer questPlayer) {
        this(player, questPlayer, plugin.getQuestConfiguration(), plugin.getColorProvider(),
                plugin.getQuestManager(), plugin.getDatabaseManager(), null);
    }

    QuestBoard(Player player, QuestPlayer questPlayer, BetterDailyQuestConfiguration pluginConfiguration,
               ColorProvider colorProvider, QuestManager questManager, DatabaseManager databaseManager,
               Inventory inventory) {
        this.player = player;
        this.questPlayer = questPlayer;
        this.pluginConfiguration = pluginConfiguration;
        this.configuration = pluginConfiguration.getQuestBoardConfiguration();
        this.colorProvider = colorProvider;
        this.questManager = questManager;
        this.databaseManager = databaseManager;
        if(inventory == null) {
            String title = colorProvider.colorize(Placeholders.safePlaceholderAPI(player, configuration.getTitle()));
            this.inventory = Bukkit.createInventory(this, configuration.getRows() * 9, title);
        } else {
            this.inventory = inventory;
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public String getQuestId(int slot) {
        return questIdsBySlot.get(slot);
    }

    public ActiveQuest getAssignment(String questId) {
        QuestPlayer current = databaseManager.getCachedQuestPlayer(player);
        return current == null ? null : current.getActiveQuest(questId);
    }

    public boolean isAvailable(ActiveQuest assignment) {
        return questManager.fromId(assignment.getQuestId(), assignment.getGroupId()) != null;
    }

    public void refresh() {
        populate();
    }

    public void populate() {
        questIdsBySlot.clear();
        ItemStack filler = buildItem(configuration.getFillerItem(), null, null, null, null, null);
        for(int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItem(slot, filler);
        }

        for(GroupConfiguration group : configuration.getGroups()) {
            List<ActiveQuest> assignments = sortedAssignments(questPlayer.getActiveQuests(group.getGroupId()));
            for(int index = 0; index < group.getSlots().size(); index++) {
                int slot = group.getSlots().get(index);
                if(index >= assignments.size()) {
                    inventory.setItem(slot, buildItem(group.getEmptyItem(), null, null, null, null, null));
                    continue;
                }

                ActiveQuest assignment = assignments.get(index);
                Quest quest = questManager.fromId(assignment.getQuestId(), assignment.getGroupId());
                XMaterial material = quest == null ? XMaterial.BARRIER : null;
                inventory.setItem(slot, buildItem(group.getQuestItem(), material, quest, assignment, null, null));
                questIdsBySlot.put(slot, assignment.getQuestId());
            }
        }
    }

    static List<ActiveQuest> sortedAssignments(List<ActiveQuest> assignments) {
        List<ActiveQuest> sorted = new ArrayList<>(assignments);
        sorted.sort(QUEST_ID_ORDER);
        return sorted;
    }

    static List<Task> sortedTasks(Quest quest) {
        List<Task> sorted = new ArrayList<>(quest.getTasks());
        sorted.sort(Comparator.comparingInt(Task::getTaskId));
        return sorted;
    }

    private ItemStack buildItem(ItemConfiguration itemConfiguration, XMaterial materialOverride, Quest quest,
                                ActiveQuest activeQuest, Task task, ActiveTask activeTask) {
        XMaterial material = materialOverride == null ? itemConfiguration.getMaterial() : materialOverride;
        ItemStack item = material.parseItem();
        if(item == null) {
            throw new IllegalStateException("Quest Board material became unavailable: " + material.name());
        }

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setDisplayName(render(itemConfiguration.getName(), quest, activeQuest, task, activeTask));
        if(!itemConfiguration.getLore().isEmpty()) {
            meta.setLore(expandLore(itemConfiguration.getLore(), quest, activeQuest));
        }
        item.setItemMeta(meta);
        return item;
    }

    List<String> expandLore(List<String> template, Quest quest, ActiveQuest activeQuest) {
        List<String> lore = new ArrayList<>();
        for(String line : template) {
            if("%quest_description%".equals(line)) {
                if(quest != null) {
                    quest.getDescription().forEach(description -> lore.add(render(description, quest, activeQuest, null, null)));
                }
            } else if("%task_descriptions%".equals(line)) {
                if(quest != null) {
                    for(Task task : sortedTasks(quest)) {
                        String description = task.getTaskDescription();
                        if(!description.isBlank()) {
                            lore.add(render(description, quest, activeQuest, task, activeQuest.getActiveTask(task.getTaskId())));
                        }
                    }
                }
            } else {
                lore.add(render(line, quest, activeQuest, null, null));
            }
        }
        return lore;
    }

    String render(String text, Quest quest, ActiveQuest activeQuest, Task task, ActiveTask activeTask) {
        String rendered = text;
        if(quest != null) {
            rendered = Placeholders.QUEST_PLACEHOLDER.apply(rendered, quest);
        } else if(activeQuest != null) {
            rendered = rendered
                    .replace("%quest_id%", activeQuest.getQuestId())
                    .replace("%quest_name%", activeQuest.getQuestId())
                    .replace("%quest_group%", activeQuest.getGroupId())
                    .replace("%quest_tasks%", "0");
        }
        if(task != null) {
            rendered = Placeholders.TASK_PLACEHOLDER.apply(rendered, task);
        }
        if(activeQuest != null) {
            rendered = rendered
                    .replace("%quest_player%", player.getName())
                    .replace("%quest_started%", String.valueOf(activeQuest.isStarted()))
                    .replace("%quest_done%", String.valueOf(activeQuest.isDone()))
                    .replace("%quest_tasks_completed%", String.valueOf(activeQuest.getTasks().stream().filter(ActiveTask::isDone).count()))
                    .replace("%quest_reset_timestamp%", String.valueOf(activeQuest.getNextReset()))
                    .replace("%quest_reset_time%", pluginConfiguration.getTimeFormatConfiguration()
                            .format(activeQuest.getNextReset() - System.currentTimeMillis()))
                    .replace("%quest_status%", configuration.getStatus(activeQuest, quest != null));
        }
        if(activeTask != null) {
            rendered = rendered
                    .replace("%task_progress%", activeTask.getProgress().toString())
                    .replace("%task_done%", String.valueOf(activeTask.isDone()))
                    .replace("%task_required%", activeTask.getRequired().toString());
        }
        return colorProvider.colorize(Placeholders.safePlaceholderAPI(player, rendered));
    }
}
