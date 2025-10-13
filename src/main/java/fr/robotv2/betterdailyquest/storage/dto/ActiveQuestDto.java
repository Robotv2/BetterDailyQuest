package fr.robotv2.betterdailyquest.storage.dto;

import fr.robotv2.anchor.api.annotation.Column;
import fr.robotv2.anchor.api.annotation.Entity;
import fr.robotv2.anchor.api.annotation.Id;
import fr.robotv2.anchor.api.repository.Identifiable;
import fr.robotv2.betterdailyquest.storage.model.ActiveQuest;
import org.jetbrains.annotations.ApiStatus;

import java.util.UUID;

@Entity("active_quests")
public class ActiveQuestDto implements Identifiable<UUID> {

    @Id
    @Column("active_quest_id")
    private UUID activeQuestUniqueId;

    @Column("owner_id")
    private UUID owner;

    @Column("quest_id")
    private String questId;

    @Column("group_id")
    private String groupId;

    @Column("next_reset")
    private long nextReset;

    @Column("started")
    private boolean started;

    @Column("reroll_count")
    private int rerollCount;

    @ApiStatus.Internal
    public ActiveQuestDto() {

    }

    public ActiveQuestDto(ActiveQuest activeQuest) {
        this.activeQuestUniqueId = activeQuest.getUID();
        this.owner = activeQuest.getOwner();
        this.questId = activeQuest.getQuestId();
        this.groupId = activeQuest.getGroupId();
        this.nextReset = activeQuest.getNextReset();
        this.started = activeQuest.isStarted();
        this.rerollCount = activeQuest.getRerollCount();
    }

    public ActiveQuestDto(UUID activeQuestUniqueId, UUID owner, String questId, String groupId, long nextReset, boolean started, int rerollCount) {
        this.activeQuestUniqueId = activeQuestUniqueId;
        this.owner = owner;
        this.questId = questId;
        this.groupId = groupId;
        this.nextReset = nextReset;
        this.started = started;
        this.rerollCount = rerollCount;
    }

    @Override
    public UUID getId() {
        return activeQuestUniqueId;
    }

    public UUID getOwner() {
        return owner;
    }

    public String getQuestId() {
        return questId;
    }

    public String getGroupId() {
        return groupId;
    }

    public long getNextReset() {
        return nextReset;
    }

    public boolean isStarted() {
        return started;
    }

    public int getRerollCount() {
        return rerollCount;
    }
}
