package fr.robotv2.betterdailyquest.storage.dto;

import fr.robotv2.anchor.api.annotation.Column;
import fr.robotv2.anchor.api.annotation.Entity;
import fr.robotv2.anchor.api.annotation.Id;
import fr.robotv2.anchor.api.repository.Identifiable;
import fr.robotv2.betterdailyquest.storage.model.ActiveTask;
import org.jetbrains.annotations.ApiStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Entity("active_tasks")
public class ActiveTaskDto implements Identifiable<UUID> {

    @Id
    @Column("active_task_id")
    private UUID activeTaskUniqueId;

    @Column("parent_active_quest_id")
    private UUID parentActiveQuestId;

    @Column("parent_quest_id")
    private String parentQuestId;

    @Column("parent_quest_group_id")
    private String parentQuestGroupId;

    @Column("task_id")
    private int taskId;

    @Column("progress")
    private BigDecimal progress;

    @Column("required")
    private BigDecimal required;

    @Column("done")
    private boolean done;

    @ApiStatus.Internal
    public ActiveTaskDto() {

    }

    public ActiveTaskDto(ActiveTask activeTask) {
        this.activeTaskUniqueId = activeTask.getId();
        this.parentActiveQuestId = activeTask.getParentActiveQuestId();
        this.parentQuestId = activeTask.getParentQuestId();
        this.parentQuestGroupId = activeTask.getParentQuestGroupId();
        this.taskId = activeTask.getTaskId();
        this.progress = activeTask.getProgress();
        this.required = activeTask.getRequired();
        this.done = activeTask.isDone();
    }

    public ActiveTaskDto(UUID activeTaskUniqueId, UUID parentActiveQuestId, String parentQuestId, String parentQuestGroupId, int taskId, BigDecimal progress, BigDecimal required, boolean done) {
        this.activeTaskUniqueId = activeTaskUniqueId;
        this.parentActiveQuestId = parentActiveQuestId;
        this.parentQuestId = parentQuestId;
        this.parentQuestGroupId = parentQuestGroupId;
        this.taskId = taskId;
        this.progress = progress;
        this.required = required;
        this.done = done;
    }

    @Override
    public UUID getId() {
        return activeTaskUniqueId;
    }

    public UUID getParentActiveQuestId() {
        return parentActiveQuestId;
    }

    public String getParentQuestId() {
        return parentQuestId;
    }

    public String getParentQuestGroupId() {
        return parentQuestGroupId;
    }

    public int getTaskId() {
        return taskId;
    }

    public BigDecimal getProgress() {
        return progress;
    }

    public BigDecimal getRequired() {
        return required;
    }

    public boolean isDone() {
        return done;
    }
}
