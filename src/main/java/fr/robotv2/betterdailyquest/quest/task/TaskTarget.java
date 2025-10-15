package fr.robotv2.betterdailyquest.quest.task;

import org.jetbrains.annotations.ApiStatus;

public abstract class TaskTarget<T> {

    private final Class<T> tClass;

    public TaskTarget(Class<T> tClass) {
        this.tClass = tClass;
    }

    public Class<T> getTargetClass() {
        return tClass;
    }

    @ApiStatus.Internal
    public boolean isTarget(Object value) {

        if(!getTargetClass().isInstance(value)) {
            return false;
        }

        try {
            return matchesValue(getTargetClass().cast(value));
        } catch (ClassCastException exception) {
            return false;
        }
    }

    abstract protected boolean matchesValue(T value);
}
