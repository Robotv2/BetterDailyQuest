package fr.robotv2.betterdailyquest.configurations;

import com.cryptomorin.xseries.XMaterial;
import fr.robotv2.betterdailyquest.group.QuestGroup;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import fr.robotv2.betterdailyquest.storage.model.QuestPlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class QuestBoardConfiguration {

    private final String title;
    private final int rows;
    private final ItemConfiguration fillerItem;
    private final Map<String, GroupConfiguration> groups = new LinkedHashMap<>();
    private final List<String> errors = new ArrayList<>();
    private final String waitingStatus;
    private final String startedStatus;
    private final String completedStatus;
    private final String unavailableStatus;

    public QuestBoardConfiguration(ConfigurationSection section) {
        if(section == null) {
            this.title = "";
            this.rows = 0;
            this.fillerItem = ItemConfiguration.invalid();
            this.waitingStatus = "";
            this.startedStatus = "";
            this.completedStatus = "";
            this.unavailableStatus = "";
            errors.add("missing 'quest-board' section");
            return;
        }

        this.title = section.getString("title", "");
        this.rows = section.getInt("rows");
        this.fillerItem = readItem(section.getConfigurationSection("filler-item"), "filler-item", false);

        ConfigurationSection statuses = section.getConfigurationSection("status");
        this.waitingStatus = getString(statuses, "waiting", "&eWaiting");
        this.startedStatus = getString(statuses, "started", "&aIn progress");
        this.completedStatus = getString(statuses, "completed", "&aCompleted");
        this.unavailableStatus = getString(statuses, "unavailable", "&cUnavailable");

        if(rows < 1 || rows > 6) {
            errors.add("'rows' must be between 1 and 6");
        }
        if(title.length() > 32) {
            errors.add("'title' must be at most 32 characters for supported servers");
        }

        ConfigurationSection groupSection = section.getConfigurationSection("groups");
        if(groupSection == null) {
            errors.add("missing 'groups' section");
            return;
        }

        Set<Integer> usedSlots = new HashSet<>();
        for(String groupId : groupSection.getKeys(false)) {
            ConfigurationSection configuredGroup = groupSection.getConfigurationSection(groupId);
            if(configuredGroup == null) {
                errors.add("group '" + groupId + "' must be a section");
                continue;
            }

            List<Integer> slots = readSlots(configuredGroup, groupId, usedSlots);
            GroupConfiguration group = new GroupConfiguration(
                    groupId,
                    slots,
                    readItem(configuredGroup.getConfigurationSection("quest-item"), "groups." + groupId + ".quest-item", true),
                    readItem(configuredGroup.getConfigurationSection("empty-item"), "groups." + groupId + ".empty-item", false)
            );
            if(groups.put(groupId.toLowerCase(Locale.ROOT), group) != null) {
                errors.add("group layout '" + groupId + "' is duplicated case-insensitively");
            }
        }
    }

    public void validateGroups(Collection<QuestGroup> loadedGroups) {
        Set<String> loaded = new HashSet<>();
        for(QuestGroup group : loadedGroups) {
            String groupId = group.getGroupId().toLowerCase(Locale.ROOT);
            loaded.add(groupId);
            if(!groups.containsKey(groupId)) {
                errors.add("loaded quest group '" + group.getGroupId() + "' has no board layout");
            }
        }
        for(GroupConfiguration group : groups.values()) {
            if(!loaded.contains(group.getGroupId().toLowerCase(Locale.ROOT))) {
                errors.add("board layout references unknown quest group '" + group.getGroupId() + "'");
            }
        }
    }

    public boolean isEnabled() {
        return errors.isEmpty();
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public void logErrors(Logger logger) {
        errors.forEach(error -> logger.warning("Quest Board disabled: " + error + "."));
    }

    public Optional<String> findOverflow(QuestPlayer player) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        Map<String, String> originalIds = new LinkedHashMap<>();
        for(ActiveQuest quest : player.getActiveQuests()) {
            String groupId = quest.getGroupId().toLowerCase(Locale.ROOT);
            counts.merge(groupId, 1, Integer::sum);
            originalIds.putIfAbsent(groupId, quest.getGroupId());
        }
        for(Map.Entry<String, Integer> entry : counts.entrySet()) {
            GroupConfiguration group = groups.get(entry.getKey());
            if(group == null || entry.getValue() > group.getSlots().size()) {
                return Optional.of(originalIds.get(entry.getKey()));
            }
        }
        return Optional.empty();
    }

    public String getStatus(ActiveQuest activeQuest, boolean available) {
        if(!available) return unavailableStatus;
        if(activeQuest.isDone()) return completedStatus;
        return activeQuest.isStarted() ? startedStatus : waitingStatus;
    }

    public String getTitle() {
        return title;
    }

    public int getRows() {
        return rows;
    }

    public ItemConfiguration getFillerItem() {
        return fillerItem;
    }

    public Collection<GroupConfiguration> getGroups() {
        return Collections.unmodifiableCollection(groups.values());
    }

    private List<Integer> readSlots(ConfigurationSection section, String groupId, Set<Integer> usedSlots) {
        List<?> configuredSlots = section.getList("slots");
        List<Integer> slots = new ArrayList<>();
        if(configuredSlots == null || configuredSlots.isEmpty()) {
            errors.add("group '" + groupId + "' must define at least one slot");
            return slots;
        }
        Set<Integer> groupSlots = new HashSet<>();
        for(Object value : configuredSlots) {
            if(!(value instanceof Number number)) {
                errors.add("group '" + groupId + "' contains a non-numeric slot");
                continue;
            }
            if(number.doubleValue() != Math.rint(number.doubleValue())) {
                errors.add("group '" + groupId + "' contains a non-integer slot: " + value);
                continue;
            }
            int slot = number.intValue();
            if(slot < 0 || slot >= rows * 9) {
                errors.add("group '" + groupId + "' slot " + slot + " is outside this inventory");
            }
            if(!groupSlots.add(slot)) {
                errors.add("group '" + groupId + "' repeats slot " + slot);
            } else if(!usedSlots.add(slot)) {
                errors.add("slot " + slot + " overlaps another group layout");
            }
            slots.add(slot);
        }
        return slots;
    }

    private ItemConfiguration readItem(ConfigurationSection section, String path, boolean lore) {
        if(section == null) {
            errors.add("missing '" + path + "' section");
            return ItemConfiguration.invalid();
        }
        String materialName = section.getString("material", "");
        XMaterial material = XMaterial.matchXMaterial(materialName).orElse(null);
        if(material == null || material.parseMaterial() == null) {
            errors.add("'" + path + ".material' is invalid or unsupported on this server: " + materialName);
        }
        return new ItemConfiguration(material, section.getString("name", ""), lore ? section.getStringList("lore") : Collections.emptyList());
    }

    private static String getString(ConfigurationSection section, String key, String fallback) {
        return section == null ? fallback : section.getString(key, fallback);
    }

    public static class GroupConfiguration {
        private final String groupId;
        private final List<Integer> slots;
        private final ItemConfiguration questItem;
        private final ItemConfiguration emptyItem;

        private GroupConfiguration(String groupId, List<Integer> slots, ItemConfiguration questItem, ItemConfiguration emptyItem) {
            this.groupId = groupId;
            this.slots = List.copyOf(slots);
            this.questItem = questItem;
            this.emptyItem = emptyItem;
        }

        public String getGroupId() {
            return groupId;
        }

        public List<Integer> getSlots() {
            return slots;
        }

        public ItemConfiguration getQuestItem() {
            return questItem;
        }

        public ItemConfiguration getEmptyItem() {
            return emptyItem;
        }
    }

    public static class ItemConfiguration {
        private final XMaterial material;
        private final String name;
        private final List<String> lore;

        private ItemConfiguration(XMaterial material, String name, List<String> lore) {
            this.material = material;
            this.name = name;
            this.lore = List.copyOf(lore);
        }

        private static ItemConfiguration invalid() {
            return new ItemConfiguration(null, "", Collections.emptyList());
        }

        public XMaterial getMaterial() {
            return material;
        }

        public String getName() {
            return name;
        }

        public List<String> getLore() {
            return lore;
        }
    }
}
