package fr.robotv2.betterdailyquest.event;

import fr.robotv2.betterdailyquest.quest.Quest;
import fr.robotv2.betterdailyquest.quest.task.Task;
import fr.robotv2.betterdailyquest.storage.model.ActiveTask;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TaskEvent extends QuestEvent {

    private final static HandlerList HANDLER_LIST = new HandlerList();

    private final Task task;
    private final ActiveTask activeTask;

    public TaskEvent(Quest quest, Task task, ActiveTask activeTask) {
        super(quest);
        this.task = task;
        this.activeTask = activeTask;
    }

    public Task getTask() {
        return task;
    }

    public ActiveTask getActiveTask() {
        return activeTask;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
