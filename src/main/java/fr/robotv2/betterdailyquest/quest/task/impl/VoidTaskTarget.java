package fr.robotv2.betterdailyquest.quest.task.impl;

import fr.robotv2.betterdailyquest.quest.task.Task;
import fr.robotv2.betterdailyquest.quest.task.TaskTarget;

public class VoidTaskTarget extends TaskTarget<Void> {

    public VoidTaskTarget(Task task) {
        super(Void.class);
        if(task.getTaskSection().contains("required_target") || task.getTaskSection().contains("required_targets")) {
            throw new IllegalArgumentException("This task type does not accept a target.");
        }
    }

    @Override
    protected boolean matchesValue(Void value) {
        return true;
    }
}
