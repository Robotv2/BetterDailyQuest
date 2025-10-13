package fr.robotv2.betterdailyquest.storage;

public interface DirtyAware {

    boolean isDirty();

    void setDirty(boolean dirty);
}
