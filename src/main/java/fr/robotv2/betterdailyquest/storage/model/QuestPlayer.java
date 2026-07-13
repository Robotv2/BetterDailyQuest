package fr.robotv2.betterdailyquest.storage.model;

import fr.robotv2.betterdailyquest.group.QuestGroup;
import fr.robotv2.betterdailyquest.quest.Quest;
import fr.robotv2.betterdailyquest.quest.options.Optionnable;
import fr.robotv2.betterdailyquest.storage.DirtyAware;
import fr.robotv2.betterdailyquest.storage.dto.QuestPlayerDto;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.stream.Collectors;

public class QuestPlayer implements java.io.Serializable, DirtyAware {

    private final UUID playerUniqueId;

    private final String playerName;

    private final Set<ActiveQuest> activeQuests;

    private final Map<String, Integer> questDone;

    private transient volatile boolean dirty;

    public QuestPlayer(Player player) {
        this.playerUniqueId = player.getUniqueId();
        this.playerName = player.getName();
        this.activeQuests = new HashSet<>();
        this.questDone = new HashMap<>();
    }

    public QuestPlayer(QuestPlayerDto dto) {
        this.playerUniqueId = dto.getId();
        this.playerName = dto.getPlayerName();
        this.activeQuests = new HashSet<>();
        this.questDone = new HashMap<>(dto.getQuestDone());
    }
    
    // Constructor for schema-based repositories
    public QuestPlayer(UUID playerUniqueId, String playerName) {
        this.playerUniqueId = playerUniqueId;
        this.playerName = playerName;
        this.activeQuests = new HashSet<>();
        this.questDone = new HashMap<>();
    }

    public UUID getId() {
        return playerUniqueId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getId());
    }

    public Set<ActiveQuest> getActiveQuests() {
        return activeQuests;
    }

    @UnmodifiableView
    public List<ActiveQuest> getActiveQuests(String groupId) {
        return getActiveQuests().stream()
                .filter(quest -> quest.getGroupId().equalsIgnoreCase(groupId))
                .collect(Collectors.toList());
    }

    @UnmodifiableView
    public List<ActiveQuest> getActiveQuests(QuestGroup group) {
        return getActiveQuests(group.getGroupId());
    }

    public ActiveQuest getActiveQuest(Quest quest) {
        return getActiveQuest(quest.getQuestId());
    }

    public ActiveQuest getActiveQuest(String questId) {
        return getActiveQuests().stream()
                .filter(active -> active.getQuestId().equalsIgnoreCase(questId))
                .findFirst().orElse(null);
    }

    public void addActiveQuests(Collection<ActiveQuest> activeQuests) {
        this.activeQuests.addAll(activeQuests);
        setDirty(true);
    }

    public void addActiveQuest(ActiveQuest activeQuest) {
        this.activeQuests.add(activeQuest);
        setDirty(true);
    }

    public void removeActiveQuest(ActiveQuest activeQuest) {
        removeActiveQuestById(activeQuest.getQuestId());
        setDirty(true);
    }

    public void removeActiveQuest(Quest quest) {
        removeActiveQuestById(quest.getQuestId());
    }

    public void removeActiveQuestById(String questId) {
        boolean result = this.activeQuests.removeIf((active) -> active.getQuestId().equals(questId));
        if(result) setDirty(true);
    }

    public void removeActiveQuest(QuestGroup group) {
        boolean result = this.activeQuests.removeIf((quest) -> quest.getGroupId().equalsIgnoreCase(group.getGroupId()));
        if(result) setDirty(true);
    }

    public void clearActiveQuests() {
        activeQuests.clear();
        setDirty(true);
    }

    public boolean hasQuest(Quest quest) {
        return getActiveQuests().stream()
                .anyMatch(other -> Objects.equals(quest.getQuestId(), other.getQuestId())
                        && Objects.equals(quest.getQuestGroup().getGroupId(), other.getGroupId()));
    }

    @UnmodifiableView
    public Map<String, Integer> getQuestDone() {
        return Collections.unmodifiableMap(questDone);
    }

    public boolean hasCompletedQuest(String questId) {
        return questDone.containsKey(questId);
    }

    public boolean canReceiveQuest(Quest quest) {
        return !hasQuest(quest)
                && (quest.getOptionValue(Optionnable.Option.REPEATABLE) || !hasCompletedQuest(quest.getQuestId()));
    }

    public void recordQuestCompletion(String questId) {
        questDone.merge(questId, 1, Integer::sum);
        setDirty(true);
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
