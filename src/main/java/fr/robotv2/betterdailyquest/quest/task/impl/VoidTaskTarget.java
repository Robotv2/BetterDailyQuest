package fr.robotv2.betterdailyquest.quest.task.impl;

import fr.robotv2.betterdailyquest.quest.task.Task;
import fr.robotv2.betterdailyquest.quest.task.TaskTarget;

public class VoidTaskTarget extends TaskTarget<Void> {

    public VoidTaskTarget(Task task) {
        super(Void.class);
    }

    @Override
    protected boolean matchesValue(Void value) {
        return true;
    }
}
